package uk.gov.hmcts.probate.functional.tests;

import io.restassured.RestAssured;
import net.serenitybdd.junit.spring.integration.SpringIntegrationSerenityRunner;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import uk.gov.hmcts.probate.functional.IntegrationTestBase;

@RunWith(SpringIntegrationSerenityRunner.class)
@Ignore
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
        Assert.assertEquals(EXPIRE_CAVEATS_CALLED, response);
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
