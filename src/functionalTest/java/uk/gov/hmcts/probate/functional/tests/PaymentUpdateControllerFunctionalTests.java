package uk.gov.hmcts.probate.functional.tests;

import io.restassured.RestAssured;
import net.serenitybdd.junit5.SerenityJUnit5Extension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.http.MediaType;
import uk.gov.hmcts.probate.functional.IntegrationTestBase;

@ExtendWith(SerenityJUnit5Extension.class)
public class PaymentUpdateControllerFunctionalTests extends IntegrationTestBase {
    private static final String PAYMENT_UPDATE_URL = "/payment-updates";

    @Test
    public void updatePaymentDetails() throws Exception {
        String draftJsonStr = utils.getJsonFromFile("payment.json");

        RestAssured.given()
            .relaxedHTTPSValidation()
            .headers("ServiceAuthorization", utils.getPaymentToken())
            .body(draftJsonStr).contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .put(PAYMENT_UPDATE_URL)
            .then()
            .assertThat()
            .statusCode(200);
    }

    @Test
    public void forbiddenAsNoProperHeader() {
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
