package uk.gov.hmcts.probate.core.service.mapper;

import org.hamcrest.core.IsNull;
import org.junit.jupiter.api.Test;
import uk.gov.hmcts.reform.probate.model.IhtFormType;

import java.math.BigDecimal;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class IhtValuesMapperTest {

    @Test
    public void shouldMapIHT205Value() {
        assertThat(IhtValuesMapper.getGrossIht205(IhtFormType.optionIHT205, 10000L),
            equalTo(new BigDecimal("100.00")));
    }

    @Test
    public void shouldReturnNullifNotIHT205() {
        assertThat(IhtValuesMapper.getGrossIht205(IhtFormType.optionIHT207, 10000L),
            IsNull.nullValue());
    }

    @Test
    public void shouldMapIHT207Value() {
        assertThat(IhtValuesMapper.getGrossIht207(IhtFormType.optionIHT207, 10000L),
            equalTo(new BigDecimal("100.00")));
    }

    @Test
    public void shouldReturnNullifNotIHT207() {
        assertThat(IhtValuesMapper.getGrossIht207(IhtFormType.optionIHT400421, 10000L),
            IsNull.nullValue());
    }

    @Test
    public void shouldMapIHT400421Value() {
        assertThat(IhtValuesMapper.getGrossIht400421(IhtFormType.optionIHT400421, 10000L),
            equalTo(new BigDecimal("100.00")));
    }

    @Test
    public void shouldReturnNullifNotIHT400421() {
        assertThat(IhtValuesMapper.getGrossIht400421(IhtFormType.optionIHT207, 10000L),
            IsNull.nullValue());
    }

    @Test
    public void shouldMapNetIHT205Value() {
        assertThat(IhtValuesMapper.getNetIht205(IhtFormType.optionIHT205, 10000L),
            equalTo(new BigDecimal("100.00")));
    }

    @Test
    public void shouldReturnNullifNotNetIHT205() {
        assertThat(IhtValuesMapper.getNetIht205(IhtFormType.optionIHT207, 10000L),
            IsNull.nullValue());
    }

    @Test
    public void shouldMapIHTNet207Value() {
        assertThat(IhtValuesMapper.getNetIht207(IhtFormType.optionIHT207, 10000L),
            equalTo(new BigDecimal("100.00")));
    }

    @Test
    public void shouldReturnNullifNotNetIHT207() {
        assertThat(IhtValuesMapper.getNetIht207(IhtFormType.optionIHT400421, 10000L),
            IsNull.nullValue());
    }

    @Test
    public void shouldMapIHTNet400421Value() {
        assertThat(IhtValuesMapper.getNetIht400421(IhtFormType.optionIHT400421, 10000L),
            equalTo(new BigDecimal("100.00")));
    }

    @Test
    public void shouldReturnNullifNotNetIHT400421() {
        assertThat(IhtValuesMapper.getNetIht400421(IhtFormType.optionIHT207, 10000L),
            IsNull.nullValue());
    }

    @Test
    public void shouldMapIHTNet400Value() {
        assertThat(IhtValuesMapper.getNetIht400(IhtFormType.optionIHT400, 10000L),
                equalTo(new BigDecimal("100.00")));
    }

    @Test
    public void shouldReturnNullifNotIHT400() {
        assertThat(IhtValuesMapper.getGrossIht400(IhtFormType.optionIHT207, 10000L),
                IsNull.nullValue());
    }

    @Test
    public void shouldMapNotRequired() {
        assertThat(IhtValuesMapper.getNetNotRequired(IhtFormType.optionNotRequired, 10000L),
                equalTo(new BigDecimal("100.00")));
    }

    @Test
    public void shouldReturnNullifNotOptionNotRequired() {
        assertThat(IhtValuesMapper.getGrossNotRequired(IhtFormType.optionIHT207, 10000L),
                IsNull.nullValue());
    }
}

