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
import uk.gov.hmcts.reform.probate.model.documents.CheckAnswersSummary;

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

    private CheckAnswersSummary checkAnswersSummary;

    private byte[] pdfExample;


    @Before
    public void setUp() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.UNWRAP_ROOT_VALUE, true);
        String checkAnswersSummaryJson = TestUtils.getJSONFromFile("businessDocuments/validCheckAnswersSummary.json");
        this.checkAnswersSummary = objectMapper.readValue(checkAnswersSummaryJson, CheckAnswersSummary.class);

        Mockito.when(securityUtils.getAuthorisation()).thenReturn(AUTHORIZATION);
        Mockito.when(securityUtils.getServiceAuthorisation()).thenReturn(SERVICE_AUTHORIZATION);

        pdfExample = new byte[10];

        businessService = new BusinessServiceImpl(businessServiceApi, securityUtils);
    }

    @Test
    public void generateCheckAnswersSummaryPdf() {
        when(businessServiceApi.generateCheckAnswersSummaryPdf(AUTHORIZATION, SERVICE_AUTHORIZATION, checkAnswersSummary)).thenReturn(pdfExample);

        businessService.generateCheckAnswersSummaryPdf(checkAnswersSummary);
        verify(securityUtils, times(1)).getAuthorisation();
        verify(securityUtils, times(1)).getServiceAuthorisation();
        verify(businessServiceApi, times(1)).generateCheckAnswersSummaryPdf(AUTHORIZATION, SERVICE_AUTHORIZATION, checkAnswersSummary);
    }
}
