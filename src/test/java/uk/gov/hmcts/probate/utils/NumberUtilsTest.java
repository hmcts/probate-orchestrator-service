package uk.gov.hmcts.probate.utils;

import org.junit.Test;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

public class NumberUtilsTest {

    @Test
    public void shouldConvertPenniesToPounds() {
        assertThat(NumberUtils.penniesToPounds(1000L), equalTo(new BigDecimal("10.00")));
    }

    @Test
    public void shouldNotDefaultToZeroWhenLongNotNull() {
        assertThat(NumberUtils.defaultToZero(1000L), equalTo(1000L));
    }

    @Test
    public void shouldDefaultToZeroWhenLongNull() {
        Long val = null;
        assertThat(NumberUtils.defaultToZero(val), equalTo(0L));
    }

    @Test
    public void shouldNotDefaultToZeroWhenBigDecimalNotNull() {
        assertThat(NumberUtils.defaultToZero(1000L), equalTo(1000L));
    }

    @Test
    public void shouldDefaultToZeroWhenBigDecimalNull() {
        BigDecimal val = null;
        assertThat(NumberUtils.defaultToZero(val), equalTo(BigDecimal.ZERO));
    }
}