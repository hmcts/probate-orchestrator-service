package uk.gov.hmcts.probate.functional.tests;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import net.serenitybdd.junit.spring.integration.SpringIntegrationSerenityRunner;
import net.thucydides.core.annotations.Pending;
import org.apache.commons.lang3.RandomStringUtils;
import org.hamcrest.Matchers;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import uk.gov.hmcts.probate.functional.IntegrationTestBase;
import uk.gov.hmcts.probate.functional.TestRetryRule;
import uk.gov.hmcts.reform.probate.model.forms.CaseSummary;
import uk.gov.hmcts.reform.probate.model.forms.CaseSummaryHolder;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(SpringIntegrationSerenityRunner.class)
public class FormsFunctionalTests extends IntegrationTestBase {

    private static final String FORMS_NEW_CASE = "/forms/newcase";
    private static final String FORMS_CASES = "/forms/case/";
    private static final String FORMS_GET_ALL_CASES = "/forms/cases";
    private static final String PROBATE_TYPE = "probateType";
    private static final String INVALID_PROBATE_TYPE = "CACA";
    private static final String CCD_CASE_STATE_PENDING = "Pending";
    private static final String CASE_ID_PLACEHOLDER = "XXXXXX";
    private static final String INVITE_ID_PLACEHOLDER = "ZZZZZZ";
    private static final String FORMS_BASE_URL = "/forms/";
    private static final String FORMS_VALIDATIONS_URL = "/validations";
    private static final String INVITE_ID = RandomStringUtils.randomAlphanumeric(15);
    private static long caseId;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Rule
    public TestRetryRule retryRule = new TestRetryRule(3);
    SimpleDateFormat df = new SimpleDateFormat("dd MMMMM yyyy");
    public String currentDate = df.format(new Date());
    private CaseSummaryHolder caseSummaryHolder;
    @Value("${idam.citizen.username}")
    private String email;

    @Test
    public void initiateFormsNewCaseForProbateTypePA() {
        RestAssured.given()
                .relaxedHTTPSValidation()
                .headers(utils.getCitizenHeaders())
                .queryParam(PROBATE_TYPE, ProbateType.PA)
                .when()
                .post(FORMS_NEW_CASE)
                .then()
                .assertThat()
                .statusCode(200)
                .body("applications.ccdCase.id", hasItem(notNullValue()))
                .body("applications.ccdCase.state", hasItem(CCD_CASE_STATE_PENDING))
                .body("applications.caseType", hasItem(ProbateType.PA.toString()))
                .body("applications.dateCreated", hasItem(currentDate));
    }

    @Test
    public void initiateFormsNewCaseForProbateTypeIntestacy() {
        RestAssured.given()
                .relaxedHTTPSValidation()
                .headers(utils.getCitizenHeaders())
                .queryParam("probateType", ProbateType.INTESTACY)
                .when()
                .post(FORMS_NEW_CASE)
                .then()
                .assertThat()
                .statusCode(200)
                .body("applications.ccdCase.id", hasItem(notNullValue()))
                .body("applications.ccdCase.state", hasItem(CCD_CASE_STATE_PENDING))
                .body("applications.caseType", hasItem(ProbateType.INTESTACY.toString()))
                .body("applications.dateCreated", hasItem(currentDate));
    }

    @Test
    public void initiateFormsNewCaseWithInvalidQueryParam() {
        RestAssured.given()
                .relaxedHTTPSValidation()
                .headers(utils.getCitizenHeaders())
                .queryParam("probateType", INVALID_PROBATE_TYPE)
                .when()
                .post(FORMS_NEW_CASE)
                .then()
                .assertThat()
                .statusCode(400);
    }

    @Test
    public void getAllFormsCases() {
        RestAssured.given()
                .relaxedHTTPSValidation()
                .headers(utils.getCitizenHeaders())
                .when()
                .get(FORMS_GET_ALL_CASES)
                .then()
                .assertThat()
                .statusCode(200);
    }

    @Pending
    @Test
    public void shouldCreateDraftThenSubmitAndFinallyUpdatePayment() throws IOException, JSONException {
        setUpANewCase();
        shouldSaveFormSuccessfully();
        shouldGetCaseDataSuccessfully();
        shouldSubmitPaymentSuccessfully();
    }

