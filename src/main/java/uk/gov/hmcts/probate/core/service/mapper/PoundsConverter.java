package uk.gov.hmcts.probate.core.service.mapper;

import org.springframework.stereotype.Component;
import uk.gov.hmcts.probate.core.service.mapper.qualifiers.ToPennies;
import uk.gov.hmcts.probate.core.service.mapper.qualifiers.ToPounds;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class PoundsConverter {

    private static final BigDecimal ONE_HUNDRED = BigDecimal.valueOf(100);

    @ToPennies
    public Long poundsToPennies(BigDecimal value) {
        if (value == null) {
            return null;
        }
        return value.multiply(ONE_HUNDRED).longValue();
    }

    @ToPounds
    public BigDecimal penniesToPounds(Long value) {
        if (value == null) {
            return null;
        }
        return BigDecimal.valueOf(value).divide(ONE_HUNDRED, 2, RoundingMode.HALF_UP);
    }

}
