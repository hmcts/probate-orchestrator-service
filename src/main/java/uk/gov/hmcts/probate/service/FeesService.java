package uk.gov.hmcts.probate.service;

import uk.gov.hmcts.reform.probate.model.ProbateType;
import uk.gov.hmcts.reform.probate.model.forms.Fees;
import uk.gov.hmcts.reform.probate.model.forms.Form;

public interface FeesService {

    Form updateFees(String correlationId, ProbateType probateType);

    Fees calculateFees(ProbateType probateType, Form form);

}
