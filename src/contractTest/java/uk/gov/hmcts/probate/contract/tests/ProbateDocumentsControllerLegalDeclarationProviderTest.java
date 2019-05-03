package uk.gov.hmcts.probate.contract.tests;

import au.com.dius.pact.provider.junit.Provider;
import au.com.dius.pact.provider.junit.State;
import org.json.JSONException;
import org.springframework.boot.test.mock.mockito.MockBean;
import uk.gov.hmcts.probate.client.BusinessServiceApi;
import uk.gov.hmcts.probate.core.service.SecurityUtils;
import uk.gov.hmcts.reform.probate.model.documents.LegalDeclaration;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;


@Provider("probate_orchestrator_service_documents_legal_declaration")
public class ProbateDocumentsControllerLegalDeclarationProviderTest extends ControllerProviderTest{


    @MockBean
    private BusinessServiceApi businessServiceApi;
    @MockBean
    private SecurityUtils securityUtils;

    @State({"probate_orchestrator_service generates legal declaration byte[] with success",
            "probate_orchestrator_service generates legal declaration byte[] with success"})
    public void toPersistProbateFormDataWithSuccess() throws IOException, JSONException {

        when(securityUtils.getAuthorisation()).thenReturn("someAuthorisationId");
        when(securityUtils.getServiceAuthorisation()).thenReturn("someServiceAuthorisationId");
        when(businessServiceApi.generateLegalDeclarationPDF( anyString(),anyString(), any(LegalDeclaration.class) )) .thenReturn("".getBytes());

    }
    @State({"probate_orchestrator_service generates legal declaration byte[] with validation errors",
            "probate_orchestrator_service generates legal declaration byte[] with validation errors"})
    public void toReturnInvalidLegalDeclarationWithError() throws IOException, JSONException {

    }

}
