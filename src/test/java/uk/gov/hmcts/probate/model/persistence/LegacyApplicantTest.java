package uk.gov.hmcts.probate.model.persistence;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import uk.gov.hmcts.reform.probate.model.Relationship;

import java.io.IOException;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

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

        assertThat(legacyApplicant.getRelationshipToDeceased(), equalTo(Relationship.PARTNER));
    }

    @Test(expected = JsonMappingException.class)
    public void shouldDeserializeLegacyApplicantWithInvalidRelationship() throws IOException {
        String legacyApplicantStr = "{ \"relationshipToDeceased\" : 10000}";

        objectMapper.readValue(legacyApplicantStr, LegacyApplicant.class);
    }
}
