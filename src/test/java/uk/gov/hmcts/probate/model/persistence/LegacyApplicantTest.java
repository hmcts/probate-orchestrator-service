package uk.gov.hmcts.probate.model.persistence;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

public class LegacyApplicantTest {

    private ObjectMapper objectMapper;

    @Before
    public void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    public void shouldDeserializeLegacyApplicantWithRelationship() throws IOException {
        String legacyApplicantStr = "{ \"relationshipToDeceased\" : \"partner\"}";

        LegacyApplicant legacyApplicant = objectMapper.readValue(legacyApplicantStr, LegacyApplicant.class);

        assertThat(legacyApplicant.getRelationshipToDeceased(), equalTo("partner"));
    }


}
