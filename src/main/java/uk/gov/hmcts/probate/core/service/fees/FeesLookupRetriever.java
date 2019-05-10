package uk.gov.hmcts.probate.core.service.fees;

import java.math.BigDecimal;

public interface FeesLookupRetriever {

    FeesCalculation lookupFeeForApplicationFee(BigDecimal amount, String authorization, String serviceAuthorization);

    FeesCalculation lookupFeeForCopies(Long volume, String authorization, String serviceAuthorization);
}