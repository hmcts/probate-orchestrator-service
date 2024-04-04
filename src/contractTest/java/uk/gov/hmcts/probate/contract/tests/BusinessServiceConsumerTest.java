package uk.gov.hmcts.probate.contract.tests;

import au.com.dius.pact.core.model.annotations.Pact;
import au.com.dius.pact.core.model.annotations.PactFolder;
import au.com.dius.pact.consumer.dsl.PM;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.core.model.RequestResponsePact;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.pactfoundation.consumer.dsl.LambdaDslJsonArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.ResourceUtils;
import uk.gov.hmcts.probate.client.business.BusinessServiceApi;
import uk.gov.hmcts.reform.probate.model.documents.BulkScanCoverSheet;
import uk.gov.hmcts.reform.probate.model.documents.CheckAnswersSummary;
import uk.gov.hmcts.reform.probate.model.documents.LegalDeclaration;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.UndeclaredThrowableException;
import java.nio.file.Files;

import static io.pactfoundation.consumer.dsl.LambdaDsl.newJsonBody;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;


@ExtendWith(PactConsumerTestExt.class)
@ExtendWith(SpringExtension.class)
@PactTestFor(providerName = "probate_business_service", port = "8894")
@PactFolder("pacts")
@SpringBootTest({
    // overriding provider address
    "probate_submitservice.ribbon.listOfServers: localhost:8891",
    "submit.service.api.url : localhost:8892",
    "core_case_data.api.url : localhost:8893",
    "business.service.api.url : localhost:8894"
})
public class BusinessServiceConsumerTest {


    @Autowired
    private BusinessServiceApi businessServiceApi;

    @Autowired
    ContractTestUtils contractTestUtils;

    @Autowired
    ObjectMapper objectMapper;

    public static final String SOME_AUTHORIZATION_TOKEN = "someAuthorizationToken";
    public static final String SOME_SERVICE_AUTHORIZATION_TOKEN = "someServiceAuthorizationToken";
    private String serviceAuthorization = "ServiceAuthorization";

    @BeforeEach
    public void setUpTest() throws InterruptedException {
        Thread.sleep(2000);
    }

    @Pact(provider = "probate_businessservice_documents", consumer = "probate_orchestrator_service")
    public RequestResponsePact executeSuccessGetCheckYoursAnswersSummaryPact(PactDslWithProvider builder)
        throws IOException, JSONException {
        // @formatter:off
        return builder
                .given("business service returns check your answers document with success")
                .uponReceiving("a request to POST check your answers doc ")
                .path("/businessDocument/generateCheckAnswersSummaryPDF")
                .method("POST")
                .headers(AUTHORIZATION, SOME_AUTHORIZATION_TOKEN, serviceAuthorization,
                SOME_SERVICE_AUTHORIZATION_TOKEN)
                .body(contractTestUtils.createJsonObject("businessDocuments/checkAnswersSimpleSummary.json"))
                .willRespondWith()
                .status(200)
                .matchHeader("FormDataContent-Type", "application/octet-stream")
                .toPact();
        // @formatter:on
    }


    @Pact(provider = "probate_businessservice_documents", consumer = "probate_orchestrator_service")
    public RequestResponsePact executeValidationErrorsCheckYoursAnswersSummaryPact(PactDslWithProvider builder)
        throws IOException, JSONException {
        // @formatter:off
        return builder
                .given("business service returns validation errors for invalid check answers summary")
                .uponReceiving("a request to POST invalid check your answers doc ")
                .path("/businessDocument/generateCheckAnswersSummaryPDF")
                .method("POST")
                .headers(AUTHORIZATION, SOME_AUTHORIZATION_TOKEN, serviceAuthorization,
                SOME_SERVICE_AUTHORIZATION_TOKEN)
                //.body(invalidCheckAnswersSummaryBody)
                .body(contractTestUtils.createJsonObject("businessDocuments/invalidCheckAnswersSummary.json"))
                .willRespondWith()
                .status(400)
                .matchHeader("FormDataContent-Type", "application/json;charset=UTF-8")
                .body(newJsonBody((o) -> {
                    o.stringValue("path", "/businessDocument/generateCheckAnswersSummaryPDF");
                    o.numberType("timestamp");
                    o.array("errors", (a) -> {
                        addNonBlankError(a, "checkAnswersSummary", "mainParagraph");
                    });
                    o.stringValue("error", "Bad Request");
                    o.numberValue("status", 400);
                    o.stringValue("message", "Validation failed for object='checkAnswersSummary'. Error count: 1");
                }).build())
                .toPact();

        // @formatter:on
    }


    @Pact(provider = "probate_businessservice_documents", consumer = "probate_orchestrator_service")
    public RequestResponsePact executeSuccessGetLegalDeclarationPact(PactDslWithProvider builder)
        throws IOException, JSONException {
        // @formatter:off
        return builder
                    .given("business service returns legal declaration document with success")
                    .uponReceiving("a request to POST a legal declaration doc")
                    .path("/businessDocument/generateLegalDeclarationPDF")
                    .method("POST")
                    .headers(AUTHORIZATION, SOME_AUTHORIZATION_TOKEN, serviceAuthorization,
                    SOME_SERVICE_AUTHORIZATION_TOKEN)
                    .body(contractTestUtils.createJsonObject("businessDocuments/validLegalDeclaration.json"))
                    .matchHeader("Content-Type", "application/json")
                    .willRespondWith()
                    .status(200)
                    .matchHeader("FormDataContent-Type", "application/octet-stream")
                    .toPact();
        // @formatter:on
    }


