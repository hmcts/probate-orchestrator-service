package uk.gov.hmcts.probate.contract.tests;

import au.com.dius.pact.provider.junitsupport.Provider;
import au.com.dius.pact.provider.junitsupport.State;
import org.json.JSONException;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import uk.gov.hmcts.probate.client.submit.SubmitServiceApi;
import uk.gov.hmcts.probate.core.service.SecurityUtils;
import uk.gov.hmcts.reform.probate.model.cases.ProbateCaseDetails;

import java.io.IOException;

import static org.mockito.Mockito.when;

@Provider("probate_orchestrator_service_intestacy_forms")
public class IntestacyDraftsFormsControllerProviderTest extends ControllerProviderTest {

    @MockitoBean
    private SubmitServiceApi submitServiceApi;
    @MockitoBean
    private SecurityUtils securityUtils;
    @MockitoBean
    private ProbateCaseDetails probateCaseDetails;


    @State({"probate_orchestrator_service persists intestacy formdata with success",
            "probate_orchestrator_service persists intestacy formdata with success"})
    public void toPersistIntestacyFormDataWithSuccess() throws IOException, JSONException {

        when(securityUtils.getAuthorisation()).thenReturn("someAuthorisationId");
        when(securityUtils.getServiceAuthorisation()).thenReturn("someServiceAuthorisationId");

        ProbateCaseDetails probateCaseDetails = getProbateCaseDetails(
                "probate_orchestrator_service_persists_intestacy_formdata_with_success_probate_case_details.json");
        ProbateCaseDetails probateCaseDetailsResponse = getProbateCaseDetails(
                "probate_orchestrator_service_persists_intestacy_formdata_with_success_probate_case_details_"
                        + "response.json");
        when(submitServiceApi.saveCase("someAuthorisationId", "someServiceAuthorisationId",
                "someemailaddress@host.com", "BOCaseStopped",
                probateCaseDetails)).thenReturn(probateCaseDetailsResponse);

    }


}
