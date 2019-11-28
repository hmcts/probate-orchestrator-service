package uk.gov.hmcts.probate.core.service.mapper;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import uk.gov.hmcts.probate.TestUtils;
import uk.gov.hmcts.probate.core.service.mapper.migration.LegacyPaMapper;
import uk.gov.hmcts.probate.model.persistence.FormHolder;
import uk.gov.hmcts.probate.model.persistence.LegacyForm;
import uk.gov.hmcts.reform.probate.model.cases.grantofrepresentation.GrantOfRepresentationData;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LegacyPaMapperTest {

    @Autowired
    private LegacyPaMapper mapper;
    private LegacyForm legacyForm;

    private GrantOfRepresentationData grantOfRepresentation;

    @Before
    public void setUp() throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();
        String intestacyFormStr = TestUtils.getJSONFromFile("legacyPaForm.json");
        FormHolder formHolder = objectMapper.readValue(intestacyFormStr, FormHolder.class);
        legacyForm = formHolder.getFormdata();

    }

    @Test
    public void shouldMapLegacyPaFormToGrantOfRepresentation() {
        GrantOfRepresentationData actualGrantOfRepresentation = mapper.toCaseData(legacyForm);
        assertThat(actualGrantOfRepresentation).isNotNull();
    }

}