    public void setUpANewCase() throws IOException {
        String responseJsonStr = RestAssured.given()
                .relaxedHTTPSValidation()
                .headers(utils.getCitizenHeaders())
                .queryParam(PROBATE_TYPE, ProbateType.PA)
                .when()
                .post(FORMS_NEW_CASE)
                .then()
                .assertThat()
                .statusCode(200)
                .body("applications.ccdCase.id", notNullValue())
                .extract().jsonPath().prettify();
        this.caseSummaryHolder = objectMapper.readValue(responseJsonStr, CaseSummaryHolder.class);
        List<CaseSummary> caseSummaryList = caseSummaryHolder.getApplications();
        caseSummaryList.sort((o1, o2) -> o1.getCcdCase().getId().compareTo(o2.getCcdCase().getId()));
        CaseSummary lastCaseSummary = caseSummaryList.get(caseSummaryList.size() - 1);
        caseId = lastCaseSummary.getCcdCase().getId();
    }

    public void shouldSaveFormSuccessfully() {
        String draftJsonStr = utils.getJsonFromFile("GoPForm_Full.json");
        draftJsonStr = draftJsonStr.replace(CASE_ID_PLACEHOLDER, String.valueOf(caseId));
        draftJsonStr = draftJsonStr.replace(INVITE_ID_PLACEHOLDER,INVITE_ID);

        Long response = RestAssured.given()
                .relaxedHTTPSValidation()
                .headers(utils.getCitizenHeaders())
                .body(draftJsonStr)
                .when()
                .post(FORMS_CASES + caseId)
                .then()
                .assertThat()
                .statusCode(200)
                .body("ccdCase.state", equalTo(CCD_CASE_STATE_PENDING))
                .extract().response().path("ccdCase.id");
        logger.info("CaseId After Form Save: {}", response);
    }

    public void shouldGetCaseDataSuccessfully() {
        RestAssured.given()
                .relaxedHTTPSValidation()
                .headers(utils.getCitizenHeaders())
                .queryParam(PROBATE_TYPE, ProbateType.PA)
                .when()
                .get(FORMS_CASES + caseId)
                .then()
                .assertThat()
                .statusCode(200)
                .body("ccdCase.id", Matchers.is(caseId))
                .body("ccdCase.state", Matchers.is(CCD_CASE_STATE_PENDING))
                .extract().jsonPath().prettify();
    }

    private void shouldSubmitPaymentSuccessfully() throws JSONException {
        String paymentDtoJsonStr = utils.getJsonFromFile("PaymentDtoTest.json");
        String response = RestAssured.given()
                .relaxedHTTPSValidation()
                .headers(utils.getCitizenHeaders())
                .queryParam("probateType", ProbateType.PA)
                .body(paymentDtoJsonStr)
                .when()
                .put("/forms/" + caseId + "/submissions")
                .then()
                .extract().response().prettyPrint();
        logger.info("Response shouldSubmitPaymentSuccessfully : {}",response);
    }

    private void shouldUpdatePaymentSuccessfully() {
        String paymentJsonStr = utils.getJsonFromFile("submissionForm.json");
        paymentJsonStr = paymentJsonStr.replace(CASE_ID_PLACEHOLDER, String.valueOf(caseId));
        logger.info("CaseId shouldUpdatePaymentSuccessfully: {}", caseId);
        RestAssured.given()
                .relaxedHTTPSValidation()
                .headers(utils.getCitizenHeaders())
                .body(paymentJsonStr)
                .when()
                .post("/forms/" + caseId + "/payments")
                .then()
                .assertThat()
                .statusCode(200)
                .body("ccdCase.id", equalTo(caseId))
                .body("ccdCase.state", equalTo("CaseCreated"));
    }

