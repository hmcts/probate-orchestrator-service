package uk.gov.hmcts.probate.service;

import uk.gov.hmcts.probate.model.pay.PaymentDto;
import uk.gov.hmcts.reform.probate.model.forms.Form;

public interface PaymentService {

    PaymentDto createPayment(Form form);

}
