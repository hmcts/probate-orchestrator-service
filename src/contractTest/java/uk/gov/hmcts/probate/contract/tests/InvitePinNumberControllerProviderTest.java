package uk.gov.hmcts.probate.contract.tests;

import au.com.dius.pact.provider.junitsupport.Provider;
import au.com.dius.pact.provider.junitsupport.State;
import org.json.JSONException;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import uk.gov.hmcts.probate.client.business.BusinessServiceApi;
import uk.gov.hmcts.probate.core.service.SecurityUtils;
import uk.gov.hmcts.reform.probate.model.PhonePin;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@Provider("probate_orchestrator_service_invite_pinnumber")
public class InvitePinNumberControllerProviderTest extends ControllerProviderTest {

    @MockitoBean
    private BusinessServiceApi businessServiceApi;
    @MockitoBean
    private SecurityUtils securityUtils;


    @State("probate_orchestrator_service returns pin number")
    public void toGetPinNumber() throws IOException, JSONException {

        when(securityUtils.getAuthorisation()).thenReturn("authToken");
        when(securityUtils.getServiceAuthorisation()).thenReturn("someServiceAuthorisation");
        when(businessServiceApi.pinNumberPost(anyString(), any(PhonePin.class)))
                .thenReturn("123457");

    }

}
