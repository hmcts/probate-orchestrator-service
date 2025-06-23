package uk.gov.hmcts.probate.contract.tests;

import au.com.dius.pact.provider.junitsupport.Provider;
import au.com.dius.pact.provider.junitsupport.State;
import org.json.JSONException;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import uk.gov.hmcts.probate.client.submit.SubmitServiceApi;
import uk.gov.hmcts.probate.core.service.SecurityUtils;
import uk.gov.hmcts.reform.probate.model.cases.ProbateCaseDetails;
import uk.gov.hmcts.reform.probate.model.cases.SubmitResult;

import java.io.IOException;

import static org.mockito.Mockito.when;

@Provider("probate_orchestrator_service_intestacy_submit")
public class IntestacySubmissionsFormsControllerProviderTest extends ControllerProviderTest {

    @MockitoBean
    private SubmitServiceApi submitServiceApi;
    @MockitoBean
    private SecurityUtils securityUtils;

    @State({"probate_orchestrator_service submits intestacy formdata with success",
            "probate_orchestrator_service submits intestacy formdata with success"})
    public void toPersistIntestacyFormDataWithSuccess() throws IOException, JSONException {

        when(securityUtils.getAuthorisation()).thenReturn("someAuthorisationId");
        when(securityUtils.getServiceAuthorisation()).thenReturn("someServiceAuthorisationId");
        ProbateCaseDetails probateCaseDetails =
            getProbateCaseDetails("intestacyGrantOfRepresentation_submission.json");
        ProbateCaseDetails probateCaseDetailsResponse =
            getProbateCaseDetails("intestacyGrantOfRepresentation_submission_response.json");
        when(submitServiceApi.update("someAuthorisationId", "someServiceAuthorisationId",
                "someemailaddress@host.com", probateCaseDetails))
            .thenReturn(new SubmitResult(probateCaseDetailsResponse, null));
    }


}
