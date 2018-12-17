package uk.gov.hmcts.probate.core.service.mapper;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

class PoundsConverterTest {

    private static final Long longValue = 250015L;

    private static final BigDecimal bigDecimalValue = new BigDecimal("2500.15");

    private PoundsConverter poundsConverter = new PoundsConverter();

    @Test
    void shouldConvertFromPoundsToPennies() {
        assertThat(poundsConverter.poundsToPennies(bigDecimalValue), equalTo(longValue));
    }

    @Test
    void shouldConvertPenniesToPounds() {
        assertThat(poundsConverter.penniesToPounds(longValue), equalTo(bigDecimalValue));
    }
}