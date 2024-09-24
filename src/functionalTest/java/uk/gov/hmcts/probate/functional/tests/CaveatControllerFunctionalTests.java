package uk.gov.hmcts.probate.functional.tests;

import io.restassured.RestAssured;
import net.serenitybdd.junit5.SerenityJUnit5Extension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import uk.gov.hmcts.probate.functional.IntegrationTestBase;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SerenityJUnit5Extension.class)
public class CaveatControllerFunctionalTests extends IntegrationTestBase {
    private static final String CAVEAT_EXPIRE = "/caveat/expire";
    private static final String EXPIRE_CAVEATS_CALLED = "Perform expire caveats called";

    @Test
    public void expireRaisedCaveatsYesterday() {
        String response = RestAssured.given()
                .relaxedHTTPSValidation()
                .headers(utils.getCitizenHeaders())
                .when()
                .post(CAVEAT_EXPIRE)
                .then()
                .assertThat()
                .statusCode(200)
                .extract().response().getBody().prettyPrint();
        assertEquals(EXPIRE_CAVEATS_CALLED, response);
    }

    @Test
    public void expireRaisedCaveatsWithoutAuthorisation() {
        RestAssured.given()
                .relaxedHTTPSValidation()
                .when()
                .post(CAVEAT_EXPIRE)
                .then()
                .assertThat()
                .statusCode(415);
    }
}
