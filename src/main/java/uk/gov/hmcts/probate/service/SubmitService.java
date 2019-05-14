package uk.gov.hmcts.probate.service;

import uk.gov.hmcts.reform.probate.model.ProbateType;
import uk.gov.hmcts.reform.probate.model.cases.CasePayment;
import uk.gov.hmcts.reform.probate.model.cases.ProbateCaseDetails;
import uk.gov.hmcts.reform.probate.model.forms.Form;

public interface SubmitService {

    Form getCase(String identifier, ProbateType probateType);

    Form saveDraft(String identifier, Form form);

    Form submit(String identifier, Form form);

    Form update(String identifier, Form form);

    Form updatePayments(String identifier, Form form);

    ProbateCaseDetails updatePaymentsByCaseId(String caseId, CasePayment casePayment);

}
