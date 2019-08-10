//package uk.gov.hmcts.probate.contract.tests;
//
//import au.com.dius.pact.provider.junit.Provider;
//import au.com.dius.pact.provider.junit.State;
//import org.json.JSONException;
//import org.junit.Ignore;
//import org.mockito.invocation.InvocationOnMock;
//import org.mockito.stubbing.Answer;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import uk.gov.hmcts.probate.client.submit.SubmitServiceApi;
//import uk.gov.hmcts.probate.core.service.SecurityUtils;
//import uk.gov.hmcts.reform.probate.model.ProbateType;
//import uk.gov.hmcts.reform.probate.model.cases.ProbateCaseDetails;
//import uk.gov.hmcts.reform.probate.model.cases.grantofrepresentation.GrantOfRepresentationData;
//
//import java.io.IOException;
//import java.time.LocalDate;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyString;
//import static org.mockito.Mockito.when;
//
//
//@Provider("probate_orchestrator_service_probate_forms")
//public class ProbateDraftsFormsControllerProviderTest extends ControllerProviderTest {
//
//    @MockBean
//    private SubmitServiceApi submitServiceApi;
//    @MockBean
//    private SecurityUtils securityUtils;
//
//
//    @State({"probate_orchestrator_service gets formdata with success",
//            "probate_orchestrator_service gets formdata with success"})
//    public void toGetProbateFormDataWithSuccess() throws IOException, JSONException {
//
//        when(securityUtils.getAuthorisation()).thenReturn("authToken");
//        when(securityUtils.getServiceAuthorisation()).thenReturn("someServiceAuthorization");
//        ProbateCaseDetails probateCaseDetailsResponse = getProbateCaseDetails("probate_orchestrator_service_probate_forms_get_with_sucess_response.json");
//        when(submitServiceApi.getCase("authToken", "someServiceAuthorization", "someemailaddress@host.com", ProbateType.PA.getCaseType().name())).thenReturn(probateCaseDetailsResponse);
//
//    }
//
//    @State({"probate_orchestrator_service persists probate formdata with success",
//            "probate_orchestrator_service persists probate formdata with success"})
//    public void toPersistProbateFormDataWithSuccess() throws IOException, JSONException {
//        when(securityUtils.getAuthorisation()).thenReturn("authToken");
//        when(securityUtils.getServiceAuthorisation()).thenReturn("someServiceAuthorization");
//        ProbateCaseDetails probateCaseDetails = getProbateCaseDetails("probate_orchestrator_service_probate_forms_persist_with_success_response.json");
//        GrantOfRepresentationData grantOfRepresentationData = (GrantOfRepresentationData) probateCaseDetails.getCaseData();
//        grantOfRepresentationData.setApplicationSubmittedDate(LocalDate.now());
//        when(submitServiceApi.saveCase(
//                anyString(),
//                anyString(),
//                anyString(),
//                any(ProbateCaseDetails.class))).then(createCaseByIdentifierAnswer);
//
//    }
//
//    private Answer<ProbateCaseDetails> createCaseByIdentifierAnswer = new Answer<ProbateCaseDetails>() {
//        @Override
//        public ProbateCaseDetails answer(InvocationOnMock invocation) throws Throwable {
//            Object[] args = invocation.getArguments();
//            String id = ((String) args[2]); // Cast to int for switch.
//            if (id.equals("someemailaddress@host.com")) {
//                return getProbateCaseDetails("probate_orchestrator_service_probate_forms_persist_with_success_ccdInfo.json");
//            }
//            return null;
//        }
//    };
//}
