package uk.gov.hmcts.probate.contract.tests;

import au.com.dius.pact.provider.junit.Provider;
import au.com.dius.pact.provider.junit.State;
import au.com.dius.pact.provider.junit.target.HttpTarget;
import au.com.dius.pact.provider.junit.target.Target;
import au.com.dius.pact.provider.junit.target.TestTarget;
import org.json.JSONException;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import uk.gov.hmcts.probate.client.SubmitServiceApi;
import uk.gov.hmcts.probate.core.service.SecurityUtils;
import uk.gov.hmcts.reform.probate.model.cases.ProbateCaseDetails;

import java.io.IOException;
import java.time.LocalDate;

import static org.mockito.Mockito.when;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, properties = {
        "server.port=8889", "spring.application.name=PACT_TEST"
})
@Provider("probate_orchestrator_service_probate_forms")
public class ProbateDraftsFormsControllerProviderTest extends ControllerProviderTest{

    private static final LocalDate DATE_OF_BIRTH = LocalDate.of(1930, 01, 01);
    private static final LocalDate DATE_OF_DEATH = LocalDate.of(2018, 01, 01);

    @MockBean
    private SubmitServiceApi submitServiceApi;
    @MockBean
    private SecurityUtils securityUtils;

    @TestTarget
    @SuppressWarnings(value = "VisibilityModifier")
    public final Target target = new HttpTarget("http", "localhost", 8889, "/");


    @State({"probate_orchestrator_service persists probate formdata with success",
            "probate_orchestrator_service persists probate formdata with success"})
    public void toPersistProbateFormDataWithSuccess() throws IOException, JSONException {

        when(securityUtils.getAuthorisation()).thenReturn("someAuthorisationId");
        when(securityUtils.getServiceAuthorisation()).thenReturn("someServiceAuthorisationId");
        ProbateCaseDetails probateCaseDetails = getProbateCaseDetails("probate_orchestrator_service_persists_intestacy_formdata_with_success_probate_case_details.json");
        ProbateCaseDetails probateCaseDetailsResponse = getProbateCaseDetails("probate_orchestrator_service_persists_intestacy_formdata_with_success_probate_case_details_response.json");
        when(submitServiceApi.saveDraft("someAuthorisationId", "someServiceAuthorisationId", "someemailaddress@host.com", probateCaseDetails)).thenReturn(probateCaseDetailsResponse);

    }

}
