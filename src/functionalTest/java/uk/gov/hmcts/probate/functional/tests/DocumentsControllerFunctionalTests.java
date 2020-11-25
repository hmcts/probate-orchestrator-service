package uk.gov.hmcts.probate.functional.tests;

import io.restassured.RestAssured;
import net.serenitybdd.junit.spring.integration.SpringIntegrationSerenityRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import uk.gov.hmcts.probate.functional.IntegrationTestBase;


@RunWith(SpringIntegrationSerenityRunner.class)
public class DocumentsControllerFunctionalTests extends IntegrationTestBase {
    private static final String LEGAL_DECLARATION_URL = "/documents/generate/legalDeclaration";
    private static final String CHECK_ANSWERS_SUMMARY_URL = "/documents/generate/checkAnswersSummary";
    private static final String BULK_SCAN_COVERSHEET_URL = "/documents/generate/bulkScanCoversheet";
    private static final String DOCUMENTS_UPLOAD_URL = "/documents/upload";
    private static final String DOCUMENTS_DELETE_URL = "/documents/delete/";
    private static final String INVALID_DOCUMENT_ID = "999938c0-4517-45a1-ae60-f5c170200bbb";
    private String documentId;


    @Test
    public void uploadAndDeleteDocumentSuccessfully() {
        uploadDocumentSuccessfully();
        deleteDocumentSuccessfully();

    }

    public void uploadDocumentSuccessfully() {
        String response = RestAssured.given()
                .relaxedHTTPSValidation()
                .headers(utils.getCitizenHeaders())
                .header("user-id", "testuser")
                .contentType("multipart/form-data")
                .multiPart("file", utils.getFile("TestDocument.jpeg"), "image/jpeg")
                .when()
                .post(DOCUMENTS_UPLOAD_URL)
                .then()
                .assertThat()
                .statusCode(200)
                .extract().jsonPath().prettify();
        documentId = response.substring(response.lastIndexOf("/") + 1, response.lastIndexOf("]") - 2);

    }

    public void deleteDocumentSuccessfully() {
        RestAssured.given()
                .relaxedHTTPSValidation()
                .headers(utils.getCitizenHeaders())
                .header("user-id", "testuser")
                .when()
                .delete(DOCUMENTS_DELETE_URL + documentId)
                .then()
                .assertThat()
                .statusCode(200);
    }

    @Test
    public void deleteInvalidDocument() {
        RestAssured.given()
                .relaxedHTTPSValidation()
                .headers(utils.getCitizenHeaders())
                .when()
                .delete(DOCUMENTS_DELETE_URL + INVALID_DOCUMENT_ID)
                .then()
                .assertThat()
                .statusCode(400);
    }

    @Test
    public void generatePDFforLegalDeclarationForValidData() {
        String validLegalDeclarationJsonStr = utils.getJsonFromFile("validLegalDeclaration.json");
        RestAssured.given()
                .relaxedHTTPSValidation()
                .headers(utils.getCitizenHeaders())
                .body(validLegalDeclarationJsonStr)
                .when()
                .post(LEGAL_DECLARATION_URL)
                .then()
                .assertThat()
                .statusCode(200);
    }

    @Test
    public void generatePDFforLegalDeclarationWithInValidAttribute() {
        String invalidLegalDeclarationJsonStr = utils.getJsonFromFile("InvalidAttributeLegalDeclaration.json");
        RestAssured.given()
                .relaxedHTTPSValidation()
                .headers(utils.getCitizenHeaders())
                .body(invalidLegalDeclarationJsonStr)
                .when()
                .post(LEGAL_DECLARATION_URL)
                .then()
                .assertThat()
                .statusCode(400);
    }

    @Test
    public void checkAnswersSummaryPDFGeneratedSuccessfully() {
        String validAnswersSummaryJsonStr = utils.getJsonFromFile("validAnswersSummary.json");
        RestAssured.given()
                .relaxedHTTPSValidation()
                .headers(utils.getCitizenHeaders())
                .body(validAnswersSummaryJsonStr)
                .when()
                .post(CHECK_ANSWERS_SUMMARY_URL)
                .then()
                .assertThat()
                .statusCode(200);
    }

    @Test
    public void generatePDFforCheckAnswersSummaryWithInValidAttribute() {
        String invalidAttributeAnswersSummaryJsonStr = utils.getJsonFromFile("invalidAttributeAnswersSummary.json");
        RestAssured.given()
                .relaxedHTTPSValidation()
                .headers(utils.getCitizenHeaders())
                .body(invalidAttributeAnswersSummaryJsonStr)
                .when()
                .post(CHECK_ANSWERS_SUMMARY_URL)
                .then()
                .assertThat()
                .statusCode(400);
    }

    @Test
    public void generatePDFforBulkScanCoversheetForValidData() {
        String validBulkScanCoversheetJsonStr = utils.getJsonFromFile("validBulkScanCoversheet.json");
        RestAssured.given()
                .relaxedHTTPSValidation()
                .headers(utils.getCitizenHeaders())
                .body(validBulkScanCoversheetJsonStr)
                .when()
                .post(BULK_SCAN_COVERSHEET_URL)
                .then()
                .assertThat()
                .statusCode(200);
    }

    @Test
    public void generatePDFforBulkScanCoversheetWithInValidAttribute() {
        String invalidBulkScanCoversheetJsonStr = utils.getJsonFromFile("invalidBulkScanCoversheet.json");
        RestAssured.given()
                .relaxedHTTPSValidation()
                .headers(utils.getCitizenHeaders())
                .body(invalidBulkScanCoversheetJsonStr)
                .when()
                .post(BULK_SCAN_COVERSHEET_URL)
                .then()
                .assertThat()
                .statusCode(400);
    }
}


