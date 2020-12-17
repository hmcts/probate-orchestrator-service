package uk.gov.hmcts.probate.functional.tests;

import cucumber.api.Pending;
import io.restassured.RestAssured;
import net.serenitybdd.junit.spring.integration.SpringIntegrationSerenityRunner;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import uk.gov.hmcts.probate.functional.IntegrationTestBase;

@Pending
@RunWith(SpringIntegrationSerenityRunner.class)
public class GrantControllerFunctionalTests extends IntegrationTestBase {
    private static final String GRANT_AWAITING_DOCUMENTS_NOTIFICATION = "/grant/awaiting-documents-notification";
    private static final String GRANT_DELAY_NOTIFICATION = "/grant/delay-notification";
    private static final String GRANT_DELAY_NOTIFICATION_CALLED = "Perform grant delayed notification called";
    private static final String GRANT_AWAITING_DOCUMENTS_NOTIFICATION_CALLED = "Perform grant Awaiting Documents notification called";

    @Test
    public void notifyGrantsAwaitingDocuments() {
        String response = RestAssured.given()
                .relaxedHTTPSValidation()
                .headers(utils.getCitizenHeaders())
                .when()
                .post(GRANT_AWAITING_DOCUMENTS_NOTIFICATION)
                .then()
                .assertThat()
                .statusCode(200)
                .extract().response().getBody().prettyPrint();
        Assert.assertEquals(GRANT_AWAITING_DOCUMENTS_NOTIFICATION_CALLED, response);
    }

    @Test
    public void notifyGrantsDelayed() {
        String response = RestAssured.given()
                .relaxedHTTPSValidation()
                .headers(utils.getCitizenHeaders())
                .when()
                .post(GRANT_DELAY_NOTIFICATION)
                .then()
                .assertThat()
                .statusCode(200)
                .extract().response().getBody().prettyPrint();
        Assert.assertEquals(GRANT_DELAY_NOTIFICATION_CALLED, response);
    }

    @Test
    public void notifyGrantsDelayedUnAuthorised() {
        RestAssured.given()
                .relaxedHTTPSValidation()
                .when()
                .post(GRANT_DELAY_NOTIFICATION)
                .then()
                .assertThat()
                .statusCode(415);
    }

    @Test
    public void notifyGrantsAwaitingDocumentsUnauthorised() {
        RestAssured.given()
                .relaxedHTTPSValidation()
                .when()
                .post(GRANT_AWAITING_DOCUMENTS_NOTIFICATION)
                .then()
                .assertThat()
                .statusCode(415);
    }

}
