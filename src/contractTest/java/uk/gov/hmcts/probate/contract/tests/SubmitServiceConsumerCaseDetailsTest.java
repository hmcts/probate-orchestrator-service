package uk.gov.hmcts.probate.contract.tests;

import au.com.dius.pact.consumer.Pact;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.model.RequestResponsePact;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.ResourceUtils;
import uk.gov.hmcts.probate.client.SubmitServiceApi;
import uk.gov.hmcts.reform.probate.model.cases.CaseType;
import uk.gov.hmcts.reform.probate.model.cases.ProbateCaseDetails;
import uk.gov.hmcts.reform.probate.model.cases.SubmitResult;
import uk.gov.hmcts.reform.probate.model.client.ApiClientException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;

import static io.pactfoundation.consumer.dsl.LambdaDsl.newJsonBody;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;


@ExtendWith(PactConsumerTestExt.class)
@ExtendWith(SpringExtension.class)
@PactTestFor(providerName = "probate_submitservice", port = "8889")
@SpringBootTest({
        // overriding provider address
        "probate_submitservice.ribbon.listOfServers: localhost:8889",
        "submit.service.api.url : localhost:8889",
        "core_case_data.api.url : localhost:8889"
})
public class SubmitServiceConsumerCaseDetailsTest {

    public static final String SOME_AUTHORIZATION_TOKEN = "someAuthorizationToken";
    public static final String SOME_SERVICE_AUTHORIZATION_TOKEN = "someServiceAuthorizationToken";
    public static final String SOMEEMAILADDRESS_HOST_COM = "jsnow@bbc.co.uk";
    public static final String CASE_ID = "12323213323";
    @Autowired
    private SubmitServiceApi submitServiceApi;
    @Autowired
    ObjectMapper objectMapper;

    private String SERVICE_AUTHORIZATION = "ServiceAuthorization";

    @Pact(state = "provider returns casedata with success", provider = "probate_submitservice_cases", consumer = "probate_orchestrator_service")
    public RequestResponsePact executeSuccessGetCaseDataPact(PactDslWithProvider builder) throws IOException, JSONException {
        // @formatter:off

        return builder
                .given("provider returns casedata with success")
                .uponReceiving("a request to GET casedata")
                .path("/cases/" + SOMEEMAILADDRESS_HOST_COM)
                .method("GET")
                .matchQuery("caseType", CaseType.GRANT_OF_REPRESENTATION.toString())
                .headers(AUTHORIZATION, SOME_AUTHORIZATION_TOKEN, SERVICE_AUTHORIZATION, SOME_SERVICE_AUTHORIZATION_TOKEN)
                .willRespondWith()
                .status(200)
                .matchHeader("Content-Type", "application/json;charset=UTF-8")
                .body(createJsonObject("intestacyGrantOfRepresentation_full.json"))
                .toPact();
        // @formatter:on
    }

    @Pact(state = "provider returns casedata not found", provider = "probate_submitservice_cases", consumer = "probate_orchestrator_service")
    public RequestResponsePact executeNotFoundGetCaseDataPact(PactDslWithProvider builder) {
        // @formatter:off

        return builder
                .given("provider returns casedata not found")
                .uponReceiving("a request to GET casedata")
                .path("/cases/" + SOMEEMAILADDRESS_HOST_COM)
                .method("GET")
                .matchQuery("caseType", CaseType.GRANT_OF_REPRESENTATION.toString())
                .headers(AUTHORIZATION, SOME_AUTHORIZATION_TOKEN, SERVICE_AUTHORIZATION, SOME_SERVICE_AUTHORIZATION_TOKEN)
                .willRespondWith()
                .status(400)
                .toPact();
        // @formatter:on
    }

    @Pact(state = "provider POSTS draft casedata with success", provider = "probate_submitservice_drafts", consumer = "probate_orchestrator_service")
    public RequestResponsePact executeSuccessPostDraftCaseDataPact(PactDslWithProvider builder) throws IOException, JSONException {
        // @formatter:off

        return builder
                .given("provider POSTS draft casedata with success")
                .uponReceiving("a request to POST draft casedata")
                .path("/drafts/" + SOMEEMAILADDRESS_HOST_COM)
                .method("POST")
                .headers(AUTHORIZATION, SOME_AUTHORIZATION_TOKEN, SERVICE_AUTHORIZATION, SOME_SERVICE_AUTHORIZATION_TOKEN)
                .matchHeader("Content-Type", "application/json")
                .body(createJsonObject("intestacyGrantOfRepresentation_full.json"))
                .willRespondWith()
                .status(200)
                .matchHeader("Content-Type", "application/json;charset=UTF-8")
                .body(createJsonObject("intestacyGrantOfRepresentation_full.json"))
                .toPact();
        // @formatter:on
    }


