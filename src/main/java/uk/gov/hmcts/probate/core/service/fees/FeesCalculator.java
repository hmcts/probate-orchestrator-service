package uk.gov.hmcts.probate.core.service.fees;

import uk.gov.hmcts.reform.probate.model.forms.Fees;
import uk.gov.hmcts.reform.probate.model.forms.Form;

public interface FeesCalculator<T extends Form> {

    Fees calculateFees(T form);
}
