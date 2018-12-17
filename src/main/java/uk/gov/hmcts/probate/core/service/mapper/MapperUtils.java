package uk.gov.hmcts.probate.core.service.mapper;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class MapperUtils {

    private static final BigDecimal ONE_HUNDRED = BigDecimal.valueOf(100);

    public static Long poundsToPennies(BigDecimal value) {
        return value.multiply(ONE_HUNDRED).longValue();
    }

    public static BigDecimal penniesToPounds(Long value) {
        return BigDecimal.valueOf(value).divide(ONE_HUNDRED, 2, RoundingMode.HALF_UP);
    }

    private MapperUtils(){
    }
}
