package uk.gov.hmcts.probate.core.service;

import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import uk.gov.hmcts.probate.client.submit.SubmitServiceApi;
import uk.gov.hmcts.probate.core.service.fees.FeesChangedException;
import uk.gov.hmcts.probate.core.service.fees.FeesNotCalculatedException;
import uk.gov.hmcts.probate.core.service.mapper.FormMapper;
import uk.gov.hmcts.probate.model.payment.PaymentDto;
import uk.gov.hmcts.probate.service.FeesService;
import uk.gov.hmcts.probate.service.PaymentService;
import uk.gov.hmcts.probate.service.SubmitService;
import uk.gov.hmcts.reform.probate.model.PaymentStatus;
import uk.gov.hmcts.reform.probate.model.ProbateType;
import uk.gov.hmcts.reform.probate.model.cases.CaseData;
import uk.gov.hmcts.reform.probate.model.cases.CasePayment;
import uk.gov.hmcts.reform.probate.model.cases.ProbateCaseDetails;
import uk.gov.hmcts.reform.probate.model.cases.ProbatePaymentDetails;
import uk.gov.hmcts.reform.probate.model.cases.SubmitResult;
import uk.gov.hmcts.reform.probate.model.forms.CcdCase;
import uk.gov.hmcts.reform.probate.model.forms.Fees;
import uk.gov.hmcts.reform.probate.model.forms.Form;
import uk.gov.hmcts.reform.probate.model.forms.Payment;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
@Slf4j
public class SubmitServiceImpl implements SubmitService {

    private final Map<ProbateType, FormMapper> mappers;

    private final SubmitServiceApi submitServiceApi;

    private final SecurityUtils securityUtils;

    private final Map<ProbateType, Function<Form, String>> formIdentifierFunctionMap;

    private final PaymentService paymentService;

    private final FeesService feesService;

    @Override
    public Form getCase(String identifier, ProbateType probateType) {
        log.info("Get case called for : {}", probateType.getName());
        FormMapper formMapper = mappers.get(probateType);
        String serviceAuthorisation = securityUtils.getServiceAuthorisation();
        String authorisation = securityUtils.getAuthorisation();
        ProbateCaseDetails probateCaseDetails = submitServiceApi.getCase(authorisation,
            serviceAuthorisation, identifier, probateType.getCaseType().name());
        Form form = formMapper.fromCaseData(probateCaseDetails.getCaseData());
        updateCcdCase(probateCaseDetails, form);
        return form;
    }

    @Override
    public Form saveDraft(String identifier, Form form) {
        log.info("Save draft called");
        assertIdentifier(identifier, form);
        FormMapper formMapper = mappers.get(form.getType());
        ProbateCaseDetails pcdEntered = ProbateCaseDetails.builder().caseData(mapToCase(form, formMapper)).build();
        ProbateCaseDetails probateCaseDetails = submitServiceApi.saveDraft(
            securityUtils.getAuthorisation(),
            securityUtils.getServiceAuthorisation(),
            identifier,
            pcdEntered
        );
        return mapFromCase(formMapper, probateCaseDetails);
    }

    @Override
    public Form submit(String identifier, Form form) {
        log.info("Submit called for");
        assertIdentifier(identifier, form);
        FormMapper formMapper = mappers.get(form.getType());
        ProbateCaseDetails probateCaseDetails = ProbateCaseDetails.builder().caseData(mapToCase(form, formMapper)).build();

        log.debug("calling submit on submitserviceapi");
        SubmitResult submitResult = submitServiceApi.submit(
            securityUtils.getAuthorisation(),
            securityUtils.getServiceAuthorisation(),
            identifier, probateCaseDetails

        );
        return mapFromCase(formMapper, submitResult.getProbateCaseDetails());
    }

