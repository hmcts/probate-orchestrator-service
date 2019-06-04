package uk.gov.hmcts.probate.contract.tests;

import au.com.dius.pact.consumer.Pact;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.model.RequestResponsePact;
import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import uk.gov.hmcts.reform.probate.model.cases.SubmitResult;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;


@ExtendWith(PactConsumerTestExt.class)
@ExtendWith(SpringExtension.class)
@PactTestFor(providerName = "probate_submitservice", port = "8891")
@SpringBootTest({
        // overriding provider address
        "probate_submitservice.ribbon.listOfServers: localhost:8891",
        "submit.service.api.url : localhost:8891",
        "core_case_data.api.url : localhost:8891"
})
public class SubmitServiceSubmissionConsumerTest {

    public static final String SOME_AUTHORIZATION_TOKEN = "someAuthorizationToken";
    public static final String SOME_SERVICE_AUTHORIZATION_TOKEN = "someServiceAuthorizationToken";
    public static final String SOMEEMAILADDRESS_HOST_COM = "jsnow@bbc.co.uk";
    public static final String CASE_ID = "12323213323";
    @Autowired
    private SubmitServiceApi submitServiceApi;
    @Autowired
    ContractTestUtils contractTestUtils;

    private String SERVICE_AUTHORIZATION = "ServiceAuthorization";


    @Pact(state = "provider PUTS submission with success", provider = "probate_submitservice_submissions", consumer = "probate_orchestrator_service")
    public RequestResponsePact executePostSubmissionWithSuccessPact(PactDslWithProvider builder) throws IOException, JSONException {
        // @formatter:off

        return builder
                .given("provider POSTS submission with success")
                .uponReceiving("a request to POST submission")
                .path("/submissions/" + SOMEEMAILADDRESS_HOST_COM)
                .method("POST")
                .headers(AUTHORIZATION, SOME_AUTHORIZATION_TOKEN, SERVICE_AUTHORIZATION, SOME_SERVICE_AUTHORIZATION_TOKEN)
                .matchHeader("Content-Type", "application/json")
                .body(contractTestUtils.createJsonObject("intestacyGrantOfRepresentation_full.json"))
                .willRespondWith()
                .status(200)
                .matchHeader("Content-Type", "application/json;charset=UTF-8")
                .body(contractTestUtils.createJsonObject("intestacyGrantOfRepresentation_full_response.json"))
                .toPact();
        // @formatter:on
    }


    @Test
    @PactTestFor(pactMethod = "executePostSubmissionWithSuccessPact")
    public void verifyExecutePostSubmissionWithSuccessPact() throws IOException, JSONException {

        SubmitResult submitResult = submitServiceApi.update(SOME_AUTHORIZATION_TOKEN, SOME_SERVICE_AUTHORIZATION_TOKEN, SOMEEMAILADDRESS_HOST_COM, contractTestUtils.getProbateCaseDetails("intestacyGrantOfRepresentation_full.json"));
        assertThat(submitResult.getProbateCaseDetails().getCaseInfo().getCaseId(), equalTo(CASE_ID));
    }

}