    @Pact(state = "provider POSTS partial draft casedata with success", provider = "probate_submitservice_drafts", consumer = "probate_orchestrator_service")
    public RequestResponsePact executeSuccessPostPartialDraftCaseDataPact(PactDslWithProvider builder) throws IOException, JSONException {
        // @formatter:off

        return builder
                .given("provider POSTS partial draft casedata with success")
                .uponReceiving("a request to POST partial draft casedata")
                .path("/drafts/" + SOMEEMAILADDRESS_HOST_COM)
                .method("POST")
                .headers(AUTHORIZATION, SOME_AUTHORIZATION_TOKEN, SERVICE_AUTHORIZATION, SOME_SERVICE_AUTHORIZATION_TOKEN)
                .matchHeader("Content-Type", "application/json")
                .body(createJsonObject("intestacyGrantOfRepresentation_partial_draft.json"))
                .willRespondWith()
                .status(200)
                .matchHeader("Content-Type", "application/json;charset=UTF-8")
                .body(createJsonObject("intestacyGrantOfRepresentation_partial_draft.json"))
                .toPact();
        // @formatter:on
    }

    @Pact(state = "provider POSTS submission with success", provider = "probate_submitservice_submissions", consumer = "probate_orchestrator_service")
    public RequestResponsePact executePostSubmissionWithSuccessPact(PactDslWithProvider builder) throws IOException, JSONException {
        // @formatter:off

        return builder
                .given("provider POSTS submission with success")
                .uponReceiving("a request to POST submission")
                .path("/submissions/update/" + SOMEEMAILADDRESS_HOST_COM)
                .method("POST")
                .headers(AUTHORIZATION, SOME_AUTHORIZATION_TOKEN, SERVICE_AUTHORIZATION, SOME_SERVICE_AUTHORIZATION_TOKEN)
                .matchHeader("Content-Type", "application/json")
                .body(createJsonObject("intestacyGrantOfRepresentation_full.json"))
                .willRespondWith()
                .status(200)
                .matchHeader("Content-Type", "application/json;charset=UTF-8")
                .body(createJsonObject("intestacyGrantOfRepresentation_full_response.json"))
                .toPact();
        // @formatter:on
    }

    @Pact(state = "provider POSTS submission with validation errors", provider = "probate_submitservice_submissions", consumer = "probate_orchestrator_service")
    public RequestResponsePact executePostSubmissionWithValidationErrors(PactDslWithProvider builder) throws IOException, JSONException {
        // @formatter:off
        return builder
                .given("provider POSTS submission with validation errors")
                .uponReceiving("a request to POST an invalid submission")
                .path("/submissions/update/" + SOMEEMAILADDRESS_HOST_COM)
                .method("POST")
                .headers(AUTHORIZATION, SOME_AUTHORIZATION_TOKEN, SERVICE_AUTHORIZATION, SOME_SERVICE_AUTHORIZATION_TOKEN)
                .matchHeader("Content-Type", "application/json")
                .body(createJsonObject("intestacyGrantOfRepresentation_invalid.json"))
                .willRespondWith()
                .status(400)
                .matchHeader("Content-Type", "application/json;charset=UTF-8")
                .body(createJsonObject("intestacyGrantOfRepresentation_invalid_response.json"))
                .toPact();
        // @formatter:on
    }


    @Pact(state = "provider POSTS submission with presubmit validation errors", provider = "probate_submitservice_submissions", consumer = "probate_orchestrator_service")
    public RequestResponsePact executePostSubmissionWithPreSubmitValidationErrorsDsl(PactDslWithProvider builder) throws IOException, JSONException {
        return builder
                .given("provider POSTS submission with presubmit validation errors")
                .uponReceiving("a request to PUT an invalid submission with presubmit errors")
                .path("/submissions/update/" + SOMEEMAILADDRESS_HOST_COM)
                .method("POST")
                .headers(AUTHORIZATION, SOME_AUTHORIZATION_TOKEN, SERVICE_AUTHORIZATION, SOME_SERVICE_AUTHORIZATION_TOKEN)
                .matchHeader("Content-Type", "application/json")
                .body(createJsonObject("intestacyGrantOfRepresentation_invalid_presubmit.json"))
                .willRespondWith()
                .status(400)
                .matchHeader("Content-Type", "application/json;charset=UTF-8")
                .body(newJsonBody((o) -> {
                    o.stringValue("path", "/submissions/update/" + SOMEEMAILADDRESS_HOST_COM);
                    o.stringType("timestamp");
                    o.array("errors", (a) -> {
                                a.object((e) -> e.stringValue("field", "caseData.deceasedForenames")
                                        .booleanValue("bindingFailure", false)
                                        .stringValue("code", "Size")
                                        .array("codes", (c) -> {
                                            c.stringValue("Size.probateCaseDetails.caseData.deceasedForenames")
                                                    .stringValue("Size.caseData.deceasedForenames")
                                                    .stringValue("Size.deceasedForenames")
                                                    .stringValue("Size.java.lang.String")
                                                    .stringValue("Size");
                                        })
                                        .stringValue("defaultMessage", "size must be between 2 and 2147483647")
                                        .stringValue("rejectedValue", ("N")
                                        ));
                            }

                    );
                    o.stringValue("error", "Bad Request");
                    o.numberValue("status", 400);
                    o.stringValue("message", "Validation failed for object='probateCaseDetails'. Error count: 1");
                }).build())
                .toPact();

    }