    @Override
    public Form update(String identifier, ProbateType probateType, String returnUrl) {
        Form form = getCase(identifier, probateType);
        if (form.getFees() == null) {
            throw new FeesNotCalculatedException();
        }

        if (form.getFees().getApplicationFee().compareTo(BigDecimal.ZERO) == 0
            && form.getCopies().getUk() == 0L && form.getCopies().getOverseas() == 0L) {
            return updateCaseState(identifier, probateType);
        }

        Optional<PaymentDto> paymentOptional = paymentService.findNonFailedPaymentByCaseId(form.getCcdCase().getId().toString());
        if (paymentOptional.isPresent()) {
            PaymentDto paymentDto = paymentOptional.get();
            return updatePayment(identifier, form, paymentDto);
        }

        Fees calculatedFees = feesService.calculateFees(form.getType(), form);
        if (!form.getFees().equals(calculatedFees)) {
            throw new FeesChangedException();
        }

        PaymentDto paymentDto = paymentService.createPayment(form, returnUrl);
        return updatePayment(identifier, form, paymentDto);
    }

    public Form updatePayment(String identifier, Form form, PaymentDto paymentDto) {
        Payment payment = Payment.builder()
            .reference(paymentDto.getReference())
            .status(PaymentStatus.valueOf(paymentDto.getStatus()))
            .date(paymentDto.getDateCreated())
            .method(paymentDto.getMethod())
            .status(PaymentStatus.getPaymentStatusByName(paymentDto.getStatus()))
            .amount(paymentDto.getAmount())
            .total(paymentDto.getAmount())
            .build();
        form.setPayments(Lists.newArrayList(payment));
        return updatePayments(identifier, form);
    }

    public Form updateCaseState(String identifier, ProbateType probateType) {
        SubmitResult submitResult = submitServiceApi.update(
            securityUtils.getAuthorisation(),
            securityUtils.getServiceAuthorisation(),
            identifier,
            probateType.getCaseType().name()
        );
        FormMapper formMapper = mappers.get(probateType);
        return mapFromCase(formMapper, submitResult.getProbateCaseDetails());
    }

    private void assertIdentifier(String identifier, Form form) {
        String identifierInForm = Optional.of(formIdentifierFunctionMap.get(form.getType()))
            .orElseThrow(IllegalArgumentException::new)
            .apply(form);
        Assert.isTrue(identifierInForm != null && identifierInForm.equals(identifier),
            "Path variable identifier must match identifier in form");
    }

    private void updateCcdCase(ProbateCaseDetails probateCaseDetails, Form formResponse) {
        formResponse.setCcdCase(CcdCase.builder()
            .id(Long.valueOf(probateCaseDetails.getCaseInfo().getCaseId()))
            .state(probateCaseDetails.getCaseInfo().getState())
            .build());
    }

    @Override
    public Form updatePayments(String identifier, Form form) {
        log.info("update Payments called");
        Assert.isTrue(!CollectionUtils.isEmpty(form.getPayments()),
            "Cannot update case with no payments, there needs to be at least one payment");
        FormMapper formMapper = mappers.get(form.getType());
        CaseData caseData = formMapper.toCaseData(form);
        CasePayment casePayment = caseData.getPayments().get(0).getValue();
        log.debug("calling update Payments in submitServiceApi");
        ProbateCaseDetails probateCaseDetails = submitServiceApi.updatePayments(
            securityUtils.getAuthorisation(),
            securityUtils.getServiceAuthorisation(),
            identifier,
            ProbatePaymentDetails.builder().caseType(form.getType().getCaseType())
                .payment(casePayment)
                .build());
        return mapFromCase(formMapper, probateCaseDetails);
    }

    private CaseData mapToCase(Form form, FormMapper formMapper) {
        return formMapper.toCaseData(form);
    }

    private Form mapFromCase(FormMapper formMapper, ProbateCaseDetails probateCaseDetails) {
        Form formResponse = formMapper.fromCaseData(probateCaseDetails.getCaseData());
        updateCcdCase(probateCaseDetails, formResponse);
        return formResponse;
    }
}
