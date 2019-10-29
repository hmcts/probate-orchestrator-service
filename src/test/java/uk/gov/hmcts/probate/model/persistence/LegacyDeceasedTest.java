package uk.gov.hmcts.probate.model.persistence;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import uk.gov.hmcts.reform.probate.model.cases.MaritalStatus;

import java.io.IOException;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

public class LegacyDeceasedTest {

    private ObjectMapper objectMapper;

    @Before
    public void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    public void shouldDeserializeLegacyDeceasedWithMaritalStatus() throws IOException {
        String legacyDeceasedStr = "{ \"maritalStatus\" : \"Widowed\"}";

        LegacyDeceased legacyDeceased = objectMapper.readValue(legacyDeceasedStr, LegacyDeceased.class);

        assertThat(legacyDeceased.getMaritalStatus(), equalTo(MaritalStatus.WIDOWED.getDescription()));
    }

}
