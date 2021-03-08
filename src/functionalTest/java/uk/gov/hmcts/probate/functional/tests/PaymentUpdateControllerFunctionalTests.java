package uk.gov.hmcts.probate.functional.tests;

import io.restassured.RestAssured;
import net.serenitybdd.junit.spring.integration.SpringIntegrationSerenityRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import uk.gov.hmcts.probate.functional.IntegrationTestBase;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringIntegrationSerenityRunner.class)
public class PaymentUpdateControllerFunctionalTests extends IntegrationTestBase {
    public static final String SERVICE_AUTHORIZATION_HEADER = "ServiceAuthorization";
    public static final String BEARER_AUTH_TOKEN = "Bearer test.auth.token1";
    private static final String PAYMENT_UPDATE_URL = "/payment-updates";

    @Autowired
    private MockMvc webClient;

    @Test
    public void updatePaymentDetails() throws Exception {
        String draftJsonStr = utils.getJsonFromFile("payment.json");

        webClient.perform(put(PAYMENT_UPDATE_URL)
            .header(SERVICE_AUTHORIZATION_HEADER, BEARER_AUTH_TOKEN)
            .content(draftJsonStr)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().is2xxSuccessful());
    }

    @Test
    public void ForbiddenAsNoProperHeader() {
        String draftJsonStr = utils.getJsonFromFile("payment.json");
        RestAssured.given()
            .relaxedHTTPSValidation()
            .headers(utils.getCitizenHeaders())
            .body(draftJsonStr)
            .when()
            .put(PAYMENT_UPDATE_URL)
            .then()
            .assertThat()
            .statusCode(403);
    }
}
