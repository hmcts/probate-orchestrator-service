package uk.gov.hmcts.probate.core.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import uk.gov.hmcts.probate.TestUtils;
import uk.gov.hmcts.probate.client.BusinessServiceApi;
import uk.gov.hmcts.reform.probate.model.documents.BulkScanCoverSheet;
import uk.gov.hmcts.reform.probate.model.documents.CheckAnswersSummary;
import uk.gov.hmcts.reform.probate.model.documents.LegalDeclaration;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BusinessServiceImplTest {

    private static final String SERVICE_AUTHORIZATION = "SERVICEAUTH1234567";
    private static final String AUTHORIZATION = "AUTH1234567";

    @Mock
    private BusinessServiceApi businessServiceApi;

    @Mock
    SecurityUtils securityUtils;

    private BusinessServiceImpl businessService;

    private byte[] pdfExample;

    private ObjectMapper objectMapper;

    @Before
    public void setUp() throws Exception {
        objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.UNWRAP_ROOT_VALUE, true);

        Mockito.when(securityUtils.getAuthorisation()).thenReturn(AUTHORIZATION);
        Mockito.when(securityUtils.getServiceAuthorisation()).thenReturn(SERVICE_AUTHORIZATION);
        pdfExample = new byte[10];
        businessService = new BusinessServiceImpl(businessServiceApi, securityUtils);
    }

    @Test
    public void generateCheckAnswersSummaryPdf() throws Exception {

        String checkAnswersSummaryJson = TestUtils.getJSONFromFile("businessDocuments/validCheckAnswersSummary.json");
        CheckAnswersSummary checkAnswersSummary = objectMapper.readValue(checkAnswersSummaryJson, CheckAnswersSummary.class);

        when(businessServiceApi.generateCheckAnswersSummaryPdf(AUTHORIZATION, SERVICE_AUTHORIZATION, checkAnswersSummary)).thenReturn(pdfExample);

        businessService.generateCheckAnswersSummaryPdf(checkAnswersSummary);
        verify(securityUtils, times(1)).getAuthorisation();
        verify(securityUtils, times(1)).getServiceAuthorisation();
        verify(businessServiceApi, times(1)).generateCheckAnswersSummaryPdf(AUTHORIZATION, SERVICE_AUTHORIZATION, checkAnswersSummary);
    }


    @Test
    public void generateLegalDeclarationPdf() throws Exception {

        String legalDeclarationJson = TestUtils.getJSONFromFile("businessDocuments/validLegalDeclaration.json");
        LegalDeclaration legalDeclaration = objectMapper.readValue(legalDeclarationJson, LegalDeclaration.class);

        when(businessServiceApi.generateLegalDeclarationPDF(AUTHORIZATION, SERVICE_AUTHORIZATION, legalDeclaration)).thenReturn(pdfExample);

        businessService.generateLegalDeclarationPdf(legalDeclaration);
        verify(securityUtils, times(1)).getAuthorisation();
        verify(securityUtils, times(1)).getServiceAuthorisation();
        verify(businessServiceApi, times(1)).generateLegalDeclarationPDF(AUTHORIZATION, SERVICE_AUTHORIZATION, legalDeclaration);
    }


    @Test
    public void generateBulkScanCoversheetPdf() throws Exception {

        String bulkScanCoversheetJson = TestUtils.getJSONFromFile("businessDocuments/validBulkScanCoverSheet.json");
        BulkScanCoverSheet bulkScanCoverSheet = objectMapper.readValue(bulkScanCoversheetJson, BulkScanCoverSheet.class);

        when(businessServiceApi.generateBulkScanCoverSheetPDF(AUTHORIZATION, SERVICE_AUTHORIZATION, bulkScanCoverSheet)).thenReturn(pdfExample);

        businessService.generateBulkScanCoverSheetPdf(bulkScanCoverSheet);
        verify(securityUtils, times(1)).getAuthorisation();
        verify(securityUtils, times(1)).getServiceAuthorisation();
        verify(businessServiceApi, times(1)).generateBulkScanCoverSheetPDF(AUTHORIZATION, SERVICE_AUTHORIZATION, bulkScanCoverSheet);
    }
}