    @Pending
    @Test
    public void shouldSubmitForm() throws IOException, JSONException {
        String submitJsonStr = utils.getJsonFromFile("caveatForm.json");
        String genApplicationId = RandomStringUtils.randomAlphanumeric(12).toLowerCase();
        submitJsonStr = submitJsonStr.replace(CASE_ID_PLACEHOLDER, genApplicationId);
        JSONObject applicationIdJsonObject = new JSONObject(submitJsonStr);
        String applicationId = applicationIdJsonObject.getString("applicationId");

        RestAssured.given()
                .relaxedHTTPSValidation()
                .headers(utils.getCitizenHeaders())
                .body(submitJsonStr)
                .when()
                .post("/forms/" + applicationId + "/submissions")
                .then()
                .assertThat()
                .statusCode(200)
                .body("type", Matchers.is("Caveat"));

    }

    @Pending
    @Test
    public void shouldValidateFormSuccessfully() throws IOException {
        logger.info("CaseId shouldValidateFormSuccessfully : {}", caseId);
        RestAssured.given()
                .relaxedHTTPSValidation()
                .headers(utils.getCitizenHeaders())
                .queryParam("probateType", ProbateType.PA)
                .when()
                .put(FORMS_BASE_URL + caseId + FORMS_VALIDATIONS_URL)
                .then()
                .assertThat()
                .statusCode(200)
                .body("ccdCase.id", equalTo(caseId));
    }

    @Pending
    @Test
    public void testSubmitPaymentWithZeroTotalSuccessfully() throws IOException {
        setUpANewCase();
        shouldSaveFormSuccessfully();
        logger.info("CaseId testSubmitPaymentWithZeroTotalSuccessfully : {}", caseId);
        String paymentDtoJsonStr = utils.getJsonFromFile("submitPaymentDto.json");
        RestAssured.given()
                .relaxedHTTPSValidation()
                .headers(utils.getCitizenHeaders())
                .queryParam("probateType", ProbateType.PA)
                .body(paymentDtoJsonStr)
                .when()
                .put("/forms/" + caseId + "/submissions")
                .then()
                .assertThat()
                .statusCode(200)
                .body("ccdCase.id", equalTo(caseId))
                .body("ccdCase.state", equalTo("CaseCreated"));
    }

    @Pending
    @Test
    public void testValidateFormForValidationErrors() throws IOException {
        setUpANewCase();
        String draftJsonStr = utils.getJsonFromFile("GoPForm_partial.json");
        draftJsonStr = draftJsonStr.replace(CASE_ID_PLACEHOLDER, String.valueOf(caseId));
        RestAssured.given()
                .relaxedHTTPSValidation()
                .headers(utils.getCitizenHeaders())
                .body(draftJsonStr)
                .when()
                .post(FORMS_CASES + caseId)
                .then()
                .assertThat()
                .statusCode(200)
                .body("ccdCase.state", equalTo(CCD_CASE_STATE_PENDING));
        logger.info("CaseId testValidateFormForValidationErrors : {}", caseId);
        RestAssured.given()
                .relaxedHTTPSValidation()
                .headers(utils.getCitizenHeaders())
                .queryParam("probateType", ProbateType.PA)
                .when()
                .put(FORMS_BASE_URL + caseId + FORMS_VALIDATIONS_URL)
                .then()
                .assertThat()
                .statusCode(400)
                .body("type", equalTo("VALIDATION"));
    }

    @Test
    public void testPaymentFailedForInvalidPaymentStatusInTheForm() throws IOException, JSONException {
        setUpANewCase();
        shouldSaveFormSuccessfully();
        shouldSubmitPaymentSuccessfully();
        String paymentJsonStr = utils.getJsonFromFile("updatePaymentFormWithInvalidPaymentStatus.json");
        paymentJsonStr = paymentJsonStr.replace(CASE_ID_PLACEHOLDER, String.valueOf(caseId));
        logger.info("CaseId testPaymentFailedForInvalidPaymentStatusInTheForm : {}", caseId);
        RestAssured.given()
                .relaxedHTTPSValidation()
                .headers(utils.getCitizenHeaders())
                .body(paymentJsonStr)
                .when()
                .post("/forms/" + caseId + "/payments")
                .then()
                .assertThat()
                .statusCode(400);

    }

    public enum ProbateType {
        INTESTACY, PA
    }
}
