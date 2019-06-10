package uk.gov.hmcts.probate.contract.tests;

import au.com.dius.pact.consumer.Pact;
import au.com.dius.pact.consumer.dsl.PM;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.model.RequestResponsePact;
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
    ObjectMapper objectMapper;

    public static final String SOME_AUTHORIZATION_TOKEN = "someAuthorizationToken";
    public static final String SOME_SERVICE_AUTHORIZATION_TOKEN = "someServiceAuthorizationToken";
    private String SERVICE_AUTHORIZATION = "ServiceAuthorization";

    private String checkAnswersSummaryBody = "{\"pageTitle\":\"\\n        Check your answers\\n    \",\"mainParagraph\":\"Check the information below carefully. This will form a record of your application for probate. It will also be stored as a public record, and will be able to be viewed online.\",\"sections\":[{\"title\":\"\\n        The will\\n    \",\"type\":\"heading-medium\",\"questionAndAnswers\":[{\"question\":\"Did the person who died leave a will?\",\"answers\":[\"Yes\"]},{\"question\":\"Do you have the original will?\",\"answers\":[\"Yes\"]},{\"question\":\"Were any updates (‘codicils’) made to the will?\",\"answers\":[\"Yes\"]},{\"question\":\"How many updates (‘codicils’) were made to the will?\",\"answers\":[\"4\"]},{\"question\":\"Do you have a death certificate?\",\"answers\":[\"Yes\"]},{\"question\":\"Are you named as an executor on the will?\",\"answers\":[\"Yes\"]},{\"question\":\"Are all the executors able to make their own decisions?\",\"answers\":[\"Yes\"]}]},{\"title\":\"Inheritance tax\",\"type\":\"heading-medium\",\"questionAndAnswers\":[{\"question\":\"Has an Inheritance Tax (IHT) form been filled in?\",\"answers\":[\"Yes\"]},{\"question\":\"How was the Inheritance Tax (IHT) form submitted?\",\"answers\":[\"Through the HMRC online service\"]},{\"question\":\"Inheritance Tax identifier (IHT)\",\"answers\":[\"iti54\"]},{\"question\":\"Gross value of the estate in £\",\"answers\":[\"10000\"]},{\"question\":\"Net value of the estate in £\",\"answers\":[\"9000\"]}]},{\"title\":\"The executors\",\"type\":\"heading-medium\",\"questionAndAnswers\":[{\"question\":\"How many past and present executors are named on the will and any updates (‘codicils’)?\",\"answers\":[\"1\"]}]},{\"title\":\"About you\",\"type\":\"heading-small\",\"questionAndAnswers\":[{\"question\":\"First name(s)\",\"answers\":[\"Jason\"]},{\"question\":\"Last name(s)\",\"answers\":[\"Smith\"]},{\"question\":\"Is your name ‘Jason Smith’ exactly what appears on the will?\",\"answers\":[\"Yes\"]},{\"question\":\"Phone number\",\"answers\":[\"01206822777\"]},{\"question\":\"What is your address?\",\"answers\":[\"Address Line 1\\nAddress Line 2\\nAddress Line3\\nPost Code\"]}]},{\"title\":\"About the person who died\",\"type\":\"heading-medium\",\"questionAndAnswers\":[{\"question\":\"First name(s)\",\"answers\":[\"Mike\"]},{\"question\":\"Last name(s)\",\"answers\":[\"Samuels\"]},{\"question\":\"Did Mike Samuels have assets in another name?\",\"answers\":[\"Yes\"]},{\"question\":\"Names used by the deceased\",\"answers\":[\"\\n                The old codger\\n            \"]},{\"question\":\"Did Mike Samuels get married or enter into a civil partnership after the latest codicil was signed?\",\"answers\":[\"Yes\"]},{\"question\":\"What was the date that they died?\",\"answers\":[\"20 September 2018\"]},{\"question\":\"What was their date of birth?\",\"answers\":[\"1 January 1966\"]},{\"question\":\"At the time of their death did the person who died:\",\"answers\":[\"live (domicile) permanently in England or Wales\"]},{\"question\":\"What was the permanent address at the time of their death?\",\"answers\":[\"Address Line 1\\nAddress Line 2\\nAddress Line3\\nPost Code\"]}]}]}";

    private String invalidCheckAnswersSummaryBody = "{\"pageTitle\":\"pageTitle\",\"mainParagraph\":null,\"sections\":[{\"title\":null,\"type\":\"heading-medium\",\"questionAndAnswers\":[{\"question\":\"question 1\",\"answers\":[\"not answered\"]}]}]}})\"";

    private String legalDeclarationBody = "{\"headers\":[\"In the High Court of Justice\",\"Family Division\",\"(Probate)\"],\"sections\":[{\"title\":\"Legal statement\",\"headingType\":\"large\",\"declarationItems\":[{\"title\":\"I, Jason Smith of An address\\nsomewhere \\nin\\nengland\\npost code\\n, make the following statement:\",\"values\":[]}]},{\"title\":\"The person who died\",\"headingType\":\"small\",\"declarationItems\":[{\"title\":\"Mike Samuels was born on 1 January 1977 and died on 20 October 2018, domiciled in England and Wales. \",\"values\":[]}]},{\"title\":\"The estate of the person who died\",\"headingType\":\"small\",\"declarationItems\":[{\"title\":\"The gross value for the estate amounts to £10000 and the net value for the estate amounts to £10000.\",\"values\":[]},{\"title\":\"To the best of my knowledge, information and belief, there was no land vested in Mike Samuels which was settled previously to the death (and not by the will) of Mike Samuels and which remained settled land notwithstanding such death.\",\"values\":[]}]},{\"title\":\"Executors applying for probate\",\"headingType\":\"small\",\"declarationItems\":[{\"title\":\"I am an executor named in the will or codicils as Jason Smith, and I am applying for probate.\",\"values\":[]},{\"title\":\"I will sign and send to the probate registry what I believe to be the true and original last will and testament and any codicils of Mike Samuels.\",\"values\":[]}]},{\"title\":\"Declaration\",\"headingType\":\"large\",\"declarationItems\":[{\"title\":\"I confirm that we will administer the estate of Mike Samuels, according to law. I will:\",\"values\":[\"collect the whole estate\",\"keep full details (an inventory) of the estate\",\"keep a full account of how the estate has been administered\"]},{\"title\":\"If the probate registry (court) asks me to do so, I will:\",\"values\":[\"provide the full details of the estate and how it has been administered\",\"return the grant of probate to the court\"]},{\"title\":\"I understand that:\",\"values\":[\"my application will be rejected if I do not answer any questions about the information I have given\",\"criminal proceedings for fraud may be brought against me if I am found to have been deliberately untruthful or dishonest\"]}]}],\"dateCreated\": \"31/10/2018, 10:15:44\", \"deceased\":\"Mike Samuels\"}";

    private String invalidLegalDeclarationBody = "{\"headers\":[\"header0\",\"header1\",\"header2\"],\"sections\":[{\"title\":\"title\",\"headingType\":\"large\",\"declarationItems\":[{\"title\":\"declaration title\",\"values\":[\"value0\",\"value1\",\"value2\"]}]}],\"dateCreated\":\"date and time\",\"deceased\":null}";

    private String bulkScanCoverSheetBody = "{\"title\":\"Cover Sheet\",\"applicantAddressIntro\":\"The applicants address\",\"applicantAddress\":\"20 White City\\nLondon\\nW12 7PD\",\"caseReferenceIntro\":\"Your unique reference\\nnumber is\",\"caseReference\":\"1542-9021-4510-0350\",\"submitAddressIntro\":\"Please send this cover sheet along with your document(s) to the address shown below\",\"submitAddress\":\"Divorce Service\\nPO BOX 123\\nExela BSP Services\\nHarlow\\nCM19 5QS\"}";

    private String invalidBulkScanCoverSheetBody = "{\"title\":\"Download Cover Sheet\",\"applicantAddressIntro\":\"Your address\",\"applicantAddress\":\"addressLine1\",\"caseReferenceIntro\":\"Your unique reference\\nnumber is\",\"caseReference\":\"\",\"submitAddressIntro\":\"Please send this cover sheet along with your document(s) to the address shown below\",\"submitAddress\":null}";


    @BeforeEach
    public void setUpTest() throws InterruptedException{
        Thread.sleep(2000);
    }

    @Pact(state = "business service returns check your answers document request with success", provider = "probate_businessservice_documents", consumer = "probate_orchestrator_service")
    public RequestResponsePact executeSuccessGetCheckYoursAnswersSummaryPact(PactDslWithProvider builder) throws IOException, JSONException {
        // @formatter:off
        return builder
                .given("business service returns check your answers document with success")
                .uponReceiving("a request to POST check your answers doc ")
                .path("/businessDocument/generateCheckAnswersSummaryPDF")
                .method("POST")
                .headers(AUTHORIZATION, SOME_AUTHORIZATION_TOKEN, SERVICE_AUTHORIZATION, SOME_SERVICE_AUTHORIZATION_TOKEN)
                .body(checkAnswersSummaryBody)
                .willRespondWith()
                .status(200)
                .matchHeader("Content-Type", "application/octet-stream")
                .toPact();
        // @formatter:on
    }


    @Pact(state = "business service returns validation errors for invalid check answers summary", provider = "probate_businessservice_documents", consumer = "probate_orchestrator_service")
    public RequestResponsePact executeValidationErrorsCheckYoursAnswersSummaryPact(PactDslWithProvider builder) throws IOException, JSONException {
        // @formatter:off
        return builder
                .given("business service returns validation errors for invalid check answers summary")
                .uponReceiving("a request to POST invalid check your answers doc ")
                .path("/businessDocument/generateCheckAnswersSummaryPDF")
                .method("POST")
                .headers(AUTHORIZATION, SOME_AUTHORIZATION_TOKEN, SERVICE_AUTHORIZATION, SOME_SERVICE_AUTHORIZATION_TOKEN)
                .body(invalidCheckAnswersSummaryBody)
                .willRespondWith()
                .status(400)
                .matchHeader("Content-Type", "application/json;charset=UTF-8")
                .body(newJsonBody((o) -> {
                    o.stringValue("path", "/businessDocument/generateCheckAnswersSummaryPDF");
                    o.numberType("timestamp");
                    o.array("errors", (a) -> {
                                addNonBlankError(a, "checkAnswersSummary", "mainParagraph");

                            }

                    );
                    o.stringValue("error", "Bad Request");
                    o.numberValue("status", 400);
                    o.stringValue("message", "Validation failed for object='checkAnswersSummary'. Error count: 1");
                }).build())
                .toPact();

        // @formatter:on
    }


    @Pact(state = "business service returns legal declaration document request with success", provider = "probate_businessservice_documents", consumer = "probate_orchestrator_service")
    public RequestResponsePact executeSuccessGetLegalDeclarationPact(PactDslWithProvider builder) throws IOException, JSONException {
        // @formatter:off
        return builder
                .given("business service returns legal declaration document with success")
                .uponReceiving("a request to POST a legal declaration doc")
                .path("/businessDocument/generateLegalDeclarationPDF")
                .method("POST")
                .headers(AUTHORIZATION, SOME_AUTHORIZATION_TOKEN, SERVICE_AUTHORIZATION, SOME_SERVICE_AUTHORIZATION_TOKEN)
                .body(legalDeclarationBody)
                .willRespondWith()
                .status(200)
                .matchHeader("Content-Type", "application/octet-stream")
                .toPact();
        // @formatter:on
    }


    @Pact(state = "business service returns validation errors for invalid legal declaration", provider = "probate_businessservice_documents", consumer = "probate_orchestrator_service")
    public RequestResponsePact executeValidationErrorsLegalDeclarationPact(PactDslWithProvider builder) throws IOException, JSONException {
        // @formatter:off
        return builder
                .given("business service returns validation errors for invalid legal declaration")
                .uponReceiving("a request to POST invalid legal declaration doc ")
                .path("/businessDocument/generateLegalDeclarationPDF")
                .method("POST")
                .headers(AUTHORIZATION, SOME_AUTHORIZATION_TOKEN, SERVICE_AUTHORIZATION, SOME_SERVICE_AUTHORIZATION_TOKEN)
                .body(invalidLegalDeclarationBody)
                .willRespondWith()
                .status(400)
                .matchHeader("Content-Type", "application/json;charset=UTF-8")
                .body(newJsonBody((o) -> {
                    o.stringValue("path", "/businessDocument/generateLegalDeclarationPDF");
                    o.numberType("timestamp");
                    o.array("errors", (a) -> {
                                addNonBlankError(a, "legalDeclaration", "deceased");

                            }
                    );


                    o.stringValue("error", "Bad Request");
                    o.numberValue("status", 400);
                    o.stringValue("message", "Validation failed for object='legalDeclaration'. Error count: 1");
                }).build())
                .toPact();
        // @formatter:on

    }


    @Pact(state = "business service returns bulk scan coversheet document request with success", provider = "probate_businessservice_documents", consumer = "probate_orchestrator_service")
    public RequestResponsePact executeSuccessGetBulkScanCoversheetPact(PactDslWithProvider builder) throws IOException, JSONException {
        // @formatter:off
        return builder
                .given("business service returns bulk scan coversheet document with success")
                .uponReceiving("a request to POST a bulk scan coversheet doc")
                .path("/businessDocument/generateBulkScanCoverSheetPDF")
                .method("POST")
                .headers(AUTHORIZATION, SOME_AUTHORIZATION_TOKEN, SERVICE_AUTHORIZATION, SOME_SERVICE_AUTHORIZATION_TOKEN)
                .body(bulkScanCoverSheetBody)
                .willRespondWith()
                .status(200)
                .matchHeader("Content-Type", "application/octet-stream")
                .toPact();
        // @formatter:on
    }


    @Pact(state = "business service returns validation errors for invalid bulk scan coversheet", provider = "probate_businessservice_documents", consumer = "probate_orchestrator_service")
    public RequestResponsePact executeValidationErrorsBulkScanCoversheetPact(PactDslWithProvider builder) throws IOException, JSONException {
        // @formatter:off
        return builder
                .given("business service returns validation errors for invalid bulk scan coversheet")
                .uponReceiving("a request to POST invalid bulk scan coversheet doc ")
                .path("/businessDocument/generateBulkScanCoverSheetPDF")
                .method("POST")
                .headers(AUTHORIZATION, SOME_AUTHORIZATION_TOKEN, SERVICE_AUTHORIZATION, SOME_SERVICE_AUTHORIZATION_TOKEN)
                .body(invalidBulkScanCoverSheetBody)
                .willRespondWith()
                .status(400)
                .matchHeader("Content-Type", "application/json;charset=UTF-8")
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
        businessServiceApi.generateCheckAnswersSummaryPdf(SOME_AUTHORIZATION_TOKEN, SOME_SERVICE_AUTHORIZATION_TOKEN, getCheckAnswersSummary("businessDocuments/checkAnswersSimpleSummary.json"));

    }


    @Test
    @PactTestFor(pactMethod = "executeValidationErrorsCheckYoursAnswersSummaryPact")
    public void verifyExecuteValidationErrorsCheckYoursAnswersSummaryPact() throws JSONException, IOException {
        assertThrows(UndeclaredThrowableException.class, () -> {
            businessServiceApi.generateCheckAnswersSummaryPdf(SOME_AUTHORIZATION_TOKEN, SOME_SERVICE_AUTHORIZATION_TOKEN, getCheckAnswersSummary("businessDocuments/invalidCheckAnswersSummary.json"));
        });

    }

    @Test
    @PactTestFor(pactMethod = "executeValidationErrorsLegalDeclarationPact")
    public void verifyExecuteValidationErrorsLegalDeclarationPact() throws JSONException, IOException {
        assertThrows(UndeclaredThrowableException.class, () -> {
            businessServiceApi.generateLegalDeclarationPDF(SOME_AUTHORIZATION_TOKEN, SOME_SERVICE_AUTHORIZATION_TOKEN, getLegalDeclaration("businessDocuments/invalidLegalDeclaration.json"));
        });

    }

    @Test
    @PactTestFor(pactMethod = "executeSuccessGetLegalDeclarationPact")
    public void verifyExecuteSuccessGetLegalDeclarationPact() throws JSONException, IOException {
        businessServiceApi.generateLegalDeclarationPDF(SOME_AUTHORIZATION_TOKEN, SOME_SERVICE_AUTHORIZATION_TOKEN, getLegalDeclaration("businessDocuments/validLegalDeclaration.json"));

    }

    @Test
    @PactTestFor(pactMethod = "executeSuccessGetBulkScanCoversheetPact")
    public void verifyExecuteSuccessGetBulkScanCoversheetPact() throws JSONException, IOException {
        businessServiceApi.generateBulkScanCoverSheetPDF(SOME_AUTHORIZATION_TOKEN, SOME_SERVICE_AUTHORIZATION_TOKEN, getBulkScanCoverSheet("businessDocuments/bulkScanCoverSheet.json"));

    }

    @Test
    @PactTestFor(pactMethod = "executeValidationErrorsBulkScanCoversheetPact")
    public void verifyExecuteValidationErrorsBulkScanCoversheetPact() throws JSONException, IOException {
        assertThrows(UndeclaredThrowableException.class, () -> {
            businessServiceApi.generateBulkScanCoverSheetPDF(SOME_AUTHORIZATION_TOKEN, SOME_SERVICE_AUTHORIZATION_TOKEN, getBulkScanCoverSheet("businessDocuments/invalidBulkScanCoverSheet.json"));
        });

    }

    private CheckAnswersSummary getCheckAnswersSummary() throws JSONException, IOException {
        CheckAnswersSummary businessDoc = objectMapper.readValue(checkAnswersSummaryBody, CheckAnswersSummary.class);
        return businessDoc;
    }


    private CheckAnswersSummary getCheckAnswersSummary(String fileName) throws JSONException, IOException {
        File file = getFile(fileName);
        CheckAnswersSummary businessDoc = objectMapper.readValue(file, CheckAnswersSummary.class);
        return businessDoc;
    }


    private LegalDeclaration getLegalDeclaration(String fileName) throws JSONException, IOException {
        File file = getFile(fileName);
        LegalDeclaration businessDoc = objectMapper.readValue(file, LegalDeclaration.class);
        return businessDoc;
    }

    private BulkScanCoverSheet getBulkScanCoverSheet(String fileName) throws JSONException, IOException {
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
