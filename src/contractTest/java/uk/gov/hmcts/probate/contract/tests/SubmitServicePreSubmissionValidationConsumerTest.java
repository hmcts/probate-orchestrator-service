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

import java.io.IOException;

import static io.pactfoundation.consumer.dsl.LambdaDsl.newJsonBody;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
public class SubmitServicePreSubmissionValidationConsumerTest {

    public static final String SOME_AUTHORIZATION_TOKEN = "someAuthorizationToken";
    public static final String SOME_SERVICE_AUTHORIZATION_TOKEN = "someServiceAuthorizationToken";
    public static final String SOMEEMAILADDRESS_HOST_COM = "jsnow@bbc.co.uk";
    public static final String CASE_ID = "12323213323";
    @Autowired
    private SubmitServiceApi submitServiceApi;
    @Autowired
    ContractTestUtils contractTestUtils;

    private String SERVICE_AUTHORIZATION = "ServiceAuthorization";



    @Pact(state = "provider PUTS submission with presubmit validation errors", provider = "probate_submitservice_submissions", consumer = "probate_orchestrator_service")
    public RequestResponsePact executePostSubmissionWithPreSubmitValidationErrorsDsl(PactDslWithProvider builder) throws IOException, JSONException {
        return builder
                .given("provider PUTS submission with presubmit validation errors")
                .uponReceiving("a request to PUT an invalid submission with presubmit errors")
                .path("/submissions/" + SOMEEMAILADDRESS_HOST_COM)
                .method("PUT")
                .headers(AUTHORIZATION, SOME_AUTHORIZATION_TOKEN, SERVICE_AUTHORIZATION, SOME_SERVICE_AUTHORIZATION_TOKEN)
                .matchHeader("Content-Type", "application/json")
                .body(contractTestUtils.createJsonObject("intestacyGrantOfRepresentation_invalid_presubmit.json"))
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
    @PactTestFor(pactMethod = "executePostSubmissionWithPreSubmitValidationErrorsDsl")
    public void verifyExecutePostSubmissionWithValidationErrorsDsl() throws IOException, JSONException {
        assertThrows(FeignException.class, () -> {
            submitServiceApi.update(SOME_AUTHORIZATION_TOKEN, SOME_SERVICE_AUTHORIZATION_TOKEN, SOMEEMAILADDRESS_HOST_COM, contractTestUtils.getProbateCaseDetails("intestacyGrantOfRepresentation_invalid_presubmit.json"));
        });

    }

}
