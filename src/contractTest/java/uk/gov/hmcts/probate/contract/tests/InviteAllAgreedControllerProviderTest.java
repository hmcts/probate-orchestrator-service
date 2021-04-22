//package uk.gov.hmcts.probate.contract.tests;
//
//import au.com.dius.pact.provider.junit.Provider;
//import au.com.dius.pact.provider.junit.State;
//import org.json.JSONException;
//import org.junit.Ignore;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import uk.gov.hmcts.probate.client.business.BusinessServiceApi;
//import uk.gov.hmcts.probate.client.submit.SubmitServiceApi;
//import uk.gov.hmcts.probate.core.service.SecurityUtils;
//import uk.gov.hmcts.reform.probate.model.ProbateType;
//import uk.gov.hmcts.reform.probate.model.cases.ProbateCaseDetails;
//
//import java.io.IOException;
//
//import static org.mockito.Mockito.when;
//
//@Provider("probate_orchestrator_service_invite_allagreed")
//public class InviteAllAgreedControllerProviderTest extends ControllerProviderTest {
//
//    @MockBean
//    private BusinessServiceApi businessServiceApi;
//    @MockBean
//    private SubmitServiceApi submitServiceApi;
//    @MockBean
//    private SecurityUtils securityUtils;
//
//
//    @State({"probate_orchestrator_service returns executors agreed flags with true",
//            "probate_orchestrator_service returns executors agreed flags with true"})
//    public void toGetAgreedFlagTrueForAllExecutors() throws IOException, JSONException {
//
//        when(securityUtils.getAuthorisation()).thenReturn("authToken");
//        when(securityUtils.getServiceAuthorisation()).thenReturn("someServiceAuthorisation");
//        ProbateCaseDetails probateCaseDetails =
//        getProbateCaseDetails("probate_orchestrator_service_executors_all_agreed_response.json");
//        when(submitServiceApi.getCase("authToken", "someServiceAuthorisation", "123456",
//        ProbateType.PA.getCaseType().name())).thenReturn(probateCaseDetails);
//
//    }
//
//    @State({"probate_orchestrator_service returns executors agreed flags with false",
//            "probate_orchestrator_service returns executors agreed flags with false"})
//    public void toGetAgreedFlagFalseForAllExecutors() throws IOException, JSONException {
//
//        when(securityUtils.getAuthorisation()).thenReturn("authToken");
//        when(securityUtils.getServiceAuthorisation()).thenReturn("someServiceAuthorisation");
//        ProbateCaseDetails probateCaseDetails =
//        getProbateCaseDetails("probate_orchestrator_service_executors_all_agreed_false_response.json");
//        when(submitServiceApi.getCase("authToken", "someServiceAuthorisation",
//        "123457", ProbateType.PA.getCaseType().name())).thenReturn(probateCaseDetails);
//
//    }
//}
