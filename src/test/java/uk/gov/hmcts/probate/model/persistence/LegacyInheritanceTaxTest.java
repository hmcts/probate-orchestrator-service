package uk.gov.hmcts.probate.model.persistence;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import uk.gov.hmcts.reform.probate.model.IhtFormType;

import java.io.IOException;
import java.math.BigDecimal;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

public class LegacyInheritanceTaxTest {

    private ObjectMapper objectMapper;

    @Before
    public void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    public void shouldDeserializeJsonWithAmount() throws IOException {
        String legacyInheritanceTaxStr = "{ \"grossValueFieldIHT205\" : \"1,000,000\"}";

        LegacyInheritanceTax legacyInheritanceTax = objectMapper.readValue(legacyInheritanceTaxStr, LegacyInheritanceTax.class);

        assertThat(legacyInheritanceTax.getGrossIht205(), equalTo(new BigDecimal("1000000")));
    }

    @Test(expected = JsonMappingException.class)
    public void shouldNotDeserializeJsonWhenInvalidAmount() throws IOException {
        String legacyInheritanceTaxStr = "{ \"grossValueFieldIHT205\" : 100000}";

        objectMapper.readValue(legacyInheritanceTaxStr, LegacyInheritanceTax.class);
    }

    @Test
    public void shouldDeserializeJsonWithFormType207() throws IOException {
        String legacyInheritanceTaxStr = "{ \"form\" : \"207\"}";

        LegacyInheritanceTax legacyInheritanceTax = objectMapper.readValue(legacyInheritanceTaxStr, LegacyInheritanceTax.class);

        assertThat(legacyInheritanceTax.getForm(), equalTo(IhtFormType.IHT207));
    }

    @Test
    public void shouldDeserializeJsonWithFormTypeIHT207() throws IOException {
        String legacyInheritanceTaxStr = "{ \"form\" : \"IHT207\"}";

        LegacyInheritanceTax legacyInheritanceTax = objectMapper.readValue(legacyInheritanceTaxStr, LegacyInheritanceTax.class);

        assertThat(legacyInheritanceTax.getForm(), equalTo(IhtFormType.IHT207));
    }

    @Test
    public void shouldDeserializeJsonWithFormType205() throws IOException {
        String legacyInheritanceTaxStr = "{ \"form\" : \"205\"}";

        LegacyInheritanceTax legacyInheritanceTax = objectMapper.readValue(legacyInheritanceTaxStr, LegacyInheritanceTax.class);

        assertThat(legacyInheritanceTax.getForm(), equalTo(IhtFormType.IHT205));
    }

    @Test
    public void shouldDeserializeJsonWithFormTypeIHT205() throws IOException {
        String legacyInheritanceTaxStr = "{ \"form\" : \"IHT205\"}";

        LegacyInheritanceTax legacyInheritanceTax = objectMapper.readValue(legacyInheritanceTaxStr, LegacyInheritanceTax.class);

        assertThat(legacyInheritanceTax.getForm(), equalTo(IhtFormType.IHT205));
    }

    @Test
    public void shouldDeserializeJsonWithFormType400421() throws IOException {
        String legacyInheritanceTaxStr = "{ \"form\" : \"400421\"}";

        LegacyInheritanceTax legacyInheritanceTax = objectMapper.readValue(legacyInheritanceTaxStr, LegacyInheritanceTax.class);

        assertThat(legacyInheritanceTax.getForm(), equalTo(IhtFormType.IHT400421));
    }

    @Test
    public void shouldDeserializeJsonWithFormTypeIHT400421() throws IOException {
        String legacyInheritanceTaxStr = "{ \"form\" : \"IHT400421\"}";

        LegacyInheritanceTax legacyInheritanceTax = objectMapper.readValue(legacyInheritanceTaxStr, LegacyInheritanceTax.class);

        assertThat(legacyInheritanceTax.getForm(), equalTo(IhtFormType.IHT400421));
    }

    @Test
    public void shouldDeserializeJsonWithFormType400() throws IOException {
        String legacyInheritanceTaxStr = "{ \"form\" : \"400\"}";

        LegacyInheritanceTax legacyInheritanceTax = objectMapper.readValue(legacyInheritanceTaxStr, LegacyInheritanceTax.class);

        assertThat(legacyInheritanceTax.getForm(), equalTo(IhtFormType.IHT400421));
    }

    @Test(expected = JsonMappingException.class)
    public void shouldNotDeserializeJsonWhenInvalidFormType() throws IOException {
        String legacyInheritanceTaxStr = "{ \"form\" : 100000}";

        objectMapper.readValue(legacyInheritanceTaxStr, LegacyInheritanceTax.class);
    }
}
