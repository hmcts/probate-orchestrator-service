package uk.gov.hmcts.probate.contract.tests;

import au.com.dius.pact.consumer.Pact;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.model.RequestResponsePact;
import ch.qos.logback.core.net.SyslogOutputStream;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import feign.codec.DecodeException;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.ResourceUtils;
import org.springframework.web.client.RestTemplate;
import uk.gov.hmcts.probate.client.SubmitServiceApi;
import uk.gov.hmcts.reform.probate.model.cases.ProbateCaseDetails;
import uk.gov.hmcts.reform.probate.model.cases.SubmitResult;
import uk.gov.hmcts.reform.probate.model.cases.caveat.CaveatData;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;

import static io.pactfoundation.consumer.dsl.LambdaDsl.newJsonBody;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;


@ExtendWith(PactConsumerTestExt.class)
@ExtendWith(SpringExtension.class)
@PactTestFor(providerName = "probate_submitservice", port = "8890")
@SpringBootTest({
        // overriding provider address
        "probate_submitservice.ribbon.listOfServers: localhost:8890",
        "submit.service.api.url : localhost:8890",
        "core_case_data.api.url : localhost:8890"
})
public class SubmitServiceCaveatsConsumerCaseDetailsTest {

    public static final String SOME_AUTHORIZATION_TOKEN = "someAuthorizationToken";
    public static final String SOME_SERVICE_AUTHORIZATION_TOKEN = "someServiceAuthorizationToken";
    public static final String SOMEEMAILADDRESS_HOST_COM = "jsnow@bbc.co.uk";
    @Autowired
    private SubmitServiceApi submitServiceApi;
    @Autowired
    ObjectMapper objectMapper;

    private String SERVICE_AUTHORIZATION = "ServiceAuthorization";


    @Pact(state = "provider caveat POSTS submission with success", provider = "probate_submitservice_caveat_submissions", consumer = "probate_orchestrator_service")
    public RequestResponsePact executePostSubmissionWithSuccessPact(PactDslWithProvider builder) throws IOException, JSONException {
        // @formatter:off

        return builder
                .given("provider caveat POSTS submission with success")
                .uponReceiving("a request to POST submission")
                .path("/submissions/" + SOMEEMAILADDRESS_HOST_COM)
                .method("POST")
                .headers(AUTHORIZATION, SOME_AUTHORIZATION_TOKEN, SERVICE_AUTHORIZATION, SOME_SERVICE_AUTHORIZATION_TOKEN)
                .matchHeader("Content-Type", "application/json")
                .body(createJsonObject("caveatsCaseData_full.json"))
                .willRespondWith()
                .status(200)
                .matchHeader("Content-Type", "application/json;charset=UTF-8")
                .body(createJsonObject("caveatsCaseData_full_response.json"))
                .toPact();
        // @formatter:on
    }

    @Pact(state = "provider caveat POSTS submission with validation errors", provider = "probate_submitservice_caveat_submissions", consumer = "probate_orchestrator_service")
    public RequestResponsePact executePostSubmissionWithValidationErrors(PactDslWithProvider builder) throws IOException, JSONException {
        // @formatter:off
        return builder
                .given("provider caveat POSTS submission with validation errors")
                .uponReceiving("a request to POST an invalid submission")
                .path("/submissions/" + SOMEEMAILADDRESS_HOST_COM)
                .method("POST")
                .headers(AUTHORIZATION, SOME_AUTHORIZATION_TOKEN, SERVICE_AUTHORIZATION, SOME_SERVICE_AUTHORIZATION_TOKEN)
                .matchHeader("Content-Type", "application/json")
                .body(createJsonObject("caveatsCaseData_invalid.json"))
                .willRespondWith()
                .status(400)
                .matchHeader("Content-Type", "application/json;charset=UTF-8")
                .body(createJsonObject("caveatsCaseData_invalid_response.json"))
                .toPact();
        // @formatter:on
    }

    @Pact(state = "provider caveat POSTS submission with presubmit validation errors", provider = "probate_submitservice_caveat_submissions", consumer = "probate_orchestrator_service")
    public RequestResponsePact executePostSubmissionWithPreSubmitValidationErrorsDsl(PactDslWithProvider builder) throws IOException, JSONException {
        return builder
                .given("provider caveat POSTS submission with presubmit validation errors")
                .uponReceiving("a request to PUT an invalid submission with presubmit errors")
                .path("/submissions/" + SOMEEMAILADDRESS_HOST_COM)
                .method("POST")
                .headers(AUTHORIZATION, SOME_AUTHORIZATION_TOKEN, SERVICE_AUTHORIZATION, SOME_SERVICE_AUTHORIZATION_TOKEN)
                .matchHeader("Content-Type", "application/json")
                .body(createJsonObject("caveatsCaseData_invalid_presubmit.json"))
                .willRespondWith()
                .status(400)
                .matchHeader("Content-Type", "application/json;charset=UTF-8")
                .body(newJsonBody((o) -> {
                    o.stringValue("path", "/submissions/" + SOMEEMAILADDRESS_HOST_COM);
                    o.stringType("timestamp");
                    o.array("errors", (a) -> {
                                a.object((e) -> e.stringValue("field", "caseData.caveatorForenames")
                                        .booleanValue("bindingFailure", false)
                                        .stringValue("code", "Size")
                                        .array("codes", (c) -> {
                                            c.stringValue("Size.probateCaseDetails.caseData.caveatorForenames")
                                                    .stringValue("Size.caseData.caveatorForenames")
                                                    .stringValue("Size.caseData.caveatorForenames")
                                                    .stringValue("Size.java.lang.String")
                                                    .stringValue("Size");
                                        })
                                        .stringValue("defaultMessage", "size must be between 2 and 2147483647")
                                        .stringValue("rejectedValue", ("c")
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
    @PactTestFor(pactMethod = "executePostSubmissionWithPreSubmitValidationErrorsDsl")
    public void verifyExecutePostSubmissionWithValidationErrorsDsl() throws IOException, JSONException {
        assertThrows(FeignException.class, () -> {
            submitServiceApi.submit(SOME_AUTHORIZATION_TOKEN, SOME_SERVICE_AUTHORIZATION_TOKEN, SOMEEMAILADDRESS_HOST_COM, getProbateCaseDetails("caveatsCaseData_invalid_presubmit.json"));
        });

    }

    @Test
    @PactTestFor(pactMethod = "executePostSubmissionWithSuccessPact")
    public void verifyExecutePostSubmissionWithSuccessPact() throws IOException, JSONException {
        SubmitResult submitResult = submitServiceApi.submit(SOME_AUTHORIZATION_TOKEN, SOME_SERVICE_AUTHORIZATION_TOKEN, SOMEEMAILADDRESS_HOST_COM, getProbateCaseDetails("caveatsCaseData_full.json"));
        CaveatData caveatData = (CaveatData)submitResult.getProbateCaseDetails().getCaseData();
        assertThat(caveatData.getCaveatorEmailAddress (), equalTo("caveator@email.com"));
    }

    @Test
    @PactTestFor(pactMethod = "executePostSubmissionWithValidationErrors")
    public void verifyExecutePostSubmissionWithValidationErrors() throws IOException, JSONException {
        assertThrows(FeignException.class, () -> {
            submitServiceApi.submit(SOME_AUTHORIZATION_TOKEN, SOME_SERVICE_AUTHORIZATION_TOKEN, SOMEEMAILADDRESS_HOST_COM, getProbateCaseDetails("caveatsCaseData_invalid.json"));
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
