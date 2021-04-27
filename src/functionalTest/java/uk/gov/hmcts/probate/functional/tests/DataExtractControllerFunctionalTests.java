package uk.gov.hmcts.probate.functional.tests;

import io.restassured.RestAssured;
import net.serenitybdd.junit.spring.integration.SpringIntegrationSerenityRunner;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import uk.gov.hmcts.probate.functional.IntegrationTestBase;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@RunWith(SpringIntegrationSerenityRunner.class)
public class DataExtractControllerFunctionalTests extends IntegrationTestBase {
    private static final String DATA_EXTRACT_HMRC_URL = "/data-extract/hmrc";
    private static final String DATA_EXTRACT_EXELA_URL = "/data-extract/exela";
    private static final String DATA_EXTRACT_IRON_MOUNTAIN_URL = "/data-extract/iron-mountain";
    private static final String EXELA_DATA_EXTRACT_FINISHED = "Perform Exela data extract finished";
    private static final String HMRC_DATA_EXTRACT_FINISHED = "Perform HMRC data extract finished";
    private static final String IRON_MOUNTAIN_DATA_EXTRACT_FINISHED = "Perform Iron Mountain data extract finished";
    private static final String DATA_EXTRACT_SMEE_AND_FORD_URL = "/data-extract/smee-and-ford";
    private static final String SMEE_AND_FORD_EXTRACT_FINISHED = "Perform Smee And Ford data extract finished";


    @Test
    public void performHmrcDataExtractAsCitizen() {
        String response = RestAssured.given()
            .relaxedHTTPSValidation()
            .headers(utils.getCitizenHeaders())
            .when()
            .post(DATA_EXTRACT_HMRC_URL)
            .then()
            .assertThat()
            .statusCode(202)
            .extract().response().getBody().prettyPrint();
        Assert.assertEquals(HMRC_DATA_EXTRACT_FINISHED, response);
    }

    @Test
    public void performHmrcDataExtractAsCaseWorker() {
        String response = RestAssured.given()
            .relaxedHTTPSValidation()
            .headers(utils.getCaseworkerHeaders())
            .when()
            .post(DATA_EXTRACT_HMRC_URL)
            .then()
            .assertThat()
            .statusCode(202)
            .extract().response().getBody().prettyPrint();
        Assert.assertEquals(HMRC_DATA_EXTRACT_FINISHED, response);
    }

