//package uk.gov.hmcts.probate.contract.tests;
//
//import au.com.dius.pact.provider.junit.Provider;
//import au.com.dius.pact.provider.junit.State;
//import org.json.JSONException;
//import org.junit.Ignore;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import uk.gov.hmcts.probate.client.business.BusinessServiceApi;
//import uk.gov.hmcts.probate.core.service.SecurityUtils;
//import uk.gov.hmcts.reform.probate.model.documents.CheckAnswersSummary;
//
//import java.io.IOException;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyString;
//import static org.mockito.Mockito.when;
//
//
//@Provider("probate_orchestrator_service_documents_check_answers")
//public class ProbateDocumentsControllerCheckAnswersProviderTest extends ControllerProviderTest{
//
//    @MockBean
//    private BusinessServiceApi businessServiceApi;
//    @MockBean
//    private SecurityUtils securityUtils;
//
//    @State({"probate_orchestrator_service generates check answers byte[] with success",
//            "probate_orchestrator_service generates check answers byte[] with success"})
//    public void toReturnCheckAnswersSummaryWithSuccess() throws IOException, JSONException {
//
//        when(securityUtils.getAuthorisation()).thenReturn("someAuthorisationId");
//        when(securityUtils.getServiceAuthorisation()).thenReturn("someServiceAuthorisationId");
//        when(businessServiceApi.generateCheckAnswersSummaryPdf( anyString(),anyString(),
//        any(CheckAnswersSummary.class) )) .thenReturn("".getBytes());
//
//    }
//
//    @State({"probate_orchestrator_service returns with validation errors",
//            "probate_orchestrator_service returns with validation errors"})
//    public void toReturnInvalidCheckAnswersSummaryWithError() throws IOException, JSONException {
//
//    }
//}
