package uk.gov.hmcts.probate.functional.tests;

import io.restassured.RestAssured;
import net.serenitybdd.junit.spring.integration.SpringIntegrationSerenityRunner;
import net.thucydides.core.annotations.Pending;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;


@RunWith(SpringIntegrationSerenityRunner.class)
public class InvitationControllerFunctionalTests extends FormsFunctionalTests {
    private static final String INVITE_URL = "/invite";
    private static final String INVITE_AGREED_URL = "/invite/agreed/";
    private static final String INVITE_AGREED_ALL_URL = "/invite/allAgreed/";
    private static final String INVITE_BILINGUAL_URL = "/invite/bilingual";
    private static final String INVITE_CONTACT_DETAILS_URL = "/invite/contactdetails/";
    private static final String INVITE_PIN_URL = "/invite/pin";
    private static final String INVITE_PIN_BILINGUAL_URL = "/invite/pin/bilingual";
    private static final String INVITE_RESET_AGREED_FLAGS_URL = "/invite/resetAgreed/";
    private static final String FORMDATA_ID_PLACEHOLDER = "XXXXX";
    private static final String INVITE_ID_PLACEHOLDER = "YYYYY";
    private static final String INVALID_FORM_DATA_ID = "1604925395199999";
    private static final String SESSION_ID = "Session-Id";
    private static final String TEST_SESSION_ID = "ses123";
    public static boolean setUp = true;
    //    private static final String INVITE_DATA_URL = "/invite/data/";
//    private static final String DELETE_INVITE_URL = "/invite/delete/";
//    private static final String GET_ALL_INVITES_URL = "/invites/";
    private static String inviteId;
    private static long formdataId;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Before
    public void aInit() throws IOException, JSONException {
        if (setUp) {
            setUpANewCase();
            shouldSaveFormSuccessfully();
            formdataId = geCaseId();
            String expectedResponseJsonStr = utils.getJsonFromFile("GoPForm_partial.json");
            JSONObject expectedJsonObject = new JSONObject(expectedResponseJsonStr);
            JSONObject expectedDeceasedObject = expectedJsonObject.getJSONObject("executors");
            inviteId = expectedDeceasedObject.getString("inviteId");
            setUp = false;
        }
        logger.info("Generate InviteId: {}", inviteId);
        logger.info("Form DataId: {}", formdataId);
    }


    @Test
    public void generateInvitation() {
        String validInvitationJsonStr = utils.getJsonFromFile("validInvitation.json");
        validInvitationJsonStr = validInvitationJsonStr.replace(FORMDATA_ID_PLACEHOLDER, String.valueOf(formdataId));
        RestAssured.given()
                .relaxedHTTPSValidation()
                .headers(utils.getCitizenHeaders())
                .header(SESSION_ID, TEST_SESSION_ID)
                .body(validInvitationJsonStr)
                .when()
                .post(INVITE_URL)
                .then()
                .assertThat()
                .statusCode(200)
                .extract().response().path("invitations[0].inviteId");
    }


    @Test
    public void inviteAgreed() {
        String validInvitationJsonStr = utils.getJsonFromFile("validInvitationWithInviteId.json");
        validInvitationJsonStr = validInvitationJsonStr.replace(FORMDATA_ID_PLACEHOLDER, String.valueOf(formdataId));
        validInvitationJsonStr = validInvitationJsonStr.replace(INVITE_ID_PLACEHOLDER, inviteId);
        RestAssured.given()
                .relaxedHTTPSValidation()
                .headers(utils.getCaseworkerHeaders())
                .header(SESSION_ID, TEST_SESSION_ID)
                .body(validInvitationJsonStr)
                .when()
                .post(INVITE_AGREED_URL + formdataId)
                .then()
                .assertThat()
                .statusCode(200)
                .extract().response().prettyPrint();
    }

    @Test
    public void getInviteAllAgreedForValidFormDataId() {
        String response = RestAssured.given()
                .relaxedHTTPSValidation()
                .headers(utils.getCitizenHeaders())
                .when()
                .get(INVITE_AGREED_ALL_URL + formdataId)
                .then()
                .assertThat()
                .statusCode(200)
                .extract().response().getBody().prettyPrint();
        Assert.assertEquals("false", response);

    }