    @Pact(provider = "probate_businessservice_documents", consumer = "probate_orchestrator_service")
    public RequestResponsePact executeValidationErrorsLegalDeclarationPact(PactDslWithProvider builder)
        throws IOException, JSONException {
        // @formatter:off
        return builder
                .given("business service returns validation errors for invalid legal declaration")
                .uponReceiving("a request to POST invalid legal declaration doc ")
                .path("/businessDocument/generateLegalDeclarationPDF")
                .method("POST")
                .headers(AUTHORIZATION, SOME_AUTHORIZATION_TOKEN, serviceAuthorization,
                SOME_SERVICE_AUTHORIZATION_TOKEN)
                .body(contractTestUtils.createJsonObject("businessDocuments/invalidLegalDeclaration.json"))
                .matchHeader("Content-Type", "application/json")
                .willRespondWith()
                .status(400)
                .body(newJsonBody((o) -> {
                    o.stringValue("path", "/businessDocument/generateLegalDeclarationPDF");
                    o.numberType("timestamp");
                    o.array("errors", (a) -> {
                        addNonBlankError(a, "legalDeclaration", "deceased");
                    });
                    o.stringValue("error", "Bad Request");
                    o.numberValue("status", 400);
                    o.stringValue("message", "Validation failed for object='legalDeclaration'. Error count: 1");
                }).build())
                .toPact();
        // @formatter:on

    }


    @Pact(provider = "probate_businessservice_documents", consumer = "probate_orchestrator_service")
    public RequestResponsePact executeSuccessGetBulkScanCoversheetPact(PactDslWithProvider builder)
        throws IOException, JSONException {
        // @formatter:off
        return builder
                .given("business service returns bulk scan coversheet document with success")
                .uponReceiving("a request to POST a bulk scan coversheet doc")
                .path("/businessDocument/generateBulkScanCoverSheetPDF")
                .method("POST")
                .headers(AUTHORIZATION, SOME_AUTHORIZATION_TOKEN, serviceAuthorization,
                SOME_SERVICE_AUTHORIZATION_TOKEN)
                .body(contractTestUtils.createJsonObject("businessDocuments/bulkScanCoverSheet.json"))
                .matchHeader("Content-Type", "application/json")
                .willRespondWith()
                .status(200)
                .matchHeader("FormDataContent-Type", "application/octet-stream")
                .toPact();
        // @formatter:on
    }


    @Pact(provider = "probate_businessservice_documents", consumer = "probate_orchestrator_service")
    public RequestResponsePact executeValidationErrorsBulkScanCoversheetPact(PactDslWithProvider builder)
        throws IOException, JSONException {
        // @formatter:off
        return builder
                .given("business service returns validation errors for invalid bulk scan coversheet")
                .uponReceiving("a request to POST invalid bulk scan coversheet doc ")
                .path("/businessDocument/generateBulkScanCoverSheetPDF")
                .method("POST")
                .headers(AUTHORIZATION, SOME_AUTHORIZATION_TOKEN, serviceAuthorization,
                SOME_SERVICE_AUTHORIZATION_TOKEN)
                .body(contractTestUtils.createJsonObject("businessDocuments/invalidBulkScanCoverSheet.json"))
                .matchHeader("Content-Type", "application/json")
                .willRespondWith()
                .status(400)
                .body(newJsonBody((o) -> {
                    o.stringValue("path", "/businessDocument/generateBulkScanCoverSheetPDF");
                    o.numberType("timestamp");
                    o.array("errors", (a) -> {
                        addNonBlankError(a, "bulkScanCoverSheet", "caseReference");
                        addNonBlankError(a, "bulkScanCoverSheet", "submitAddress");
                    }
                    );

                    o.stringValue("error", "Bad Request");
                    o.numberValue("status", 400);
                    o.stringValue("message", "Validation failed for object='bulkScanCoverSheet'. Error count: 2");
                }).build())
                .toPact();
        // @formatter:on
    }

    @Test
    @PactTestFor(pactMethod = "executeSuccessGetCheckYoursAnswersSummaryPact")
    public void verifyExecuteSuccessGetCheckYoursAnswersSummaryPact() throws JSONException, IOException {
        businessServiceApi.generateCheckAnswersSummaryPdf(SOME_AUTHORIZATION_TOKEN, SOME_SERVICE_AUTHORIZATION_TOKEN,
            getCheckAnswersSummary("businessDocuments/checkAnswersSimpleSummary.json"));

    }


