package uk.gov.hmcts.probate.contract.tests;

import au.com.dius.pact.provider.junit.Provider;
import au.com.dius.pact.provider.junit.State;
import org.json.JSONException;
import org.springframework.boot.test.mock.mockito.MockBean;
import uk.gov.hmcts.probate.client.submit.SubmitServiceApi;
import uk.gov.hmcts.probate.core.service.SecurityUtils;
import uk.gov.hmcts.reform.probate.model.cases.ProbateCaseDetails;
import uk.gov.hmcts.reform.probate.model.cases.SubmitResult;

import java.io.IOException;

import static org.mockito.Mockito.when;

@Provider("probate_orchestrator_service_probate_submit")
public class ProbateSubmissionsFormsControllerProviderTest extends ControllerProviderTest {

    @MockBean
    private SubmitServiceApi submitServiceApi;
    @MockBean
    private SecurityUtils securityUtils;

    @State({"probate_orchestrator_service submits probate formdata with success",
            "probate_orchestrator_service submits probate formdata with success"})
    public void toPersistProbateFormDataWithSuccess() throws IOException, JSONException {
        when(securityUtils.getAuthorisation()).thenReturn("authToken");
        when(securityUtils.getServiceAuthorisation()).thenReturn("someServiceAuthorization");
        ProbateCaseDetails probateCaseDetails = getProbateCaseDetails("probateGrantOfRepresentation_partial_submission.json");
        ProbateCaseDetails probateCaseDetailsResponse = getProbateCaseDetails("probateGrantOfRepresentation_partial_submission_response.json");
        when(submitServiceApi.update("authToken", "someServiceAuthorization", "payment.test8@gmail.com", probateCaseDetails)).thenReturn(new SubmitResult(probateCaseDetailsResponse, null));
}
    @State({"probate_orchestrator_service submits single probate formdata with success",
            "probate_orchestrator_service submits single probate formdata with success"})
    public void toPersistProbateSingleApplicantFormDataWithSuccess() throws IOException, JSONException {

        when(securityUtils.getAuthorisation()).thenReturn("authToken");
        when(securityUtils.getServiceAuthorisation()).thenReturn("someServiceAuthorization");
        ProbateCaseDetails probateCaseDetails = getProbateCaseDetails("probateGrantOfRepresentation_SingleProbateApplicant_submission.json");
        ProbateCaseDetails probateCaseDetailsResponse = getProbateCaseDetails("probateGrantOfRepresentation_SingleProbateApplicant_submission_response.json");
        when(submitServiceApi.update("authToken", "someServiceAuthorization", "maggy.penelope@sellcow.net", probateCaseDetails)).thenReturn(new SubmitResult(probateCaseDetailsResponse, null));
    }

    @State({"probate_orchestrator_service submits multiple probate formdata with success",
            "probate_orchestrator_service submits multiple probate formdata with success"})
    public void toPersistProbateMultipleApplicantFormDataWithSuccess() throws IOException, JSONException {

        when(securityUtils.getAuthorisation()).thenReturn("authToken");
        when(securityUtils.getServiceAuthorisation()).thenReturn("someServiceAuthorization");
        ProbateCaseDetails probateCaseDetails = getProbateCaseDetails("probateGrantOfRepresentation_MultipleProbateApplicant_submission.json");
        ProbateCaseDetails probateCaseDetailsResponse = getProbateCaseDetails("probateGrantOfRepresentation_MultipleProbateApplicant_submission_response.json");
        when(submitServiceApi.update("authToken", "someServiceAuthorization", "maggy.penelope@sellcow.net", probateCaseDetails)).thenReturn(new SubmitResult(probateCaseDetailsResponse, null));
    }


}
