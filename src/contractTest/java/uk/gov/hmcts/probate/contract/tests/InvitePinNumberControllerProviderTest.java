//package uk.gov.hmcts.probate.contract.tests;
//
//import au.com.dius.pact.provider.junit.Provider;
//import au.com.dius.pact.provider.junit.State;
//import org.json.JSONException;
//import org.junit.Ignore;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import uk.gov.hmcts.probate.client.business.BusinessServiceApi;
//import uk.gov.hmcts.probate.core.service.SecurityUtils;
//
//import java.io.IOException;
//
//import static org.mockito.ArgumentMatchers.anyString;
//import static org.mockito.Mockito.when;
//
//@Provider("probate_orchestrator_service_invite_pinnumber")
//public class InvitePinNumberControllerProviderTest extends ControllerProviderTest {
//
//    @MockBean
//    private BusinessServiceApi businessServiceApi;
//    @MockBean
//    private SecurityUtils securityUtils;
//
//
//    @State("probate_orchestrator_service returns pin number")
//    public void toGetPinNumber() throws IOException, JSONException {
//
//        when(securityUtils.getAuthorisation()).thenReturn("authToken");
//        when(securityUtils.getServiceAuthorisation()).thenReturn("someServiceAuthorisation");
//        when(businessServiceApi.pinNumber(anyString(), anyString())).thenReturn("123457");
//
//    }
//
//}
