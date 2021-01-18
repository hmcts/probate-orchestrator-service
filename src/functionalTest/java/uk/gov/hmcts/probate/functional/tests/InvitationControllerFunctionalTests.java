package uk.gov.hmcts.probate.functional.tests;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import net.serenitybdd.junit.spring.integration.SpringIntegrationSerenityRunner;
import net.thucydides.core.annotations.Pending;
import org.apache.commons.lang3.RandomStringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.*;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.hmcts.probate.functional.IntegrationTestBase;
import uk.gov.hmcts.probate.functional.TestRetryRule;
import uk.gov.hmcts.reform.probate.model.forms.CaseSummary;
import uk.gov.hmcts.reform.probate.model.forms.CaseSummaryHolder;

import java.io.IOException;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@RunWith(SpringIntegrationSerenityRunner.class)

public class InvitationControllerFunctionalTests extends IntegrationTestBase {
    private static final String INVITE_URL = "/invite";
    private static final String INVITE_AGREED_URL = "/invite/agreed/";
    private static final String INVITE_AGREED_ALL_URL = "/invite/allAgreed/";
    private static final String INVITE_BILINGUAL_URL = "/invite/bilingual";
    private static final String INVITE_CONTACT_DETAILS_URL = "/invite/contactdetails/";
    private static final String INVITE_PIN_URL = "/invite/pin";
    private static final String INVITE_PIN_BILINGUAL_URL = "/invite/pin/bilingual";
    private static final String INVITE_RESET_AGREED_FLAGS_URL = "/invite/resetAgreed/";
    private static final String FORMDATA_ID_PLACEHOLDER = "XXXXXX";
    private static final String INVITE_ID_PLACEHOLDER = "YYYYY";
    private static final String FORM_INVITE_ID_HOLDER = "ZZZZZZ";
    private static final String INVALID_FORM_DATA_ID = "1604925395199999";
    private static final String SESSION_ID = "Session-Id";
    private static final String TEST_SESSION_ID = "ses123";
    private static final String INVITE_DATA_URL = "/invite/data/";
    private static final String DELETE_INVITE_URL = "/invite/delete/";
    private static final String GET_ALL_INVITES_URL = "/invites/";
    private static final String INVITE_ID = RandomStringUtils.randomAlphanumeric(15);
    public static boolean setUp = true;
    private static long formdataId;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private CaseSummaryHolder caseSummaryHolder;
    @Rule
    public TestRetryRule retryRule = new TestRetryRule(3);

    @Before
    public void aInit() throws IOException, JSONException {
        if (setUp) {
            String responseJsonStr = RestAssured.given()
                    .relaxedHTTPSValidation()
                    .headers(utils.getCitizenHeaders())
                    .queryParam("probateType", "PA")
                    .when()
                    .post("/forms/newcase")
                    .then()
                    .assertThat()
                    .statusCode(200)
                    .body("applications.ccdCase.id", notNullValue())
                    .extract().jsonPath().prettify();
            this.caseSummaryHolder = objectMapper.readValue(responseJsonStr, CaseSummaryHolder.class);
            List<CaseSummary> CaseSummaryList = caseSummaryHolder.getApplications();
            CaseSummaryList.sort((o1, o2) -> o1.getCcdCase().getId().compareTo(o2.getCcdCase().getId()));
            CaseSummary lastCaseSummary = CaseSummaryList.get(CaseSummaryList.size() - 1);
            formdataId = lastCaseSummary.getCcdCase().getId();
            logger.info("Create New case CaseId: {}", formdataId);

            String draftJsonStr = utils.getJsonFromFile("GoPForm_Full.json");
            draftJsonStr = draftJsonStr.replace(FORMDATA_ID_PLACEHOLDER, String.valueOf(formdataId));
            draftJsonStr = draftJsonStr.replace(FORM_INVITE_ID_HOLDER, INVITE_ID);

            Long response = RestAssured.given()
                    .relaxedHTTPSValidation()
                    .headers(utils.getCitizenHeaders())
                    .body(draftJsonStr)
                    .when()
                    .post("/forms/case/" + formdataId)
                    .then()
                    .assertThat()
                    .statusCode(200)
                    .body("ccdCase.state", equalTo("Pending"))
                    .extract().response().path("ccdCase.id");
            logger.info("CaseId After Form Save: {}", response);
            setUp = false;
        }
        logger.info("InviteId: {}", INVITE_ID);
        logger.info("Form DataId: {}", formdataId);
    }


    @Test
    @Pending
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
        validInvitationJsonStr = validInvitationJsonStr.replace(INVITE_ID_PLACEHOLDER, INVITE_ID);
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
    @Pending
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
    @Pending
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
    @Pending
    public void inviteBilingual() {
        String validInvitationJsonStr = utils.getJsonFromFile("validInvitationWithInviteId.json");
        validInvitationJsonStr = validInvitationJsonStr.replace(FORMDATA_ID_PLACEHOLDER, String.valueOf(formdataId));
        validInvitationJsonStr = validInvitationJsonStr.replace(INVITE_ID_PLACEHOLDER, INVITE_ID);
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
    @Pending
    public void updateContactDetails() {
        String validInvitationJsonStr = utils.getJsonFromFile("validInvitationWithInviteId.json");
        validInvitationJsonStr = validInvitationJsonStr.replace(FORMDATA_ID_PLACEHOLDER, String.valueOf(formdataId));
        validInvitationJsonStr = validInvitationJsonStr.replace(INVITE_ID_PLACEHOLDER, INVITE_ID);
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
        Assert.assertEquals(response, INVITE_ID);
    }

    @Test
    @Pending
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
    @Pending
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
                .relaxedHTTPSValidation()
                .headers(utils.getCitizenHeaders())
                .when()
                .post(INVITE_RESET_AGREED_FLAGS_URL + formdataId)
                .then()
                .assertThat()
                .statusCode(200)
                .extract().body().equals(String.valueOf(formdataId));
    }

    @Test
    @Pending
    public void getInviteData() {
        RestAssured.given()
                .relaxedHTTPSValidation()
                .headers(utils.getCitizenHeaders())
                .when()
                .get(INVITE_DATA_URL + INVITE_ID)
                .then()
                .assertThat()
                .statusCode(200)
                .extract().body().jsonPath().prettify();
    }

    @Test
    @Pending
    public void deleteInvites() {
        generateInvitation();
        String validInvitationJsonStr = utils.getJsonFromFile("validInvitationWithInviteId.json");
        validInvitationJsonStr = validInvitationJsonStr.replace(INVITE_ID_PLACEHOLDER, INVITE_ID);
        RestAssured.given()
                .relaxedHTTPSValidation()
                .headers(utils.getCitizenHeaders())
                .body(validInvitationJsonStr)
                .when()
                .post(DELETE_INVITE_URL + formdataId)
                .then()
                .assertThat()
                .statusCode(200)
                .extract().body().jsonPath().prettify();
    }

    @Test
    @Pending
    public void getInvites() {
        RestAssured.given()
                .relaxedHTTPSValidation()
                .headers(utils.getCaseworkerHeaders())
                .when()
                .get(GET_ALL_INVITES_URL + formdataId)
                .then()
                .assertThat()
                .statusCode(200)
                .extract().body().jsonPath().prettify();
    }
}
