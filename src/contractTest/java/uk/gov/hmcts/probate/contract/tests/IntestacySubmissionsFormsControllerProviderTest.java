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
import uk.gov.hmcts.reform.probate.model.cases.SubmitResult;

import java.io.IOException;
import java.time.LocalDate;

import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, properties = {
        "server.port=8890", "spring.application.name=PACT_TEST"
})
@Provider("probate_orchestrator_service_intestacy_submit")
public class IntestacySubmissionsFormsControllerProviderTest extends ControllerProviderTest {

    private static final LocalDate DATE_OF_BIRTH = LocalDate.of(1930, 01, 01);
    private static final LocalDate DATE_OF_DEATH = LocalDate.of(2018, 01, 01);


    @MockBean
    private SubmitServiceApi submitServiceApi;
    @MockBean
    private SecurityUtils securityUtils;


    @TestTarget
    @SuppressWarnings(value = "VisibilityModifier")
    public final Target target = new HttpTarget("http", "localhost", 8890, "/");

    @State({"probate_orchestrator_service submits intestacy formdata with success",
            "probate_orchestrator_service submits intestacy formdata with success"})
    public void toPersistIntestacyFormDataWithSuccess() throws IOException, JSONException {

        when(securityUtils.getAuthorisation()).thenReturn("someAuthorisationId");
        when(securityUtils.getServiceAuthorisation()).thenReturn("someServiceAuthorisationId");
        ProbateCaseDetails probateCaseDetails = getProbateCaseDetails("intestacyGrantOfRepresentation_submission.json");
        ProbateCaseDetails probateCaseDetailsResponse = getProbateCaseDetails("intestacyGrantOfRepresentation_submission_response.json");
        when(submitServiceApi.submit("someAuthorisationId", "someServiceAuthorisationId", "someemailaddress@host.com", probateCaseDetails)).thenReturn(new SubmitResult(probateCaseDetailsResponse, null));
}


}
