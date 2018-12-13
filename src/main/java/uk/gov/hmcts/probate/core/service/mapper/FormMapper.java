package uk.gov.hmcts.probate.core.service.mapper;

import uk.gov.hmcts.reform.probate.model.cases.CaseData;
import uk.gov.hmcts.reform.probate.model.forms.Form;

public interface FormMapper<R extends CaseData, T extends Form> {

    R map(T form);
}
