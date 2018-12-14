package uk.gov.hmcts.probate.core.service;

import org.hamcrest.*;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;

@RunWith(MockitoJUnitRunner.class)
public class ValidationServiceImplTest {





    @Test
    public void shouldValidateIntestacy(){

        BigDecimal bd = new BigDecimal("100.01");
        long  result = bd.multiply(new BigDecimal(100)).longValue();
        Assert.assertThat(result, Matchers.equalTo(10001L));
    }
}