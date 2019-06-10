package uk.gov.hmcts.probate.contract.tests;

import au.com.dius.pact.provider.junit.Provider;
import au.com.dius.pact.provider.junit.State;
import org.json.JSONException;
import org.mockito.ArgumentMatchers;
import org.springframework.boot.test.mock.mockito.MockBean;
import uk.gov.hmcts.probate.client.business.BusinessServiceApi;
import uk.gov.hmcts.probate.client.submit.SubmitServiceApi;
import uk.gov.hmcts.probate.core.service.SecurityUtils;
import uk.gov.hmcts.reform.probate.model.ProbateType;
import uk.gov.hmcts.reform.probate.model.cases.CaseType;
import uk.gov.hmcts.reform.probate.model.cases.ProbateCaseDetails;
import uk.gov.hmcts.reform.probate.model.multiapplicant.Invitation;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@Provider("probate_orchestrator_service_invitelink")
public class InviteLinkControllerProviderTest extends ControllerProviderTest {

    @MockBean
    private BusinessServiceApi businessServiceApi;
    @MockBean
    private SubmitServiceApi submitServiceApi;
    @MockBean
    private SecurityUtils securityUtils;


    @State("probate_orchestrator_service gets an invite")
    public void toGetInvite() throws IOException, JSONException {

        when(securityUtils.getAuthorisation()).thenReturn("authToken");
        when(securityUtils.getServiceAuthorisation()).thenReturn("someServiceAuthorisation");

        ProbateCaseDetails probateCaseDetails = getProbateCaseDetails("probate_orchestrator_service_invite_get_response.json");
        when(submitServiceApi.getCaseByInvitationId("authToken","someServiceAuthorisation","54321", CaseType.GRANT_OF_REPRESENTATION.name())).thenReturn(probateCaseDetails);



    }

    @State("probate_orchestrator_service creates and sends the invite")
    public void toCreateAndSendInvite() throws IOException, JSONException {

        when(securityUtils.getAuthorisation()).thenReturn("authToken");
        when(securityUtils.getServiceAuthorisation()).thenReturn("someServiceAuthorisation");

        when(businessServiceApi.invite(ArgumentMatchers.any(Invitation.class), anyString())).thenReturn("54321");
        ProbateCaseDetails probateCaseDetails = getProbateCaseDetails("probate_orchestrator_service_invite_send_response.json");
        when(submitServiceApi.getCase("authToken", "someServiceAuthorisation", "123456", ProbateType.PA.getCaseType().name())).thenReturn(probateCaseDetails);

    }

    @State("probate_orchestrator_service creates and resends the invite")
    public void toCreateAndResendsInvite() throws IOException, JSONException {

        when(securityUtils.getAuthorisation()).thenReturn("authToken");
        when(securityUtils.getServiceAuthorisation()).thenReturn("someServiceAuthorisation");

        when(businessServiceApi.invite( anyString(),ArgumentMatchers.any(Invitation.class), anyString())).thenReturn("54321");
        ProbateCaseDetails probateCaseDetails = getProbateCaseDetails("probate_orchestrator_service_invite_send_response.json");
        when(submitServiceApi.getCase("authToken", "someServiceAuthorisation", "123456", ProbateType.PA.getCaseType().name())).thenReturn(probateCaseDetails);

    }
}
