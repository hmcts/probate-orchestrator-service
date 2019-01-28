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
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import uk.gov.hmcts.probate.TestUtils;
import uk.gov.hmcts.probate.service.BusinessService;
import uk.gov.hmcts.reform.probate.model.documents.CheckAnswersSummary;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@WebMvcTest(value = {DocumentsController.class}, secure = false)
public class DocumentsControllerTest {

    private static final String CHECK_ANSWERS_SUMMARY_ENDPOINT = "/documents/generate/check-answers-summary";

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

    @Test
    public void shouldGenerateCheckAnswersSummaryPdf() throws Exception {
        when(businessService.generateCheckAnswersSummaryPdf(eq(checkAnswersSummary))).thenReturn(new byte[10]);

        mockMvc.perform(post(CHECK_ANSWERS_SUMMARY_ENDPOINT)
                .content(checkAnswersSummaryJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(checkAnswersSummaryJson, true));
        verify(businessService, times(1)).generateCheckAnswersSummaryPdf(eq(checkAnswersSummary));
    }
}
