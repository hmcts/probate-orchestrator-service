package uk.gov.hmcts.probate.contract.tests;

import au.com.dius.pact.consumer.Pact;
import au.com.dius.pact.consumer.dsl.PactDslJsonBody;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.model.RequestResponsePact;
import org.apache.http.client.fluent.Executor;
import org.json.JSONException;
import org.junit.After;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import uk.gov.hmcts.probate.client.IdamClientApi;
import uk.gov.hmcts.probate.model.idam.AuthenticateUserResponse;
import uk.gov.hmcts.probate.model.idam.TokenExchangeResponse;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;


@ExtendWith(PactConsumerTestExt.class)
@ExtendWith(SpringExtension.class)
@PactTestFor(providerName = "sidam", port = "8891")
@SpringBootTest({
        "auth.idam.client.baseUrl : localhost:8891"
})
public class SidamConsumerTest {

    public static final String SOME_AUTHORIZATION_TOKEN = "someAuthorizationToken";
    public static final String CLIENT_ID = "probate";
    public static final String RESPONSE_TYPE = "code";
    public static final String REDIRECT = "http://localhost:3451/oauth2redirect";
    public static final String CODE = "42";
    private static final String GRANT_TYPE = "authorization_code";
    private static final String CLIENT_SECRET = "someClientSecret";
    private static final String ACCESS_TOKEN = "someAccessToken";

    @Autowired
    private IdamClientApi idamClientApi;


    private String SERVICE_AUTHORIZATION = "ServiceAuthorization";

    @BeforeEach
    public void setUpTest() throws InterruptedException {
        Thread.sleep(2000);
    }

    @After
    void teardown() {
        Executor.closeIdleConnections();
    }

    @Pact(state = "Authorised user requests code", provider = "sidam", consumer = "probate_orchestratorService")
    RequestResponsePact authorizeUserByCodePact(PactDslWithProvider builder) {
        // @formatter:off
        return builder
                .given("A valid authorised user")
                .uponReceiving("a request for a code")
                .path("/oauth2/authorize")
                .method("POST")
                .headers(HttpHeaders.AUTHORIZATION, SOME_AUTHORIZATION_TOKEN)
                .matchHeader(CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .matchQuery("client_id", CLIENT_ID)
                .matchQuery("response_type", RESPONSE_TYPE)
                .matchQuery("redirect_uri", REDIRECT)
                .willRespondWith()
                .status(201)
                .body(new PactDslJsonBody()
                        .stringType("code", "42"))
                .toPact();
    }


    @Pact(state = "Authorised user has been issued a code", provider = "sidam", consumer = "probate_orchestratorService")
    RequestResponsePact exchangeTokenPact(PactDslWithProvider builder) {
        // @formatter:off
        return builder
                .given("An authorised user was issued a code")
                .uponReceiving("a request to exchange a token")
                .path("/oauth2/token")
                .method("POST")
                .matchHeader(CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .matchQuery("code", CODE)
                .matchQuery("grant_type", GRANT_TYPE)
                .matchQuery("redirect_uri", REDIRECT)
                .matchQuery("client_id", CLIENT_ID)
                .matchQuery("client_secret", CLIENT_SECRET)
                .willRespondWith()
                .status(201)
                .body(new PactDslJsonBody()
                        .stringType("access_token", ACCESS_TOKEN))
                .toPact();
    }

    @Test
    @PactTestFor(pactMethod = "authorizeUserByCodePact")
    public void verifyAuthorizeUserByCodePact() throws IOException, JSONException {

        AuthenticateUserResponse authenticateUserResponse = idamClientApi.authenticateUser(SOME_AUTHORIZATION_TOKEN,
                RESPONSE_TYPE, CLIENT_ID, REDIRECT);
        assertThat(authenticateUserResponse.getCode(), equalTo(CODE));
    }


    @Test
    @PactTestFor(pactMethod = "exchangeTokenPact")
    public void verifyExchangeTokenPact() throws IOException, JSONException {

        TokenExchangeResponse tokenExchangeResponse = idamClientApi.exchangeCode(CODE, GRANT_TYPE, REDIRECT
                , CLIENT_ID, CLIENT_SECRET);
        assertThat(tokenExchangeResponse.getAccessToken(), equalTo(ACCESS_TOKEN));
    }

}
