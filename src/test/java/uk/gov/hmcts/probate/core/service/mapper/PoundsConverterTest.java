package uk.gov.hmcts.probate.core.service.mapper;


import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

public class PoundsConverterTest {

    private static final Long longValue = 250015L;

    private static final BigDecimal bigDecimalValue = new BigDecimal("2500.15");

    private PoundsConverter poundsConverter = new PoundsConverter();

    @Test
    public void shouldConvertFromPoundsToPennies() {
        assertThat(poundsConverter.poundsToPennies(bigDecimalValue), equalTo(longValue));
    }

    @Test
    public void shouldConvertNullFromPoundsToPennies() {
        assertThat(poundsConverter.poundsToPennies(null), is(nullValue()));
    }

    @Test
    public void shouldConvertPoundsToString() {
        assertThat(poundsConverter.penniesToPoundsString(null), is(nullValue()));
    }

    @Test
    public void shouldConvertPenniesToPounds() {
        assertThat(poundsConverter.penniesToPoundsString(longValue), equalTo(bigDecimalValue.toPlainString()));
    }

    @Test
    public void shouldConvertNullPenniesToPounds() {
        assertThat(poundsConverter.poundsToPennies(null), is(nullValue()));
    }
}