    @Test
    @PactTestFor(pactMethod = "executeSuccessGetCaseDataPact")
    public void verifyExecuteSuccessGetCaseDataPact() {

        ProbateCaseDetails caseDetails = submitServiceApi.getCase(SOME_AUTHORIZATION_TOKEN, SOME_SERVICE_AUTHORIZATION_TOKEN, SOMEEMAILADDRESS_HOST_COM, CaseType.GRANT_OF_REPRESENTATION.toString());
        assertThat(caseDetails.getCaseInfo().getCaseId(), equalTo(CASE_ID));
    }

    @Test
    @PactTestFor(pactMethod = "executeSuccessPostPartialDraftCaseDataPact")
    public void verifyExecuteSuccessPostPartialDraftCaseDataPact() throws IOException, JSONException {

        ProbateCaseDetails caseDetails = submitServiceApi.saveDraft(SOME_AUTHORIZATION_TOKEN, SOME_SERVICE_AUTHORIZATION_TOKEN, SOMEEMAILADDRESS_HOST_COM, getProbateCaseDetails("intestacyGrantOfRepresentation_partial_draft.json"));
        assertThat(caseDetails.getCaseInfo().getCaseId(), equalTo(CASE_ID));
    }

    @Test
    @PactTestFor(pactMethod = "executeSuccessPostDraftCaseDataPact")
    public void verifyExecuteSuccessPostDraftCaseDataPact() throws IOException, JSONException {

        ProbateCaseDetails caseDetails = submitServiceApi.saveDraft(SOME_AUTHORIZATION_TOKEN, SOME_SERVICE_AUTHORIZATION_TOKEN, SOMEEMAILADDRESS_HOST_COM, getProbateCaseDetails("intestacyGrantOfRepresentation_full.json"));
        assertThat(caseDetails.getCaseInfo().getCaseId(), equalTo(CASE_ID));
    }

    @Test
    @PactTestFor(pactMethod = "executeNotFoundGetCaseDataPact")
    public void verifyExecuteNotFoundGetCaseDataPact() {
        assertThrows(ApiClientException.class, () -> {
            submitServiceApi.getCase(SOME_AUTHORIZATION_TOKEN, SOME_SERVICE_AUTHORIZATION_TOKEN, SOMEEMAILADDRESS_HOST_COM, CaseType.GRANT_OF_REPRESENTATION.toString());
        });

    }

    @Test
    @PactTestFor(pactMethod = "executePostSubmissionWithSuccessPact")
    public void verifyExecutePostSubmissionWithSuccessPact() throws IOException, JSONException {

        SubmitResult submitResult = submitServiceApi.update(SOME_AUTHORIZATION_TOKEN, SOME_SERVICE_AUTHORIZATION_TOKEN, SOMEEMAILADDRESS_HOST_COM, getProbateCaseDetails("intestacyGrantOfRepresentation_full.json"));
        assertThat(submitResult.getProbateCaseDetails().getCaseInfo().getCaseId(), equalTo(CASE_ID));
    }


    @Test
    @PactTestFor(pactMethod = "executePostSubmissionWithPreSubmitValidationErrorsDsl")
    public void verifyExecutePostSubmissionWithValidationErrorsDsl() throws IOException, JSONException {
        assertThrows(ApiClientException.class, () -> {
            submitServiceApi.update(SOME_AUTHORIZATION_TOKEN, SOME_SERVICE_AUTHORIZATION_TOKEN, SOMEEMAILADDRESS_HOST_COM, getProbateCaseDetails("intestacyGrantOfRepresentation_invalid_presubmit.json"));
        });

    }

    @Test
    @PactTestFor(pactMethod = "executePostSubmissionWithValidationErrors")
    public void verifyExecutePostSubmissionWithValidationErrors() throws IOException, JSONException {
        assertThrows(ApiClientException.class, () -> {
            submitServiceApi.update(SOME_AUTHORIZATION_TOKEN, SOME_SERVICE_AUTHORIZATION_TOKEN, SOMEEMAILADDRESS_HOST_COM, getProbateCaseDetails("intestacyGrantOfRepresentation_invalid.json"));
        });

    }


    private JSONObject createJsonObject(String fileName) throws JSONException, IOException {
        File file = getFile(fileName);
        String jsonString = new String(Files.readAllBytes(file.toPath()));
        return new JSONObject(jsonString);
    }

    private ProbateCaseDetails getProbateCaseDetails(String fileName) throws JSONException, IOException {
        File file = getFile(fileName);
        ProbateCaseDetails probateCaseDetails = objectMapper.readValue(file, ProbateCaseDetails.class);
        return probateCaseDetails;
    }

    private File getFile(String fileName) throws FileNotFoundException {
        return ResourceUtils.getFile(this.getClass().getResource("/json/" + fileName));
    }
}
