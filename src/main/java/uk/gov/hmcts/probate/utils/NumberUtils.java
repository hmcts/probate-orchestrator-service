package uk.gov.hmcts.probate.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

public final class NumberUtils {

    private static final BigDecimal ONE_HUNDRED = BigDecimal.valueOf(100);

    private NumberUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static BigDecimal penniesToPounds(Long value) {
        return BigDecimal.valueOf(value).divide(ONE_HUNDRED, 2, RoundingMode.HALF_UP);
    }

    public static BigDecimal defaultToZero(BigDecimal value) {
        return value == null ? BigDecimal.ZERO : value;
    }

    public static Long defaultToZero(Long value) {
        return value == null ? 0L : value;
    }
}
