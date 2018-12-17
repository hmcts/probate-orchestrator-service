package uk.gov.hmcts.probate.core.service.mapper;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

class MapperUtilsTest {

    private static final Long longValue = 250015L;

    private static final BigDecimal bigDecimalValue = new BigDecimal("2500.15");

    @Test
    void shouldConvertFromPoundsToPennies() {
        assertThat(MapperUtils.poundsToPennies(bigDecimalValue), equalTo(longValue));
    }

    @Test
    void shouldConvertPenniesToPounds() {
        assertThat(MapperUtils.penniesToPounds(longValue), equalTo(bigDecimalValue));
    }
}