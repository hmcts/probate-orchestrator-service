package uk.gov.hmcts.probate.service;

import uk.gov.hmcts.reform.probate.model.ProbateType;
import uk.gov.hmcts.reform.probate.model.forms.Form;

public interface SubmitService {

    Form getCase(String applicantEmail, ProbateType probateType);

    Form saveDraft(String applicantEmail, Form form);

    Form submit(String applicantEmail, Form form);

    Form updatePayments(String applicantEmail, Form form);
}
