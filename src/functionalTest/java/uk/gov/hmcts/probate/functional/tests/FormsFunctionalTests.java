package uk.gov.hmcts.probate.functional.tests;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import net.serenitybdd.junit.spring.integration.SpringIntegrationSerenityRunner;
import net.thucydides.core.annotations.Pending;
import org.hamcrest.Matchers;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
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

@RunWith(SpringIntegrationSerenityRunner.class)
public class FormsFunctionalTests extends IntegrationTestBase {

    private static final String FORMS_NEW_CASE = "/forms/newcase";
    private static final String FORMS_CASES = "/forms/case/";
    private static final String FORMS_GET_ALL_CASES = "/forms/cases";
    private static final String PROBATE_TYPE = "probateType";
    private static final String INVALID_PROBATE_TYPE = "CACA";
    private static final String CCD_CASE_STATE_PENDING = "Pending";
    private static final String CASE_ID_PLACEHOLDER = "XXXXXX";
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Rule
    public TestRetryRule retryRule = new TestRetryRule(3);
    SimpleDateFormat df = new SimpleDateFormat("dd MMMMM yyyy");
    public String currentDate = df.format(new Date());
    private CaseSummaryHolder caseSummaryHolder;
    private static long caseId;
    @Value("${idam.citizen.username}")
    private String email;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

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
    @Pending
    public void initiateFormsNewCaseForProbateTypeCAVEAT() {
        RestAssured.given()
                .relaxedHTTPSValidation()
                .headers(utils.getCitizenHeaders())
                .queryParam("probateType", ProbateType.CAVEAT)
                .when()
                .post(FORMS_NEW_CASE)
                .then()
                .assertThat()
                .statusCode(200)
                .body("applications.ccdCase.id", hasItem(notNullValue()))
                .body("applications.ccdCase.state", hasItem(CCD_CASE_STATE_PENDING))
                .body("applications.caseType", hasItem(ProbateType.CAVEAT.toString()))
                .body("applications.dateCreated", hasItem(currentDate));
    }

    @Test
    public void InitiateFormsNewCaseWithInvalidQueryParam() {
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

    @Test
    public void shouldCreateDraftThenSubmitAndFinallyUpdatePayment() throws IOException {
        setUpANewCase();
        shouldSaveFormSuccessfully();
        //shouldGetCaseDataSuccessfully();
        //shouldUpdateForm();
        //shouldSubmitForm();
        //shouldUpdatePaymentSuccessfully();
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
        List<CaseSummary> CaseSummaryList = caseSummaryHolder.getApplications();
        CaseSummaryList.sort((o1, o2) -> o1.getCcdCase().getId().compareTo(o2.getCcdCase().getId()));
        CaseSummary lastCaseSummary = CaseSummaryList.get(CaseSummaryList.size() - 1);
        caseId = lastCaseSummary.getCcdCase().getId();
        logger.info("Create New case CaseId: {}", caseId);
    }

    public long geCaseId() {
        return this.caseId;
    }

    public void shouldSaveFormSuccessfully() {
        String draftJsonStr = utils.getJsonFromFile("GoPForm_partial.json");
        draftJsonStr = draftJsonStr.replace(CASE_ID_PLACEHOLDER, String.valueOf(caseId));
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

    private void shouldGetCaseDataSuccessfully() throws JSONException {
        String expectedResponseJsonStr = utils.getJsonFromFile("GoPForm_partial.json");
        JSONObject expectedJsonObject = new JSONObject(expectedResponseJsonStr);
        JSONObject expectedDeceasedObject = expectedJsonObject.getJSONObject("deceased");
        String firstName = expectedDeceasedObject.getString("firstName");
        String lastName = expectedDeceasedObject.getString("lastName");

        String resultJson = RestAssured.given()
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
        JSONObject actualDeceasedObject = new JSONObject(resultJson).getJSONObject("deceased");
        Assert.assertEquals(lastName, actualDeceasedObject.getString("lastName"));
        Assert.assertEquals(firstName, actualDeceasedObject.getString("firstName"));
    }

    private void shouldUpdateForm() {
        String draftJsonStr = utils.getJsonFromFile("intestacyForm.json");
        draftJsonStr = draftJsonStr.replace(CASE_ID_PLACEHOLDER, String.valueOf(caseId));

        RestAssured.given()
                .relaxedHTTPSValidation()
                .headers(utils.getCitizenHeaders())
                .body(draftJsonStr)
                .when()
                .put("/forms/" + caseId + "/submissions")
                .then()
                .assertThat()
                .statusCode(200)
                .body("ccdCase.id", equalTo(caseId))
                .body("ccdCase.state", equalTo("Draft"));
    }

    private void shouldSubmitForm() {
        String submitJsonStr = utils.getJsonFromFile("intestacyForm.json");
        submitJsonStr = submitJsonStr.replace(CASE_ID_PLACEHOLDER, String.valueOf(caseId));
        RestAssured.given()
                .relaxedHTTPSValidation()
                .body(submitJsonStr)
                .when()
                .put("/forms/" + caseId + "/submissions")
                .then()
                .assertThat()
                .statusCode(200)
                .body("ccdCase.id", equalTo(caseId))
                .body("ccdCase.state", equalTo("PAAppCreated"));
    }

    private void shouldUpdatePaymentSuccessfully() {
        String paymentJsonStr = utils.getJsonFromFile("intestacyForm_full.json");
        paymentJsonStr = paymentJsonStr.replace(CASE_ID_PLACEHOLDER, String.valueOf(caseId));

        RestAssured.given()
                .relaxedHTTPSValidation()
                .body(paymentJsonStr)
                .when()
                .post("/forms/" + caseId + "/payments")
                .then()
                .assertThat()
                .statusCode(200)
                .body("ccdCase.id", equalTo(caseId))
                .body("ccdCase.state", equalTo("CaseCreated"));
    }

    public enum ProbateType {
        INTESTACY, PA, CAVEAT
    }
}
