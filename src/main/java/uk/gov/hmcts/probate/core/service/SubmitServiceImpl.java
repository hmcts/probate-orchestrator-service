package uk.gov.hmcts.probate.core.service;

import com.google.common.collect.Sets;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import uk.gov.hmcts.probate.client.SubmitServiceApi;
import uk.gov.hmcts.probate.core.service.mapper.FormMapper;
import uk.gov.hmcts.probate.service.BackOfficeService;
import uk.gov.hmcts.probate.service.SubmitService;
import uk.gov.hmcts.reform.probate.model.PaymentStatus;
import uk.gov.hmcts.reform.probate.model.ProbateType;
import uk.gov.hmcts.reform.probate.model.cases.CaseData;
import uk.gov.hmcts.reform.probate.model.cases.CasePayment;
import uk.gov.hmcts.reform.probate.model.cases.CaseType;
import uk.gov.hmcts.reform.probate.model.cases.ProbateCaseDetails;
import uk.gov.hmcts.reform.probate.model.cases.ProbatePaymentDetails;
import uk.gov.hmcts.reform.probate.model.cases.SubmitResult;
import uk.gov.hmcts.reform.probate.model.forms.CcdCase;
import uk.gov.hmcts.reform.probate.model.forms.Form;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
@Slf4j
public class SubmitServiceImpl implements SubmitService {

    private final Map<ProbateType, FormMapper> mappers;

    private final SubmitServiceApi submitServiceApi;

    private final BackOfficeService backOfficeService;

    private final SecurityUtils securityUtils;

    private final Map<ProbateType, Function<Form, String>> formIdentifierFunctionMap;

    private final Set<CaseType> caseTypesForNotifications = Sets.newHashSet(CaseType.CAVEAT);

    @Override
    public Form getCase(String identifier, ProbateType probateType) {
        log.info("Get case called for : {}", probateType.getName());
        FormMapper formMapper = mappers.get(probateType);
        String serviceAuthorisation = securityUtils.getServiceAuthorisation();
        String authorisation = securityUtils.getAuthorisation();
        ProbateCaseDetails probateCaseDetails = submitServiceApi.getCase(authorisation,
            serviceAuthorisation, identifier, probateType.getCaseType().getName());
        return formMapper.fromCaseData(probateCaseDetails.getCaseData());
    }

    @Override
    public Form saveDraft(String identifier, Form form) {
        log.info("Save draft called");
        assertIdentifier(identifier, form);
        FormMapper formMapper = mappers.get(form.getType());
        ProbateCaseDetails probateCaseDetails = submitServiceApi.saveDraft(
            securityUtils.getAuthorisation(),
            securityUtils.getServiceAuthorisation(),
            identifier,
            ProbateCaseDetails.builder().caseData(mapToCase(form, formMapper)).build()
        );
        return mapFromCase(formMapper, probateCaseDetails);
    }

    @Override
    public Form submit(String identifier, Form form) {
        log.info("Submit called for");
        assertIdentifier(identifier, form);
        FormMapper formMapper = mappers.get(form.getType());
        log.debug("calling submit on submitserviceapi");
        SubmitResult submitResult = submitServiceApi.submit(
            securityUtils.getAuthorisation(),
            securityUtils.getServiceAuthorisation(),
            identifier,
            ProbateCaseDetails.builder().caseData(mapToCase(form, formMapper)).build()
        );
        return mapFromCase(formMapper, submitResult.getProbateCaseDetails());
    }

    @Override
    public Form update(String identifier, Form form) {
        log.info("Update called for");
        assertIdentifier(identifier, form);
        FormMapper formMapper = mappers.get(form.getType());
        log.debug("calling update on submitserviceapi");
        SubmitResult submitResult = submitServiceApi.update(
            securityUtils.getAuthorisation(),
            securityUtils.getServiceAuthorisation(),
            identifier,
            ProbateCaseDetails.builder().caseData(mapToCase(form, formMapper)).build()
        );
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
        String authorisation = securityUtils.getAuthorisation();
        String serviceAuthorisation = securityUtils.getServiceAuthorisation();

        ProbateCaseDetails existingCase = submitServiceApi.getCase(authorisation,
            serviceAuthorisation, identifier, form.getType().getCaseType().name());

        FormMapper formMapper = mappers.get(form.getType());
        CaseData caseData = formMapper.toCaseData(form);

        existingCase.getCaseData().setPayments(caseData.getPayments());
        sendNotification(existingCase);
        
        log.debug("calling update Payments in submitServiceApi");
        ProbateCaseDetails probateCaseDetails = submitServiceApi.createCase(
            authorisation,
            serviceAuthorisation,
            identifier,
            existingCase
        );
        return mapFromCase(formMapper, probateCaseDetails);
    }

    public Optional<CaseData> sendNotification(ProbateCaseDetails probateCaseDetails) {
        CaseType caseType = CaseType.getCaseType(probateCaseDetails.getCaseData());
        CasePayment casePayment = probateCaseDetails.getCaseData().getPayments().get(0).getValue();
        if (caseTypesForNotifications.contains(caseType) && PaymentStatus.SUCCESS.equals(casePayment.getStatus())) {
            return Optional.ofNullable(backOfficeService.sendNotification(probateCaseDetails));
        }
        return Optional.empty();
    }

    @Override
    public ProbateCaseDetails updateByCaseId(String caseId, ProbateCaseDetails caseDetails) {
        log.info("Call to submit service with id {}", caseId);
        ProbateCaseDetails probateCaseDetails = submitServiceApi.updateByCaseId(
            securityUtils.getAuthorisation(),
            securityUtils.getServiceAuthorisation(),
            caseId,
            caseDetails);
        return probateCaseDetails;
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
