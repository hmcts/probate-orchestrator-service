//package uk.gov.hmcts.probate.core.service;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Component;
//import uk.gov.hmcts.probate.client.payment.PaymentApi;
//import uk.gov.hmcts.probate.core.service.fees.FeesChangedException;
//import uk.gov.hmcts.probate.core.service.fees.FeesNotCalculatedException;
//import uk.gov.hmcts.probate.core.service.payment.PaymentConfiguration;
//import uk.gov.hmcts.probate.model.payment.CardPaymentRequest;
//import uk.gov.hmcts.probate.model.payment.PaymentDto;
//import uk.gov.hmcts.probate.model.payment.PaymentsDto;
//import uk.gov.hmcts.probate.service.FeesService;
//import uk.gov.hmcts.probate.service.PaymentService;
//import uk.gov.hmcts.reform.probate.model.forms.Fees;
//import uk.gov.hmcts.reform.probate.model.forms.Form;
//
//import java.math.BigDecimal;
//
//@Component
//@RequiredArgsConstructor
//public class PaymentServiceImpl implements PaymentService {
//
//    private final FeesService feesService;
//
//    private final PaymentApi paymentApi;
//
//    private final SecurityUtils securityUtils;
//
//    private final PaymentConfiguration paymentConfiguration;
//
//    @Override
//    public PaymentDto createPayment(Form form) {
//
//        String caseId = form.getCcdCase().getId().toString();
//        String serviceAuthorisation = securityUtils.getServiceAuthorisation();
//        String authorisation = securityUtils.getAuthorisation();
//
//        PaymentsDto paymentsDto = paymentApi.getPayments(authorisation, serviceAuthorisation, caseId, "Probate");
//        for (PaymentDto paymentDto : paymentsDto.getPayments()) {
//            if (paymentDto.getStatus().equals("Success")) {
//                return paymentDto;
//            }
//            if (paymentDto.getStatus().equals("Initiated")) {
//                return paymentApi.getCardPayment(serviceAuthorisation, authorisation, paymentDto.getPaymentReference());
//            }
//        }
//
//
//        Fees fees = form.getFees();
//        if (form.getFees() == null) {
//            throw new FeesNotCalculatedException();
//        }
//
//        if ((form.getFees().getTotal().compareTo(BigDecimal.ZERO) == 0) )  {
//            // submit
//        }
//
//        Fees calculatedFees = feesService.calculateFees(form.getType(), form);
//        if (fees.equals(calculatedFees)) {
//            throw new FeesChangedException();
//        }
//
//
//        CardPaymentRequest cardPaymentRequest = CardPaymentRequest.builder()
//            .amount(fees.getTotal())
//            .description("Probate Fees")
//            .ccdCaseNumber(caseId)
//            .service(paymentConfiguration.getServiceId())
//            .siteId(paymentConfiguration.getSiteId())
//            .currency(paymentConfiguration.getCurrency())
//            .build();
//
//        return null;
//    }
//}
