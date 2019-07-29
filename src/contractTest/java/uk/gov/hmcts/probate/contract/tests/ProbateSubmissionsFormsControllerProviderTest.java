package uk.gov.hmcts.probate.contract.tests;

import au.com.dius.pact.provider.junit.Provider;
import au.com.dius.pact.provider.junit.State;
import org.json.JSONException;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.boot.test.mock.mockito.MockBean;
import uk.gov.hmcts.probate.client.submit.SubmitServiceApi;
import uk.gov.hmcts.probate.core.service.SecurityUtils;
import uk.gov.hmcts.reform.probate.model.cases.CaseType;
import uk.gov.hmcts.reform.probate.model.cases.ProbateCaseDetails;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@Provider("probate_orchestrator_service_probate_submit")
public class ProbateSubmissionsFormsControllerProviderTest extends ControllerProviderTest {

    @MockBean
    private SubmitServiceApi submitServiceApi;
    @MockBean
    private SecurityUtils securityUtils;

    @State({"probate_orchestrator_service receives an initial payment"})
    public void toInitalisePaymentWithSuccess() throws IOException, JSONException {
        when(securityUtils.getAuthorisation()).thenReturn("authToken");
        when(securityUtils.getServiceAuthorisation()).thenReturn("someServiceAuthorization");

        ProbateCaseDetails probateCaseDetailsResponse = getProbateCaseDetails("prePaymentMadeCaseData.json");
        when(submitServiceApi.getCase("authToken", "someServiceAuthorization", "paymentinitiated.test8@gmail.com", CaseType.GRANT_OF_REPRESENTATION.toString())).thenReturn(probateCaseDetailsResponse);
        when(submitServiceApi.createCase(
                anyString(),
                anyString(),
                anyString(),
                any(ProbateCaseDetails.class))).then(createCaseByIdentifierAnswer);
    }



    @State({"probate_orchestrator_service receives a successful payment"})
    public void toSubmitSuccessfulPayment() throws IOException, JSONException {
        when(securityUtils.getAuthorisation()).thenReturn("authToken");
        when(securityUtils.getServiceAuthorisation()).thenReturn("someServiceAuthorization");

        ProbateCaseDetails probateCaseDetailsResponse = getProbateCaseDetails("postPaymentCaseData.json");
        when(submitServiceApi.getCase("authToken", "someServiceAuthorization", "paymentmade.test8@gmail.com", CaseType.GRANT_OF_REPRESENTATION.toString())).thenReturn(probateCaseDetailsResponse);
        when(submitServiceApi.createCase(
                anyString(),
                anyString(),
                anyString(),
                any(ProbateCaseDetails.class))).then(createCaseByIdentifierAnswer);
    }

/*    @State({"probate_orchestrator_service submits single probate formdata with success",
            "probate_orchestrator_service submits single probate formdata with success"})
    public void toPersistProbateSingleApplicantFormDataWithSuccess() throws IOException, JSONException {

        when(securityUtils.getAuthorisation()).thenReturn("authToken");
        when(securityUtils.getServiceAuthorisation()).thenReturn("someServiceAuthorization");

        ProbateCaseDetails probateCaseDetails = getProbateCaseDetails("probateGrantOfRepresentation_SingleProbateApplicant_submission.json");
        ProbateCaseDetails probateCaseDetailsResponse = getProbateCaseDetails("probateGrantOfRepresentation_SingleProbateApplicant_submission_response.json");

        when(submitServiceApi.getCase("authToken", "someServiceAuthorization", "ccdcasedata1@gmail.com", CaseType.GRANT_OF_REPRESENTATION.toString())).thenReturn(probateCaseDetailsResponse);

        when(submitServiceApi.createCase(
                anyString(),
                anyString(),
                anyString(),
                any(ProbateCaseDetails.class))).then(createCaseByIdentifierAnswer);
    }*/



//    @State({"probate_orchestrator_service submits multiple probate formdata with success",
//            "probate_orchestrator_service submits multiple probate formdata with success"})
//    public void toPersistProbateMultipleApplicantFormDataWithSuccess() throws IOException, JSONException {
//
//        when(securityUtils.getAuthorisation()).thenReturn("authToken");
//        when(securityUtils.getServiceAuthorisation()).thenReturn("someServiceAuthorization");
//        ProbateCaseDetails probateCaseDetails = getProbateCaseDetails("probateGrantOfRepresentation_MultipleProbateApplicant_submission.json");
//        ProbateCaseDetails probateCaseDetailsResponse = getProbateCaseDetails("probateGrantOfRepresentation_MultipleProbateApplicant_submission_response.json");
//
//        when(submitServiceApi.getCase("authToken", "someServiceAuthorization", "maggy.penelope@sellcow.net", CaseType.GRANT_OF_REPRESENTATION.toString())).thenReturn(probateCaseDetailsResponse);
//    }



    private Answer<ProbateCaseDetails> createCaseByIdentifierAnswer = new Answer<ProbateCaseDetails>() {
        @Override
        public ProbateCaseDetails answer(InvocationOnMock invocation) throws Throwable {
            Object[] args = invocation.getArguments();
            String id = ((String) args[2]); // Cast to int for switch.
            if (id.equals("paymentinitiated.test8@gmail.com")) {
                return getProbateCaseDetails("paymentInitiatedCaseData.json");
            } else if (id.equals("paymentmade.test8@gmail.com")) {
                return getProbateCaseDetails("postPaymentCaseData.json");
            }

            return null;
        }
    };
}



