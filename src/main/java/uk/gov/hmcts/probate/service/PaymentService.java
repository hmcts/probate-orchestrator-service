package uk.gov.hmcts.probate.service;

import uk.gov.hmcts.reform.probate.model.ProbateType;
import uk.gov.hmcts.reform.probate.model.forms.PaymentSubmission;

public interface PaymentService {

    PaymentSubmission createPaymentSubmission(String identifier, ProbateType probateType, String returnUrl, String callbackUrl);

    PaymentSubmission updatePaymentSubmission(String identifier, ProbateType probateType);

}
