package uk.gov.hmcts.probate.utils;

import lombok.experimental.UtilityClass;

import java.math.BigDecimal;
import java.math.RoundingMode;

@UtilityClass
public class NumberUtils {

    private final BigDecimal ONE_HUNDRED = BigDecimal.valueOf(100);

    public BigDecimal penniesToPounds(Long value) {
        return BigDecimal.valueOf(value).divide(ONE_HUNDRED, 2, RoundingMode.HALF_UP);
    }

    public BigDecimal defaultToZero(BigDecimal value) {
        return value == null ? BigDecimal.ZERO : value;
    }

    public Long defaultToZero(Long value) {
        return value == null ? 0L : value;
    }
}
