package uk.gov.hmcts.probate.controller;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import uk.gov.hmcts.probate.TestUtils;
import uk.gov.hmcts.probate.service.BusinessService;
import uk.gov.hmcts.reform.probate.model.documents.CheckAnswersSummary;


@RunWith(SpringRunner.class)
@WebMvcTest(value = {DocumentsController.class}, secure = false)
public class DocumentsControllerTest {

    private static final String DOCUMENTS_ENDPOINT = "/documents";

    @MockBean
    private BusinessService businessService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String checkAnswersSummaryJson;

    private CheckAnswersSummary checkAnswersSummary;

    @Before
    public void setUp() throws Exception {
        this.checkAnswersSummaryJson = TestUtils.getJSONFromFile("businessDocuments/validCheckAnswersSummary.json");
        objectMapper.configure(DeserializationFeature.UNWRAP_ROOT_VALUE, true);
        this.checkAnswersSummary = objectMapper.readValue(checkAnswersSummaryJson, CheckAnswersSummary.class);

    }

    @Test
    public void testMappingJsonResponseToCheckAnswersSummary() throws Exception {
        Assertions.assertThat(checkAnswersSummary.getMainParagraph()).isEqualTo("main paragraph");
    }




}
