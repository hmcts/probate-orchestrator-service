package uk.gov.hmcts.probate.functional.tests;

import io.restassured.RestAssured;
import net.serenitybdd.junit5.SerenityJUnit5Extension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import uk.gov.hmcts.probate.functional.IntegrationTestBase;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SerenityJUnit5Extension.class)
public class GrantControllerFunctionalTests extends IntegrationTestBase {
    private static final String GRANT_AWAITING_DOCUMENTS_NOTIFICATION = "/grant/awaiting-documents-notification";
    private static final String GRANT_DELAY_NOTIFICATION = "/grant/delay-notification";
    private static final String GRANT_DELAY_NOTIFICATION_CALLED = "Perform grant delayed notification called";
    private static final String GRANT_AWAITING_DOCUMENTS_NOTIFICATION_CALLED =
        "Perform grant Awaiting Documents notification called";

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
        assertEquals(GRANT_AWAITING_DOCUMENTS_NOTIFICATION_CALLED, response);
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
        assertEquals(GRANT_DELAY_NOTIFICATION_CALLED, response);
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
