package uk.gov.hmcts.probate.service;

import uk.gov.hmcts.reform.probate.model.forms.Form;

public interface SubmitService {

    Form saveDrafts(Form form);
}
