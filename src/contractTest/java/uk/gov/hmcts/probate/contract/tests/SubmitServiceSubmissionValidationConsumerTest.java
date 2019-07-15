package uk.gov.hmcts.probate.contract.tests;

import au.com.dius.pact.consumer.Pact;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.model.RequestResponsePact;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.ResourceUtils;
import uk.gov.hmcts.probate.client.submit.SubmitServiceApi;
import uk.gov.hmcts.reform.probate.model.cases.ProbateCaseDetails;
import uk.gov.hmcts.reform.probate.model.client.ApiClientException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;

import static io.pactfoundation.consumer.dsl.LambdaDsl.newJsonBody;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;


@ExtendWith(PactConsumerTestExt.class)
@ExtendWith(SpringExtension.class)
@PactTestFor(providerName = "probate_submitService", port = "8890")
@SpringBootTest({
        // overriding provider address
        "probate_submitservice.ribbon.listOfServers: localhost:8890",
        "submit.service.api.url : localhost:8890",
        "core_case_data.api.url : localhost:8890"
})
public class SubmitServiceSubmissionValidationConsumerTest {

    public static final String SOME_AUTHORIZATION_TOKEN = "someAuthorizationToken";
    public static final String SOME_SERVICE_AUTHORIZATION_TOKEN = "someServiceAuthorizationToken";
    public static final String SOMEEMAILADDRESS_HOST_COM = "jsnow@bbc.co.uk";
    public static final String CASE_ID = "12323213323";
    @Autowired
    private SubmitServiceApi submitServiceApi;
    @Autowired
    ContractTestUtils contractTestUtils;
    @Autowired
    ObjectMapper objectMapper;

    private String SERVICE_AUTHORIZATION = "ServiceAuthorization";

    @BeforeEach
    public void setUpTest() throws InterruptedException{
        Thread.sleep(2000);
    }


    @Pact(state = "provider POSTS submission with errors",
            provider = "probate_submitService_submissions", consumer = "probate_orchestrator_service")
    public RequestResponsePact ExecutePostSubmissionWithClientErrors(PactDslWithProvider builder) throws IOException, JSONException {
        return builder
                .given("provider POSTS submission with errors")
                .uponReceiving("a request to PUT an invalid submission with client errors")
                .path("/submissions/update/" + SOMEEMAILADDRESS_HOST_COM)
                .method("POST")
                .headers(AUTHORIZATION, SOME_AUTHORIZATION_TOKEN, SERVICE_AUTHORIZATION, SOME_SERVICE_AUTHORIZATION_TOKEN)
                .matchHeader("FormDataContent-Type", "application/json")
                .body(createJsonObject("intestacyGrantOfRepresentation_invalid_PAAPCREATED.json"))
                .willRespondWith()
                .status(400)
                .matchHeader("FormDataContent-Type", "application/json;charset=UTF-8")
                .body(newJsonBody((o) -> {
                    o.stringValue("type", "API_CLIENT");
                    o.object("error", (e) ->
                            e.stringType("exception")
                                    .numberType("status")
                                    .stringType("error")
                                    .stringType("path"));
                }).build())
                .toPact();
    }

    @Test
    @PactTestFor(pactMethod = "ExecutePostSubmissionWithClientErrors")
    public void verifyExecutePostSubmissionWithClientErrors() {
        assertThrows(ApiClientException.class, () -> submitServiceApi.update(
                SOME_AUTHORIZATION_TOKEN,
                SOME_SERVICE_AUTHORIZATION_TOKEN,
                SOMEEMAILADDRESS_HOST_COM,
                getProbateCaseDetails("intestacyGrantOfRepresentation_invalid_PAAPCREATED.json")));
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
