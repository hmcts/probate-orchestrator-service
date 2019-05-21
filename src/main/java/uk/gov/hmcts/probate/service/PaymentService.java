package uk.gov.hmcts.probate.service;

import uk.gov.hmcts.reform.probate.model.forms.Form;
import uk.gov.hmcts.reform.probate.model.payments.PaymentDto;

import java.util.Optional;

public interface PaymentService {

    PaymentDto createPayment(Form form, String returnUrl);


    Optional<PaymentDto> findNonFailedPaymentByCaseId(String caseId);

}
