package uk.gov.hmcts.probate.contract.tests;

import au.com.dius.pact.consumer.Pact;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.model.RequestResponsePact;
import feign.FeignException;
import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import uk.gov.hmcts.probate.client.SubmitServiceApi;
import uk.gov.hmcts.reform.probate.model.cases.CaseType;
import uk.gov.hmcts.reform.probate.model.cases.ProbateCaseDetails;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;
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
public class SubmitServiceCaseDetailsConsumerTest {

    public static final String SOME_AUTHORIZATION_TOKEN = "someAuthorizationToken";
    public static final String SOME_SERVICE_AUTHORIZATION_TOKEN = "someServiceAuthorizationToken";
    public static final String SOMEEMAILADDRESS_HOST_COM = "jsnow@bbc.co.uk";
    public static final String CASE_ID = "12323213323";
    @Autowired
    private SubmitServiceApi submitServiceApi;
    @Autowired
    ContractTestUtils contractTestUtils;

    private String SERVICE_AUTHORIZATION = "ServiceAuthorization";

    @Pact(state = "provider returns casedata with success", provider = "probate_submitservice_cases", consumer = "probate_orchestrator_service")
    public RequestResponsePact executeSuccessGetCaseDataPact(PactDslWithProvider builder) throws IOException, JSONException {
        // @formatter:off

        return builder
                .given("provider returns casedata with success")
                .uponReceiving("a request to GET casedata with success")
                .path("/cases/" + SOMEEMAILADDRESS_HOST_COM)
                .method("GET")
                .matchQuery("caseType", CaseType.GRANT_OF_REPRESENTATION.toString())
                .headers(AUTHORIZATION, SOME_AUTHORIZATION_TOKEN, SERVICE_AUTHORIZATION, SOME_SERVICE_AUTHORIZATION_TOKEN)
                .willRespondWith()
                .status(200)
                .matchHeader("Content-Type", "application/json;charset=UTF-8")
                .body(contractTestUtils.createJsonObject("intestacyGrantOfRepresentation_full.json"))
                .toPact();
        // @formatter:on
    }

    @Pact(state = "provider returns casedata not found", provider = "probate_submitservice_cases", consumer = "probate_orchestrator_service")
    public RequestResponsePact executeNotFoundGetCaseDataPact(PactDslWithProvider builder) {
        // @formatter:off

        return builder
                .given("provider returns casedata not found")
                .uponReceiving("a request to GET casedata not found")
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
                .body(contractTestUtils.createJsonObject("intestacyGrantOfRepresentation_full.json"))
                .willRespondWith()
                .status(200)
                .matchHeader("Content-Type", "application/json;charset=UTF-8")
                .body(contractTestUtils.createJsonObject("intestacyGrantOfRepresentation_full.json"))
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
                .body(contractTestUtils.createJsonObject("intestacyGrantOfRepresentation_partial_draft.json"))
                .willRespondWith()
                .status(200)
                .matchHeader("Content-Type", "application/json;charset=UTF-8")
                .body(contractTestUtils.createJsonObject("intestacyGrantOfRepresentation_partial_draft.json"))
                .toPact();
        // @formatter:on
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

        ProbateCaseDetails caseDetails = submitServiceApi.saveDraft(SOME_AUTHORIZATION_TOKEN, SOME_SERVICE_AUTHORIZATION_TOKEN, SOMEEMAILADDRESS_HOST_COM, contractTestUtils.getProbateCaseDetails("intestacyGrantOfRepresentation_partial_draft.json"));
        assertThat(caseDetails.getCaseInfo().getCaseId(), equalTo(CASE_ID));
    }

    @Test
    @PactTestFor(pactMethod = "executeSuccessPostDraftCaseDataPact")
    public void verifyExecuteSuccessPostDraftCaseDataPact() throws IOException, JSONException {

        ProbateCaseDetails caseDetails = submitServiceApi.saveDraft(SOME_AUTHORIZATION_TOKEN, SOME_SERVICE_AUTHORIZATION_TOKEN, SOMEEMAILADDRESS_HOST_COM, contractTestUtils.getProbateCaseDetails("intestacyGrantOfRepresentation_full.json"));
        assertThat(caseDetails.getCaseInfo().getCaseId(), equalTo(CASE_ID));
    }

    @Test
    @PactTestFor(pactMethod = "executeNotFoundGetCaseDataPact")
    public void verifyExecuteNotFoundGetCaseDataPact() {
        assertThrows(FeignException.class, () -> {
            submitServiceApi.getCase(SOME_AUTHORIZATION_TOKEN, SOME_SERVICE_AUTHORIZATION_TOKEN, SOMEEMAILADDRESS_HOST_COM, CaseType.GRANT_OF_REPRESENTATION.toString());
        });

    }

}
