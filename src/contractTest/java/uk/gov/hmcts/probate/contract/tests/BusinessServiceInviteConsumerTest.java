package uk.gov.hmcts.probate.contract.tests;

import au.com.dius.pact.core.model.annotations.Pact;
import au.com.dius.pact.core.model.annotations.PactFolder;
import au.com.dius.pact.consumer.dsl.PactDslRootValue;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.core.model.RequestResponsePact;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import uk.gov.hmcts.probate.client.business.BusinessServiceApi;
import uk.gov.hmcts.reform.probate.model.PhonePin;

import java.io.IOException;

import static org.springframework.http.HttpHeaders.CONTENT_TYPE;


@ExtendWith(PactConsumerTestExt.class)
@ExtendWith(SpringExtension.class)
@PactTestFor(providerName = "probate_business_service_invite", port = "8894")
@PactFolder("pacts")
@SpringBootTest({
    // overriding provider address
    "probate_submitservice.ribbon.listOfServers: localhost:8891",
    "submit.service.api.url : localhost:8892",
    "core_case_data.api.url : localhost:8893",
    "business.service.api.url : localhost:8894"
})
public class BusinessServiceInviteConsumerTest {

    @Autowired
    private BusinessServiceApi businessServiceApi;

    @Autowired
    ContractTestUtils contractTestUtils;

    public static final String SOME_SESSION_ID = "someSessionId";

    @BeforeEach
    public void setUpTest() throws InterruptedException {
        Thread.sleep(2000);
    }

    @Pact(provider = "probate_business_service_invite", consumer = "probate_orchestrator_service")
    public RequestResponsePact executeSendInvitation(PactDslWithProvider builder) throws IOException, JSONException {
        // @formatter:off
        return builder
                .given("business service sends invitation")
                .uponReceiving("a request to POST Invitation ")
                .path("/invite")
                .method("POST")
                .headers(CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE, "Session-Id", SOME_SESSION_ID)
                .body(contractTestUtils.createJsonObject("/invite/invitation.json"))
                .willRespondWith()
                .status(200)
                .matchHeader("FormDataContent-Type", "text/plain;charset=UTF-8")
                .body(PactDslRootValue.stringMatcher("^[0-9]*$", "12321321"))
                .toPact();

        // @formatter:on
    }

    @Pact(provider = "probate_business_service_invite", consumer = "probate_orchestrator_service")
    public RequestResponsePact executeResendInvitation(PactDslWithProvider builder) throws IOException,
        JSONException {
        // @formatter:off
        return builder
                .given("business service resends invitation")
                .uponReceiving("a request to POST resend Invitation ")
                .path("/invite/54321")
                .method("POST")
                .headers(CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE, "Session-Id", SOME_SESSION_ID)
                .body(contractTestUtils.createJsonObject("/invite/invitationResend.json"))
                .willRespondWith()
                .status(200)
                .matchHeader("FormDataContent-Type", "text/plain;charset=UTF-8")
                .body(PactDslRootValue.stringMatcher("^[0-9]*$", "54321"))
                .toPact();

        // @formatter:on
    }


    @Pact(provider = "probate_business_service_invite", consumer = "probate_orchestrator_service")
    public RequestResponsePact executePinNumber(PactDslWithProvider builder) throws IOException, JSONException {
        // @formatter:off
        return builder
                .given("business service generates pin number")
                .uponReceiving("a request to GET a pin number ")
                .path("/pin")
                .method("GET")
                .matchQuery("phoneNumber", "07986777788")
                .headers("Session-Id", SOME_SESSION_ID)
                .willRespondWith()
                .status(200)
                .matchHeader("FormDataContent-Type", "text/plain;charset=UTF-8")
                .body(PactDslRootValue.stringMatcher("^[0-9]*$", "54321"))
                .toPact();

        // @formatter:on
    }

    @Test
    @PactTestFor(pactMethod = "executeSendInvitation")
    public void verifyExecuteSendInvitation() throws JSONException, IOException {
        businessServiceApi.invite(contractTestUtils.getInvitation("/invite/invitation.json"), SOME_SESSION_ID);
    }

    @Test
    @PactTestFor(pactMethod = "executeResendInvitation")
    public void verifyExecuteResendInvitation() throws JSONException, IOException {
        businessServiceApi.invite("54321", contractTestUtils.getInvitation("/invite/invitationResend.json"),
            SOME_SESSION_ID);
    }

    @Test
    @PactTestFor(pactMethod = "executePinNumber")
    public void verifyExecutePinNumber() throws JSONException, IOException {
        businessServiceApi.pinNumberPost(SOME_SESSION_ID, new PhonePin("07986777788"));
    }
}