    @Test
    @PactTestFor(pactMethod = "executeValidationErrorsCheckYoursAnswersSummaryPact")
    public void verifyExecuteValidationErrorsCheckYoursAnswersSummaryPact() throws JSONException, IOException {
        assertThrows(UndeclaredThrowableException.class, () -> {
            businessServiceApi.generateCheckAnswersSummaryPdf(SOME_AUTHORIZATION_TOKEN,
                SOME_SERVICE_AUTHORIZATION_TOKEN, getCheckAnswersSummary(
                        "businessDocuments/invalidCheckAnswersSummary.json"));
        });

    }

    @Test
    @PactTestFor(pactMethod = "executeValidationErrorsLegalDeclarationPact")
    public void verifyExecuteValidationErrorsLegalDeclarationPact() throws JSONException, IOException {
        assertThrows(UndeclaredThrowableException.class, () -> {
            businessServiceApi.generateLegalDeclarationPDF(SOME_AUTHORIZATION_TOKEN,
                SOME_SERVICE_AUTHORIZATION_TOKEN,
                    getLegalDeclaration("businessDocuments/invalidLegalDeclaration.json"));
        });

    }

    @Test
    @PactTestFor(pactMethod = "executeSuccessGetLegalDeclarationPact")
    public void verifyExecuteSuccessGetLegalDeclarationPact() throws JSONException, IOException {
        businessServiceApi.generateLegalDeclarationPDF(SOME_AUTHORIZATION_TOKEN, SOME_SERVICE_AUTHORIZATION_TOKEN,
            getLegalDeclaration("businessDocuments/validLegalDeclaration.json"));

    }

    @Test
    @PactTestFor(pactMethod = "executeSuccessGetBulkScanCoversheetPact")
    public void verifyExecuteSuccessGetBulkScanCoversheetPact() throws JSONException, IOException {
        businessServiceApi.generateBulkScanCoverSheetPDF(SOME_AUTHORIZATION_TOKEN, SOME_SERVICE_AUTHORIZATION_TOKEN,
            getBulkScanCoverSheet("businessDocuments/bulkScanCoverSheet.json"));

    }

    @Test
    @PactTestFor(pactMethod = "executeValidationErrorsBulkScanCoversheetPact")
    public void verifyExecuteValidationErrorsBulkScanCoversheetPact() throws JSONException, IOException {
        assertThrows(UndeclaredThrowableException.class, () -> {
            businessServiceApi.generateBulkScanCoverSheetPDF(SOME_AUTHORIZATION_TOKEN,
                SOME_SERVICE_AUTHORIZATION_TOKEN,
                getBulkScanCoverSheet("businessDocuments/invalidBulkScanCoverSheet.json"));
        });

    }

    private CheckAnswersSummary getCheckAnswersSummary(String fileName) throws JSONException, IOException {
        objectMapper.configure(DeserializationFeature.UNWRAP_ROOT_VALUE, true);
        File file = getFile(fileName);
        CheckAnswersSummary businessDoc = objectMapper.readValue(file, CheckAnswersSummary.class);
        return businessDoc;
    }


    private LegalDeclaration getLegalDeclaration(String fileName) throws JSONException, IOException {
        objectMapper.configure(DeserializationFeature.UNWRAP_ROOT_VALUE, true);
        File file = getFile(fileName);
        LegalDeclaration businessDoc = objectMapper.readValue(file, LegalDeclaration.class);
        return businessDoc;
    }

    private BulkScanCoverSheet getBulkScanCoverSheet(String fileName) throws JSONException, IOException {
        objectMapper.configure(DeserializationFeature.UNWRAP_ROOT_VALUE, true);
        File file = getFile(fileName);
        BulkScanCoverSheet businessDoc = objectMapper.readValue(file, BulkScanCoverSheet.class);
        return businessDoc;
    }

    private File getFile(String fileName) throws FileNotFoundException {
        return ResourceUtils.getFile(this.getClass().getResource("/json/" + fileName));
    }

    private JSONObject createJsonObject(String fileName) throws JSONException, IOException {
        File file = getFile(fileName);
        String jsonString = new String(Files.readAllBytes(file.toPath()));
        return new JSONObject(jsonString);
    }

    private void addNonBlankError(LambdaDslJsonArray lambdaArray, String docType, String fieldName) {
        lambdaArray.object((e) -> e.stringType("field", fieldName)
                .booleanValue("bindingFailure", false)
                .stringValue("code", "NotBlank")
                .array("codes", (c) -> {
                    c.stringType("NotBlank." + docType + "." + fieldName)
                            .stringType("NotBlank." + fieldName)
                            .stringValue("NotBlank.java.lang.String")
                            .stringValue("NotBlank");
                })
                .stringValue("defaultMessage", "must not be blank")
                .or("rejectedValue","", PM.nullValue(), PM.stringType())
                .array("arguments", (d) -> {
                    d.object((f) -> f.array("codes", (g) -> {
                        g.stringType(docType + "." + fieldName)
                                .stringType(fieldName);
                    })
                                    .stringType("arguments",(null))
                                    .stringType("defaultMessage", fieldName)
                                    .stringType("code", fieldName)
                    );

                })
                .stringType("objectName", docType)
        );
    }
}