    @Test
    public void getInviteAllAgreedForInValidFormDataId() {
        RestAssured.given()
                .relaxedHTTPSValidation()
                .headers(utils.getCitizenHeaders())
                .when()
                .get(INVITE_AGREED_ALL_URL + INVALID_FORM_DATA_ID)
                .then()
                .assertThat()
                .statusCode(404);
    }

    @Test
    public void inviteBilingual() {
        String validInvitationJsonStr = utils.getJsonFromFile("validInvitationWithInviteId.json");
        validInvitationJsonStr = validInvitationJsonStr.replace(FORMDATA_ID_PLACEHOLDER, String.valueOf(formdataId));
        validInvitationJsonStr = validInvitationJsonStr.replace(INVITE_ID_PLACEHOLDER, inviteId);
        validInvitationJsonStr = "[" + validInvitationJsonStr + "]";
        RestAssured.given()
                .relaxedHTTPSValidation()
                .headers(utils.getCitizenHeaders())
                .header(SESSION_ID, TEST_SESSION_ID)
                .body(validInvitationJsonStr)
                .when()
                .post(INVITE_BILINGUAL_URL)
                .then()
                .assertThat()
                .statusCode(200)
                .extract().jsonPath().prettify();
    }

    @Test
    public void updateContactDetails() {
        String validInvitationJsonStr = utils.getJsonFromFile("validInvitationWithInviteId.json");
        validInvitationJsonStr = validInvitationJsonStr.replace(FORMDATA_ID_PLACEHOLDER, String.valueOf(formdataId));
        validInvitationJsonStr = validInvitationJsonStr.replace(INVITE_ID_PLACEHOLDER, inviteId);
        String response = RestAssured.given()
                .relaxedHTTPSValidation()
                .headers(utils.getCitizenHeaders())
                .body(validInvitationJsonStr)
                .when()
                .post(INVITE_CONTACT_DETAILS_URL + formdataId)
                .then()
                .assertThat()
                .statusCode(200)
                .extract().response().getBody().prettyPrint();
        Assert.assertEquals(response, inviteId);
    }

    @Test
    public void getInvitePin() throws JSONException {
        String validInvitationJsonStr = utils.getJsonFromFile("validInvitationWithInviteId.json");
        JSONObject validInvitationJsonObject = new JSONObject(validInvitationJsonStr);
        String phoneNumber = validInvitationJsonObject.getString("phoneNumber");
        String response = RestAssured.given()
                .relaxedHTTPSValidation()
                .headers(utils.getCitizenHeaders())
                .header(SESSION_ID, TEST_SESSION_ID)
                .when()
                .queryParam("phoneNumber", phoneNumber)
                .get(INVITE_PIN_URL)
                .then()
                .assertThat()
                .statusCode(200)
                .extract().response().getBody().prettyPrint();
        Assert.assertNotNull(response);
    }

    @Test
    public void getInvitePinBilingual() throws JSONException {
        String validInvitationJsonStr = utils.getJsonFromFile("validInvitationWithInviteId.json");
        JSONObject validInvitationJsonObject = new JSONObject(validInvitationJsonStr);
        String phoneNumber = validInvitationJsonObject.getString("phoneNumber");
        String response = RestAssured.given()
                .relaxedHTTPSValidation()
                .headers(utils.getCitizenHeaders())
                .header(SESSION_ID, TEST_SESSION_ID)
                .when()
                .queryParam("phoneNumber", phoneNumber)
                .get(INVITE_PIN_BILINGUAL_URL)
                .then()
                .assertThat()
                .statusCode(200)
                .extract().response().getBody().prettyPrint();
        Assert.assertNotNull(response);
    }

    @Test
    @Pending
    public void resetAgreedFlags() {
        generateInvitation();
        RestAssured.given()
                .headers(utils.getCitizenHeaders())
                .when()
                .post(INVITE_RESET_AGREED_FLAGS_URL + formdataId)
                .then()
                .assertThat()
                .statusCode(200)
                .extract().body().equals(String.valueOf(formdataId));
    }
}
