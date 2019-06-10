package uk.gov.hmcts.probate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import uk.gov.hmcts.reform.probate.model.documents.BulkScanCoverSheet;
import uk.gov.hmcts.reform.probate.model.documents.CheckAnswersSummary;
import uk.gov.hmcts.reform.probate.model.documents.LegalDeclaration;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@WebMvcTest(value = {DocumentsController.class}, secure = false)
public class DocumentsControllerTest {

    private static final String CHECK_ANSWERS_SUMMARY_ENDPOINT = DocumentsController.DOCUMENTS_BASEURL + DocumentsController.CHECK_ANSWERS_ENDPOINT;
    private static final String LEGAL_DECLARATION_ENDPOINT = DocumentsController.DOCUMENTS_BASEURL + DocumentsController.LEGAL_DECLARATION_ENDPOINT;
    private static final String BULKSCAN_COVERSHEET_ENDPOINT = DocumentsController.DOCUMENTS_BASEURL + DocumentsController.BULK_SCAN_COVERSHEET_ENDPOINT;

    @MockBean
    private BusinessService businessService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    public void generateCheckAnswersSummaryPdf_withValidJson_shouldReturn200() throws Exception {
        when(businessService.generateCheckAnswersSummaryPdf(any(CheckAnswersSummary.class))).thenReturn(any(byte[].class));

        String checkAnswersSummaryJson = TestUtils.getJSONFromFile("businessDocuments/validCheckAnswersSummary.json");

        mockMvc.perform(post(CHECK_ANSWERS_SUMMARY_ENDPOINT)
                .content(checkAnswersSummaryJson)
                .contentType(MediaType.valueOf(MediaType.APPLICATION_JSON_VALUE)))
                .andExpect(status().isOk());
        verify(businessService, times(1)).generateCheckAnswersSummaryPdf(any(CheckAnswersSummary.class));
    }

    @Test
    public void generateCheckAnswersSummaryPdf_withInvalidJson_shouldReturnBadRequest() throws Exception {
        String invalidCheckAnswersSummaryJson = TestUtils.getJSONFromFile("businessDocuments/invalidCheckAnswersSummary.json");

        mockMvc.perform(post(CHECK_ANSWERS_SUMMARY_ENDPOINT)
                .content(invalidCheckAnswersSummaryJson)
                .contentType(MediaType.valueOf(MediaType.APPLICATION_JSON_VALUE)))
                .andExpect(status().isBadRequest());
    }


    @Test
    public void generateLegalDeclarationyPdf_withValidJson_shouldReturn200() throws Exception {
        when(businessService.generateLegalDeclarationPdf(any(LegalDeclaration.class))).thenReturn(any(byte[].class));

        String legalDecJson = TestUtils.getJSONFromFile("businessDocuments/validLegalDeclaration.json");

        mockMvc.perform(post(LEGAL_DECLARATION_ENDPOINT)
                .content(legalDecJson)
                .contentType(MediaType.valueOf(MediaType.APPLICATION_JSON_VALUE)))
                .andExpect(status().isOk());
        verify(businessService, times(1)).generateLegalDeclarationPdf(any(LegalDeclaration.class));
    }

    @Test
    public void generateLegalDeclarationPdf_withInvalidJson_shouldReturnBadRequest() throws Exception {
        String invalidLegalDeclarationJson = TestUtils.getJSONFromFile("businessDocuments/invalidLegalDeclaration.json");

        mockMvc.perform(post(LEGAL_DECLARATION_ENDPOINT)
                .content(invalidLegalDeclarationJson)
                .contentType(MediaType.valueOf(MediaType.APPLICATION_JSON_VALUE)))
                .andExpect(status().isBadRequest());
    }


    @Test
    public void generateBulkScanCoversheetPdf_withValidJson_shouldReturn200() throws Exception {
        when(businessService.generateLegalDeclarationPdf(any(LegalDeclaration.class))).thenReturn(any(byte[].class));

        String bulkScanJosn = TestUtils.getJSONFromFile("businessDocuments/validBulkScanCoverSheet.json");

        mockMvc.perform(post(BULKSCAN_COVERSHEET_ENDPOINT)
                .content(bulkScanJosn)
                .contentType(MediaType.valueOf(MediaType.APPLICATION_JSON_VALUE)))
                .andExpect(status().isOk());
        verify(businessService, times(1)).generateBulkScanCoverSheetPdf(any(BulkScanCoverSheet.class));
    }

    @Test
    public void generateBulkScanCoversheetPdf_withInvalidJson_shouldReturnBadRequest() throws Exception {
        String invalidBulkScanJosn = TestUtils.getJSONFromFile("businessDocuments/invalidBulkScanCoverSheet.json");

        mockMvc.perform(post(BULKSCAN_COVERSHEET_ENDPOINT)
                .content(invalidBulkScanJosn)
                .contentType(MediaType.valueOf(MediaType.APPLICATION_JSON_VALUE)))
                .andExpect(status().isBadRequest());
    }
}
