package uk.gov.hmcts.probate.contract.tests;

import au.com.dius.pact.provider.junitsupport.Provider;
import au.com.dius.pact.provider.junitsupport.State;
import org.json.JSONException;
import org.springframework.boot.test.mock.mockito.MockBean;
import uk.gov.hmcts.probate.client.business.BusinessServiceApi;
import uk.gov.hmcts.probate.client.submit.SubmitServiceApi;
import uk.gov.hmcts.probate.core.service.SecurityUtils;
import uk.gov.hmcts.reform.probate.model.ProbateType;
import uk.gov.hmcts.reform.probate.model.cases.ProbateCaseDetails;

import java.io.IOException;

import static org.mockito.Mockito.when;

@Provider("probate_orchestrator_service_invitedata")
public class InviteDataControllerProviderTest extends ControllerProviderTest {

    @MockBean
    private BusinessServiceApi businessServiceApi;
    @MockBean
    private SubmitServiceApi submitServiceApi;
    @MockBean
    private SecurityUtils securityUtils;


    @State("probate_orchestrator_service resets agreed flags")
    public void toResetAgreedFlagTrueForAllExecutors() throws IOException, JSONException {

        when(securityUtils.getAuthorisation()).thenReturn("authToken");
        when(securityUtils.getServiceAuthorisation()).thenReturn("someServiceAuthorisation");
        ProbateCaseDetails probateCaseDetails =
                getProbateCaseDetails("probate_orchestrator_service_invite_reset_agreed_response.json");
        when(submitServiceApi.getCase("authToken", "someServiceAuthorisation",
        "123456", ProbateType.PA.getCaseType().name())).thenReturn(probateCaseDetails);

    }

    @State("probate_orchestrator_service sets the agreed flag")
    public void toSetAgreedFlagTrueForInvite() throws IOException, JSONException {
        when(securityUtils.getAuthorisation()).thenReturn("authToken");
        when(securityUtils.getServiceAuthorisation()).thenReturn("someServiceAuthorisation");
        ProbateCaseDetails probateCaseDetails =
                getProbateCaseDetails("probate_orchestrator_service_invite_set_agreed_response.json");
        when(submitServiceApi.getCase("authToken", "someServiceAuthorisation", "123456",
        ProbateType.PA.getCaseType().name())).thenReturn(probateCaseDetails);

    }

    @State("probate_orchestrator_service updates the contact details")
    public void toUpdateContactDetailsForExecutor() throws IOException, JSONException {
        when(securityUtils.getAuthorisation()).thenReturn("authToken");
        when(securityUtils.getServiceAuthorisation()).thenReturn("someServiceAuthorisation");
        ProbateCaseDetails probateCaseDetails =
                getProbateCaseDetails("probate_orchestrator_service_invite_update_contact_details_response.json");
        when(submitServiceApi.getCase("authToken", "someServiceAuthorisation", "123456",
        ProbateType.PA.getCaseType().name())).thenReturn(probateCaseDetails);
    }

    @State("probate_orchestrator_service deletes the invite details")
    public void toDeleteInviteForExecutor() throws IOException, JSONException {
        when(securityUtils.getAuthorisation()).thenReturn("authToken");
        when(securityUtils.getServiceAuthorisation()).thenReturn("someServiceAuthorisation");
        ProbateCaseDetails probateCaseDetails =
                getProbateCaseDetails("probate_orchestrator_service_invite_delete_response.json");
        when(submitServiceApi.getCase("authToken", "someServiceAuthorisation", "123456",
        ProbateType.PA.getCaseType().name())).thenReturn(probateCaseDetails);
    }
}