    @Test
    public void performHmrcDataExtractForAGivenDate() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String todayDate = dateTimeFormatter.format(LocalDate.now());
        String response = RestAssured.given()
            .relaxedHTTPSValidation()
            .headers(utils.getCitizenHeaders())
            .when()
            .post(DATA_EXTRACT_HMRC_URL + "/" + todayDate)
            .then()
            .assertThat()
            .statusCode(202)
            .extract().response().getBody().prettyPrint();
        Assert.assertEquals(HMRC_DATA_EXTRACT_FINISHED, response);
    }

    @Test
    public void performHmrcDataExtractForAnInvalidDate() {
        String invalidDate = "2020-11-49";
        RestAssured.given()
            .relaxedHTTPSValidation()
            .headers(utils.getCitizenHeaders())
            .when()
            .post(DATA_EXTRACT_HMRC_URL + "/" + invalidDate)
            .then()
            .assertThat()
            .statusCode(400);
    }

    @Test
    public void performHmrcDataExtractForAGivenDateRange() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String todayDate = dateTimeFormatter.format(LocalDate.now());
        String yesterdayDate = dateTimeFormatter.format(LocalDate.now().minusDays(1L));
        System.out.println("test:" + DATA_EXTRACT_HMRC_URL + "/" + yesterdayDate + "/" + todayDate);
        String response = RestAssured.given()
            .relaxedHTTPSValidation()
            .headers(utils.getCaseworkerHeaders())
            .when()
            .post(DATA_EXTRACT_HMRC_URL + "/" + yesterdayDate + "/" + todayDate)
            .then()
            .assertThat()
            .statusCode(202)
            .extract().response().getBody().prettyPrint();
        Assert.assertEquals(HMRC_DATA_EXTRACT_FINISHED, response);
    }

    @Test
    public void performHmrcDataExtractForAGivenInvalidDateRange() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String todayDate = dateTimeFormatter.format(LocalDate.now());
        String yesterdayDate = "2020-44-12";
        System.out.println("test:" + DATA_EXTRACT_HMRC_URL + "/" + yesterdayDate + "/" + todayDate);
        RestAssured.given()
            .relaxedHTTPSValidation()
            .headers(utils.getCaseworkerHeaders())
            .when()
            .post(DATA_EXTRACT_HMRC_URL + "/" + yesterdayDate + "/" + todayDate)
            .then()
            .assertThat()
            .statusCode(400);
    }

    @Test
    public void performExelaDataExtractAsCitizen() {
        String response = RestAssured.given()
            .relaxedHTTPSValidation()
            .headers(utils.getCitizenHeaders())
            .when()
            .post(DATA_EXTRACT_EXELA_URL)
            .then()
            .assertThat()
            .statusCode(202)
            .extract().response().getBody().prettyPrint();
        Assert.assertEquals(EXELA_DATA_EXTRACT_FINISHED, response);
    }

    @Test
    public void performExelaDataExtractAsCaseWorker() {
        String response = RestAssured.given()
            .relaxedHTTPSValidation()
            .headers(utils.getCaseworkerHeaders())
            .when()
            .post(DATA_EXTRACT_EXELA_URL)
            .then()
            .assertThat()
            .statusCode(202)
            .extract().response().getBody().prettyPrint();
        Assert.assertEquals(EXELA_DATA_EXTRACT_FINISHED, response);
    }

    @Test
    public void performExelaDataExtractForAGivenDate() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String todayDate = dateTimeFormatter.format(LocalDate.now());
        String response = RestAssured.given()
            .relaxedHTTPSValidation()
            .headers(utils.getCitizenHeaders())
            .when()
            .post(DATA_EXTRACT_EXELA_URL + "/" + todayDate)
            .then()
            .assertThat()
            .statusCode(202)
            .extract().response().getBody().prettyPrint();
        Assert.assertEquals(EXELA_DATA_EXTRACT_FINISHED, response);
    }

    @Test
    public void performExelaDataExtractForAnInvalidDate() {
        String invalidDate = "2020-11-49";
        RestAssured.given()
            .relaxedHTTPSValidation()
            .headers(utils.getCitizenHeaders())
            .when()
            .post(DATA_EXTRACT_EXELA_URL + "/" + invalidDate)
            .then()
            .assertThat()
            .statusCode(400);
    }

    @Test
    public void performIronMountainDataExtractAsCitizen() {
        String response = RestAssured.given()
            .relaxedHTTPSValidation()
            .headers(utils.getCitizenHeaders())
            .when()
            .post(DATA_EXTRACT_IRON_MOUNTAIN_URL)
            .then()
            .assertThat()
            .statusCode(202)
            .extract().response().getBody().prettyPrint();
        Assert.assertEquals(IRON_MOUNTAIN_DATA_EXTRACT_FINISHED, response);
    }

    @Test
    public void performIronMountainDataExtractAsCaseWorker() {
        String response = RestAssured.given()
            .relaxedHTTPSValidation()
            .headers(utils.getCaseworkerHeaders())
            .when()
            .post(DATA_EXTRACT_IRON_MOUNTAIN_URL)
            .then()
            .assertThat()
            .statusCode(202)
            .extract().response().getBody().prettyPrint();
        Assert.assertEquals(IRON_MOUNTAIN_DATA_EXTRACT_FINISHED, response);
    }

    @Test
    public void performIronMountainDataExtractForAGivenDate() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String todayDate = dateTimeFormatter.format(LocalDate.now());
        String response = RestAssured.given()
            .relaxedHTTPSValidation()
            .headers(utils.getCitizenHeaders())
            .when()
            .post(DATA_EXTRACT_IRON_MOUNTAIN_URL + "/" + todayDate)
            .then()
            .assertThat()
            .statusCode(202)
            .extract().response().getBody().prettyPrint();
        Assert.assertEquals(IRON_MOUNTAIN_DATA_EXTRACT_FINISHED, response);
    }

    @Test
    public void performIronMountainDataExtractForAnInvalidDate() {
        String invalidDate = "2020-11-49";
        RestAssured.given()
            .relaxedHTTPSValidation()
            .headers(utils.getCitizenHeaders())
            .when()
            .post(DATA_EXTRACT_IRON_MOUNTAIN_URL + "/" + invalidDate)
            .then()
            .assertThat()
            .statusCode(400);
    }

    @Test
    public void performSmeeAndFordDataExtractScheduled() {
        String response = RestAssured.given()
            .relaxedHTTPSValidation()
            .headers(utils.getCaseworkerHeaders())
            .when()
            .post(DATA_EXTRACT_SMEE_AND_FORD_URL)
            .then()
            .assertThat()
            .statusCode(202)
            .extract().response().getBody().prettyPrint();
        Assert.assertEquals(SMEE_AND_FORD_EXTRACT_FINISHED, response);
    }

    @Test
    public void performSmeeAndFordDataExtractForDateRange() {
        String response = RestAssured.given()
            .relaxedHTTPSValidation()
            .headers(utils.getCaseworkerHeaders())
            .when()
            .post(DATA_EXTRACT_SMEE_AND_FORD_URL + "/2020-09-11/2020-09-24")
            .then()
            .assertThat()
            .statusCode(202)
            .extract().response().getBody().prettyPrint();
        Assert.assertEquals(SMEE_AND_FORD_EXTRACT_FINISHED, response);
    }

}
