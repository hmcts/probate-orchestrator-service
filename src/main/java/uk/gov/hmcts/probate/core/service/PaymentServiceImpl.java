package uk.gov.hmcts.probate.core.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.probate.client.payment.PaymentApi;
import uk.gov.hmcts.probate.core.service.fees.FeesChangedException;
import uk.gov.hmcts.probate.core.service.fees.FeesNotCalculatedException;
import uk.gov.hmcts.probate.core.service.payment.PaymentConfiguration;
import uk.gov.hmcts.probate.service.FeesService;
import uk.gov.hmcts.probate.service.PaymentService;
import uk.gov.hmcts.probate.service.SubmitService;
import uk.gov.hmcts.reform.probate.model.PaymentStatus;
import uk.gov.hmcts.reform.probate.model.ProbateType;
import uk.gov.hmcts.reform.probate.model.cases.CaseState;
import uk.gov.hmcts.reform.probate.model.forms.Fees;
import uk.gov.hmcts.reform.probate.model.forms.Form;
import uk.gov.hmcts.reform.probate.model.forms.PaymentSubmission;
import uk.gov.hmcts.reform.probate.model.payments.CardPaymentRequest;
import uk.gov.hmcts.reform.probate.model.payments.FeeDto;
import uk.gov.hmcts.reform.probate.model.payments.PaymentDto;
import uk.gov.hmcts.reform.probate.model.payments.PaymentsDto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private static final String PROBATE_FEES_MEMO_LINE = "Probate Fees";
    private static final String ADDITIONAL_OVERSEAS_COPIES_MEMO_LINE = "Additional overseas copies";
    private static final String ADDITIONAL_UK_COPIES_MEMO_LINE = "Additional UK copies";
    private static final String PROBATE_FEES = "Probate Fees";
    private static final int APPLICATION_FEE_VOLUME = 1;
    private static final String SUCCESS_STATUS = "Success";
    private static final String INITIATED_STATUS = "Initiated";
    public static final String SERVICE_NAME = "Probate";

    private final PaymentApi paymentApi;

    private final SecurityUtils securityUtils;

    private final PaymentConfiguration paymentConfiguration;

    private final FeesService feesService;

    private final SubmitService submitService;

    @Override
    public PaymentSubmission createPaymentSubmission(String identifier, ProbateType probateType, String returnUrl, String callbackUrl) {
        Form form = submitService.getCase(identifier, probateType);
        if (form.getFees() == null) {
            throw new FeesNotCalculatedException();
        }

        if (form.getFees().getApplicationFee().compareTo(BigDecimal.ZERO) == 0
            && form.getCopies().getUk() == 0L && form.getCopies().getOverseas() == 0L) {
            Form updatedForm = submitService.update(identifier, probateType, PaymentDto.builder().status(PaymentStatus.NOT_REQUIRED.getName()).build());
            return PaymentSubmission.builder().form(updatedForm).redirect(false).build();
        }

        Optional<PaymentDto> paymentOptional = findNonFailedPaymentByCaseId(form.getCcdCase().getId().toString());
        if (paymentOptional.isPresent()) {
            PaymentDto paymentDto = paymentOptional.get();
            Boolean redirect = !paymentDto.getStatus().equalsIgnoreCase(PaymentStatus.SUCCESS.getName());
            Form updatedForm = submitService.update(identifier, probateType, paymentDto);
            return PaymentSubmission.builder().form(updatedForm).redirect(redirect).build();
        }

        Fees calculatedFees = feesService.calculateFees(form.getType(), form);
        if (!form.getFees().equals(calculatedFees)) {
            throw new FeesChangedException();
        }

        PaymentDto paymentDto = createPayment(form, returnUrl, callbackUrl);
        Form updatedForm = submitService.update(identifier, probateType, paymentDto);
        return PaymentSubmission.builder().form(updatedForm).redirect(true)
            .redirectUrl(paymentDto.getLinks().getNextUrl().getHref())
            .build();
    }

    @Override
    public PaymentSubmission updatePaymentSubmission(String identifier, ProbateType probateType) {
        Form form = submitService.getCase(identifier, probateType);
        if (form.getCcdCase().getState().equals(CaseState.CASE_CREATED.getName())) {
            return PaymentSubmission.builder().form(form).redirect(false).build();
        }
        String paymentReference = form.getPayment().getReference();
        String serviceAuthorisation = securityUtils.getServiceAuthorisation();
        String authorisation = securityUtils.getAuthorisation();
        PaymentDto paymentDto = paymentApi.getCardPayment(authorisation, serviceAuthorisation, paymentReference);
        Form updatedForm = submitService.update(identifier, probateType, paymentDto);
        if (!paymentDto.getStatus().equalsIgnoreCase(PaymentStatus.SUCCESS.getName())) {
            return PaymentSubmission.builder().form(updatedForm).redirect(true).build();
        }
        return PaymentSubmission.builder().form(updatedForm).redirect(false).build();
    }

    private PaymentDto createPayment(Form form, String returnUrl, String callbackUrl) {
        String caseId = form.getCcdCase().getId().toString();
        String serviceAuthorisation = securityUtils.getServiceAuthorisation();
        String authorisation = securityUtils.getAuthorisation();
        CardPaymentRequest cardPaymentRequest = createCardPaymentRequest(form, caseId, form.getFees());
        return paymentApi.createCardPayment(authorisation, serviceAuthorisation, returnUrl, callbackUrl, cardPaymentRequest);
    }

    private Optional<PaymentDto> findNonFailedPaymentByCaseId(String caseId) {
        String serviceAuthorisation = securityUtils.getServiceAuthorisation();
        String authorisation = securityUtils.getAuthorisation();
        PaymentsDto paymentsDto = paymentApi.getPayments(authorisation, serviceAuthorisation, caseId, SERVICE_NAME);
        for (PaymentDto paymentDto : paymentsDto.getPayments()) {
            if (paymentDto.getStatus().equals(SUCCESS_STATUS)) {
                return Optional.of(paymentDto);
            }
            if (paymentDto.getStatus().equals(INITIATED_STATUS)) {
                return Optional.of(paymentApi.getCardPayment(serviceAuthorisation, authorisation, paymentDto.getReference()));
            }
        }
        return Optional.empty();
    }

    private CardPaymentRequest createCardPaymentRequest(Form form, String caseId, Fees fees) {
        return CardPaymentRequest.builder()
            .amount(fees.getTotal())
            .description(PROBATE_FEES)
            .ccdCaseNumber(caseId)
            .service(paymentConfiguration.getServiceId())
            .siteId(paymentConfiguration.getSiteId())
            .currency(paymentConfiguration.getCurrency())
            .fees(createFees(caseId, form))
            .build();
    }

    private List<FeeDto> createFees(String caseId, Form form) {
        Fees fees = form.getFees();
        List<FeeDto> feeDtos = new ArrayList<>();
        if (fees.getApplicationFee().compareTo(BigDecimal.ZERO) != 0) {
            feeDtos.add(FeeDto.builder()
                .calculatedAmount(fees.getApplicationFee())
                .ccdCaseNumber(caseId)
                .code(fees.getApplicationFeeCode())
                .version(fees.getApplicationFeeVersion())
                .reference(securityUtils.getUserId())
                .volume(APPLICATION_FEE_VOLUME)
                .memoLine(PROBATE_FEES_MEMO_LINE)
                .build());
        }

        if (fees.getOverseasCopiesFee().compareTo(BigDecimal.ZERO) != 0) {
            feeDtos.add(FeeDto.builder()
                .calculatedAmount(fees.getOverseasCopiesFee())
                .ccdCaseNumber(caseId)
                .code(fees.getOverseasCopiesFeeCode())
                .version(fees.getOverseasCopiesFeeVersion())
                .reference(securityUtils.getUserId())
                .volume(form.getCopies().getOverseas().intValue())
                .memoLine(ADDITIONAL_OVERSEAS_COPIES_MEMO_LINE)
                .build());
        }

        if (fees.getUkCopiesFee().compareTo(BigDecimal.ZERO) != 0) {
            feeDtos.add(FeeDto.builder()
                .calculatedAmount(fees.getUkCopiesFee())
                .ccdCaseNumber(caseId)
                .code(fees.getUkCopiesFeeCode())
                .version(fees.getUkCopiesFeeVersion())
                .reference(securityUtils.getUserId())
                .volume(form.getCopies().getUk().intValue())
                .memoLine(ADDITIONAL_UK_COPIES_MEMO_LINE)
                .build());
        }
        return feeDtos;
    }
}
