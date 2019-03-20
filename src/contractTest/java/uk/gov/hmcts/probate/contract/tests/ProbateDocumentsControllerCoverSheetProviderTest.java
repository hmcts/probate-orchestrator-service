package uk.gov.hmcts.probate.contract.tests;

import au.com.dius.pact.provider.junit.Provider;
import au.com.dius.pact.provider.junit.State;
import au.com.dius.pact.provider.junit.target.HttpTarget;
import au.com.dius.pact.provider.junit.target.Target;
import au.com.dius.pact.provider.junit.target.TestTarget;
import org.json.JSONException;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import uk.gov.hmcts.probate.client.BusinessServiceApi;
import uk.gov.hmcts.probate.core.service.SecurityUtils;
import uk.gov.hmcts.reform.probate.model.documents.BulkScanCoverSheet;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, properties = {
        "server.port=8889", "spring.application.name=PACT_TEST"
})
@Provider("probate_orchestrator_service_documents_cover_sheet")
public class ProbateDocumentsControllerCoverSheetProviderTest extends ControllerProviderTest{


    @MockBean
    private BusinessServiceApi businessServiceApi;
    @MockBean
    private SecurityUtils securityUtils;

    @TestTarget
    @SuppressWarnings(value = "VisibilityModifier")
    public final Target target = new HttpTarget("http", "localhost", 8889, "/");


    @State({"probate_orchestrator_service generates cover sheet byte[] with success",
            "probate_orchestrator_service generates cover sheet byte[] with success"})
    public void toReturnCoverSheetWithSuccess() throws IOException, JSONException {

        when(securityUtils.getAuthorisation()).thenReturn("someAuthorisationId");
        when(securityUtils.getServiceAuthorisation()).thenReturn("someServiceAuthorisationId");
        when(businessServiceApi.generateBulkScanCoverSheetPDF( anyString(),anyString(), any(BulkScanCoverSheet.class) )) .thenReturn("".getBytes());

    }

}
