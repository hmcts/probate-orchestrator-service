package uk.gov.hmcts.probate.service;

import uk.gov.hmcts.reform.probate.model.payments.PaymentDto;

public interface PaymentUpdateService {

    void paymentUpdate( PaymentDto paymentDto);
}
