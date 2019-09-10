package uk.gov.hmcts.probate.core.service.mapper;

import org.hamcrest.core.*;
import org.junit.Assert;
import org.junit.Test;
import uk.gov.hmcts.reform.probate.model.IhtFormType;

import java.math.BigDecimal;

import static org.hamcrest.CoreMatchers.*;

public class IhtValuesMapperTest {

    @Test
    public void shouldMapIHT205Value() {
        Assert.assertThat(IhtValuesMapper.getGrossIht205(IhtFormType.IHT205, 10000L), equalTo(new BigDecimal("100.00")));
    }

    @Test
    public void shouldReturnNullifNotIHT205() {
        Assert.assertThat(IhtValuesMapper.getGrossIht205(IhtFormType.IHT207, 10000L), IsNull.nullValue());
    }

    @Test
    public void shouldMapIHT207Value() {
        Assert.assertThat(IhtValuesMapper.getGrossIht207(IhtFormType.IHT207, 10000L), equalTo(new BigDecimal("100.00")));
    }

    @Test
    public void shouldReturnNullifNotIHT207() {
        Assert.assertThat(IhtValuesMapper.getGrossIht207(IhtFormType.IHT400421, 10000L), IsNull.nullValue());
    }

    @Test
    public void shouldMapIHT400421Value() {
        Assert.assertThat(IhtValuesMapper.getGrossIht400421(IhtFormType.IHT400421, 10000L), equalTo(new BigDecimal("100.00")));
    }

    @Test
    public void shouldReturnNullifNotIHT400421() {
        Assert.assertThat(IhtValuesMapper.getGrossIht400421(IhtFormType.IHT207, 10000L), IsNull.nullValue());
    }

    @Test
    public void shouldMapNetIHT205Value() {
        Assert.assertThat(IhtValuesMapper.getNetIht205(IhtFormType.IHT205, 10000L), equalTo(new BigDecimal("100.00")));
    }

    @Test
    public void shouldReturnNullifNotNetIHT205() {
        Assert.assertThat(IhtValuesMapper.getNetIht205(IhtFormType.IHT207, 10000L), IsNull.nullValue());
    }

    @Test
    public void shouldMapIHTNet207Value() {
        Assert.assertThat(IhtValuesMapper.getNetIht207(IhtFormType.IHT207, 10000L), equalTo(new BigDecimal("100.00")));
    }

    @Test
    public void shouldReturnNullifNotNetIHT207() {
        Assert.assertThat(IhtValuesMapper.getNetIht207(IhtFormType.IHT400421, 10000L), IsNull.nullValue());
    }

    @Test
    public void shouldMapIHTNet400421Value() {
        Assert.assertThat(IhtValuesMapper.getNetIht400421(IhtFormType.IHT400421, 10000L), equalTo(new BigDecimal("100.00")));
    }

    @Test
    public void shouldReturnNullifNotNetIHT400421() {
        Assert.assertThat(IhtValuesMapper.getNetIht400421(IhtFormType.IHT207, 10000L), IsNull.nullValue());
    }
}