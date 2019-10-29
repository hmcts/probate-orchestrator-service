package uk.gov.hmcts.probate.controller;

import com.google.common.collect.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import uk.gov.hmcts.probate.TestUtils;
import uk.gov.hmcts.probate.service.BusinessService;
import uk.gov.hmcts.reform.probate.model.documents.BulkScanCoverSheet;
import uk.gov.hmcts.reform.probate.model.documents.CheckAnswersSummary;
import uk.gov.hmcts.reform.probate.model.documents.LegalDeclaration;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@WebMvcTest(value = {DocumentsController.class}, secure = false)
public class DocumentsControllerTest {

    private static final String CHECK_ANSWERS_SUMMARY_ENDPOINT = DocumentsController.DOCUMENTS_BASEURL + DocumentsController.CHECK_ANSWERS_ENDPOINT;
    private static final String LEGAL_DECLARATION_ENDPOINT = DocumentsController.DOCUMENTS_BASEURL + DocumentsController.LEGAL_DECLARATION_ENDPOINT;
    private static final String BULKSCAN_COVERSHEET_ENDPOINT = DocumentsController.DOCUMENTS_BASEURL + DocumentsController.BULK_SCAN_COVERSHEET_ENDPOINT;
    private static final String DOCUMENT_UPLOAD_ENDPOINT = DocumentsController.DOCUMENTS_BASEURL + DocumentsController.DOCUMENT_UPLOAD_ENDPOINT;
    private static final String DOCUMENT_DELETE_ENDPOINT = DocumentsController.DOCUMENTS_BASEURL + DocumentsController.DOCUMENT_DELETE_ENDPOINT;

    @MockBean
    private BusinessService businessService;

    @Autowired
    private MockMvc mockMvc;

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
//        when(businessService.generateBulkScanCoverSheetPdf(any(BulkScanCoverSheet.class))).thenReturn(any(byte[].class));

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

    @Test
    public void shouldUpload() throws Exception {
        String userId = "USERID1234";
        String authorisation = "AUTHORISATION";

        MockMultipartFile file = new MockMultipartFile("file", "orig", MediaType.IMAGE_PNG_VALUE, "bar".getBytes());

        mockMvc.perform(multipart(DOCUMENT_UPLOAD_ENDPOINT).file(file)
            .header("Authorization", authorisation)
            .header("user-id", userId))
            .andExpect(status().isOk());

        verify(businessService, times(1)).uploadDocument(eq(authorisation), eq(userId), eq(Lists.newArrayList(file)));
    }

    @Test
    public void shouldDelete() throws Exception {
        String userId = "USERID1234";
        String documentId = "DOCID1234";

        mockMvc.perform(delete(DOCUMENT_DELETE_ENDPOINT, documentId)
            .header("user-id", userId))
            .andExpect(status().isOk());

        verify(businessService, times(1)).delete(userId, documentId);
    }
}
