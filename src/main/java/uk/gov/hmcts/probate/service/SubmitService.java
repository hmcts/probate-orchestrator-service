package uk.gov.hmcts.probate.service;

import uk.gov.hmcts.reform.probate.model.ProbateType;
import uk.gov.hmcts.reform.probate.model.forms.Form;

public interface SubmitService {

    Form getCase(String identifier, ProbateType probateType);

    Form saveDraft(String identifier, Form form);

    Form submit(String identifier, Form form);

    Form updatePayments(String identifier, Form form);
}
