package uk.gov.hmcts.probate.contract.tests;

import au.com.dius.pact.provider.junit.Provider;
import au.com.dius.pact.provider.junit.State;
import org.json.JSONException;
import org.springframework.boot.test.mock.mockito.MockBean;
import uk.gov.hmcts.probate.client.SubmitServiceApi;
import uk.gov.hmcts.probate.core.service.SecurityUtils;
import uk.gov.hmcts.reform.probate.model.cases.CaseType;
import uk.gov.hmcts.reform.probate.model.cases.ProbateCaseDetails;

import java.io.IOException;

import static org.mockito.Mockito.when;


@Provider("probate_orchestrator_service_probate_forms")
public class ProbateDraftsFormsControllerProviderTest extends ControllerProviderTest{

    @MockBean
    private SubmitServiceApi submitServiceApi;
    @MockBean
    private SecurityUtils securityUtils;

    @State({"probate_orchestrator_service persists probate formdata with success",
            "probate_orchestrator_service persists probate formdata with success"})
    public void toPersistProbateFormDataWithSuccess() throws IOException, JSONException {

        when(securityUtils.getAuthorisation()).thenReturn("someAuthorisationId");
        when(securityUtils.getServiceAuthorisation()).thenReturn("someServiceAuthorisationId");
        ProbateCaseDetails probateCaseDetails = getProbateCaseDetails("probate_orchestrator_service_persists_intestacy_formdata_with_success_probate_case_details.json");
        ProbateCaseDetails probateCaseDetailsResponse = getProbateCaseDetails("probate_orchestrator_service_persists_intestacy_formdata_with_success_probate_case_details_response.json");
        when(submitServiceApi.saveDraft("someAuthorisationId", "someServiceAuthorisationId", "someemailaddress@host.com", probateCaseDetails)).thenReturn(probateCaseDetailsResponse);

    }

    @State({"probate_orchestrator_service gets formdata with success",
            "probate_orchestrator_service gets formdata with success"})
    public void toGetProbateFormDataWithSuccess() throws IOException, JSONException {

        when(securityUtils.getAuthorisation()).thenReturn("someAuthorisationId");
        when(securityUtils.getServiceAuthorisation()).thenReturn("someServiceAuthorisationId");
        ProbateCaseDetails probateCaseDetails = getProbateCaseDetails("probate_orchestrator_service_persists_intestacy_formdata_with_success_probate_case_details.json");
        ProbateCaseDetails probateCaseDetailsResponse = getProbateCaseDetails("probate_orchestrator_service_persists_intestacy_formdata_with_success_probate_case_details_response.json");
        when(submitServiceApi.getCase("someAuthorisationId", "someServiceAuthorisationId", "someemailaddress@host.com", CaseType.GRANT_OF_REPRESENTATION.getName())).thenReturn(probateCaseDetailsResponse);

    }


}
