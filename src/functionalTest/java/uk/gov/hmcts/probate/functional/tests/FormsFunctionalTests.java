package uk.gov.hmcts.probate.functional.tests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import io.restassured.http.Headers;
import net.serenitybdd.junit.spring.integration.SpringIntegrationSerenityRunner;
import net.thucydides.core.annotations.Pending;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.hmcts.probate.functional.IntegrationTestBase;

import java.io.IOException;
import java.util.Map;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@RunWith(SpringIntegrationSerenityRunner.class)
public class FormsFunctionalTests extends IntegrationTestBase {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final String EMAIL_PLACEHOLDER = "XXXXXXXXXX";
    private static final String PASSWORD = "Probate123";
    private static final String USER_GROUP_NAME = "probate-private-beta";

    private ObjectMapper objectMapper;

    private String email;

    private Long caseId;

    @Before
    public void setUp() throws JsonProcessingException {
        objectMapper = new ObjectMapper();
        String forename = RandomStringUtils.randomAlphanumeric(5);
        String surname = RandomStringUtils.randomAlphanumeric(5);
        email = forename + "." + surname + "@email.com";
        logger.info("Generate user name: {}", email);

        IdamData idamData = IdamData.builder()
            .email(email)
            .forename(forename)
            .surname(surname)
            .password(PASSWORD)
            .userGroupName(USER_GROUP_NAME)
            .build();

        RestAssured.given()
            .relaxedHTTPSValidation()
            .headers(Headers.headers(new Header("FormDataContent-Type", ContentType.JSON.toString())))
            .baseUri(idamUrl)
            .body(objectMapper.writeValueAsString(idamData))
            .when()
            .post("/testing-support/accounts")
            .then()
            .statusCode(204);

    }

    @Test
    @Pending
    public void shouldCreateDraftThenSubmitAndFinallyUpdatePayment() throws IOException {
        shouldSaveFormSuccessfully();
        shouldUpdateForm();
        shouldSubmitForm();
        shouldUpdatePaymentSuccessfully();
    }

    private void shouldSaveFormSuccessfully() throws IOException {
        String draftJsonStr = utils.getJsonFromFile("intestacyForm_partial.json");
        draftJsonStr = draftJsonStr.replace(EMAIL_PLACEHOLDER, email);

        String responseJsonStr = RestAssured.given()
            .relaxedHTTPSValidation()
            .headers(utils.getHeaders(email, PASSWORD))
            .body(draftJsonStr)
            .when()
            .post("/forms/" + email)
            .then()
            .assertThat()
            .statusCode(200)
            .body("ccdCase.id", notNullValue())
            .body("ccdCase.state", equalTo("Draft"))
            .extract().jsonPath().prettify();
        Map<String, Object> actualJsonMap = objectMapper.readValue(responseJsonStr, Map.class);
        Map<String, Object> ccdCase = (Map<String, Object>) actualJsonMap.get("ccdCase");
        caseId = (Long) ccdCase.get("id") ;
    }

    private void shouldUpdateForm() {
        String draftJsonStr = utils.getJsonFromFile("intestacyForm_full.json");
        draftJsonStr = draftJsonStr.replace(EMAIL_PLACEHOLDER, email);

        RestAssured.given()
            .relaxedHTTPSValidation()
            .headers(utils.getHeaders(email, PASSWORD))
            .body(draftJsonStr)
            .when()
            .post("/forms/" + email)
            .then()
            .assertThat()
            .statusCode(200)
            .body("ccdCase.id", equalTo(caseId))
            .body("ccdCase.state", equalTo("Draft"));
    }

    private void shouldSubmitForm() {
        String submitJsonStr = utils.getJsonFromFile("intestacyForm_full.json");
        submitJsonStr = submitJsonStr.replace(EMAIL_PLACEHOLDER, email);

        RestAssured.given()
            .relaxedHTTPSValidation()
            .headers(utils.getHeaders(email, PASSWORD))
            .body(submitJsonStr)
            .when()
            .post("/forms/" + email + "/submissions")
            .then()
            .assertThat()
            .statusCode(200)
            .body("ccdCase.id", equalTo(caseId))
            .body("ccdCase.state", equalTo("PAAppCreated"));
    }

    private void shouldUpdatePaymentSuccessfully() {
        String paymentJsonStr = utils.getJsonFromFile("intestacyForm_full.json");

        RestAssured.given()
            .relaxedHTTPSValidation()
            .headers(utils.getHeaders(email, PASSWORD))
            .body(paymentJsonStr)
            .when()
            .post("/forms/" + email + "/payments")
            .then()
            .assertThat()
            .statusCode(200)
            .body("ccdCase.id", equalTo(caseId))
            .body("ccdCase.state", equalTo("CaseCreated"));
    }
}
