//package uk.gov.hmcts.probate.contract.tests;
//
//import au.com.dius.pact.consumer.Pact;
//import au.com.dius.pact.consumer.dsl.DslPart;
//import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
//import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
//import au.com.dius.pact.consumer.junit5.PactTestFor;
//import au.com.dius.pact.model.RequestResponsePact;
//import org.json.JSONException;
//import org.junit.Ignore;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//import uk.gov.hmcts.probate.client.submit.SubmitServiceApi;
//import uk.gov.hmcts.reform.probate.model.cases.CaseType;
//import uk.gov.hmcts.reform.probate.model.cases.ProbateCaseDetails;
//import uk.gov.hmcts.reform.probate.model.client.ApiClientException;
//
//import java.io.IOException;
//
//import static io.pactfoundation.consumer.dsl.LambdaDsl.newJsonBody;
//import static org.hamcrest.CoreMatchers.*;
//import static org.hamcrest.MatcherAssert.*;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.springframework.http.HttpHeaders.AUTHORIZATION;
//
//@ExtendWith(PactConsumerTestExt.class)
//@ExtendWith(SpringExtension.class)
//@PactTestFor(providerName = "probate_submitService", port = "8889")
//@SpringBootTest({
//        // overriding provider address
//        "probate_submitservice.ribbon.listOfServers: localhost:8889",
//        "submit.service.api.url : localhost:8889",
//        "core_case_data.api.url : localhost:8889"
//})
//public class SubmitServiceCaseDetailsConsumerTest {
//
//    public static final String SOME_AUTHORIZATION_TOKEN = "someAuthorizationToken";
//    public static final String SOME_SERVICE_AUTHORIZATION_TOKEN = "someServiceAuthorizationToken";
//    public static final String SOMEEMAILADDRESS_HOST_COM = "jsnow@bbc.co.uk";
//    public static final String CASE_ID = "12323213323";
//    public static final String SOME_INVITE_ID = "654321";
//    public static final String REGEX_DATE = "^((19|2[0-9])[0-9]{2})-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01])$";
//    @Autowired
//    private SubmitServiceApi submitServiceApi;
//    @Autowired
//    ContractTestUtils contractTestUtils;
//
//    private String SERVICE_AUTHORIZATION = "ServiceAuthorization";
//
//
//    @BeforeEach
//    public void setUpTest() throws InterruptedException {
//        Thread.sleep(2000);
//    }
//
//    @Pact(state = "provider returns casedata with success", provider = "probate_submitService_cases", consumer = "probate_orchestratorService")
//    public RequestResponsePact executeSuccessGetCaseDataPact(PactDslWithProvider builder) throws IOException, JSONException {
//        // @formatter:off
//        return builder
//                .given("provider returns casedata with success")
//                .uponReceiving("a request to GET casedata with success")
//                .path("/cases/" + SOMEEMAILADDRESS_HOST_COM)
//                .method("GET")
//                .matchQuery("caseType", CaseType.GRANT_OF_REPRESENTATION.toString())
//                .headers(AUTHORIZATION, SOME_AUTHORIZATION_TOKEN, SERVICE_AUTHORIZATION, SOME_SERVICE_AUTHORIZATION_TOKEN)
//                .willRespondWith()
//                .status(200)
//                .matchHeader("FormDataContent-Type", "application/json;charset=UTF-8")
//                //.body(contractTestUtils.createJsonObject("intestacyGrantOfRepresentation_full.json"))
//                .body(buildCaseDataPactDsl(SOMEEMAILADDRESS_HOST_COM, false))
//                .toPact();
//        // @formatter:on
//    }
//
//
//    private DslPart buildCaseDataPactDsl(String someemailaddressHostCom, boolean withExecutors) {
//        return newJsonBody((o) -> {
//            o.object("caseData", (cd) -> {
//                        cd.stringValue("type", CaseType.GRANT_OF_REPRESENTATION.getName())
//                                .stringValue("applicationType", "Personal")
//                                .stringType("primaryApplicantForenames", "Jon")
//                                .stringType("primaryApplicantSurname", "Snow")
//                                .stringMatcher("primaryApplicantAddressFound",
//                                        "Yes|No", "Yes")
//                                .stringMatcher("primaryApplicantPhoneNumber", "[0-9]+", "123455678")
//                                .stringMatcher("primaryApplicantRelationshipToDeceased", "partner|child|sibling|partner|parent|adoptedChild|other", "adoptedChild")
//                                .stringMatcher("primaryApplicantAdoptionInEnglandOrWales", "(Yes|No)", "Yes")
//                                .stringValue("primaryApplicantEmailAddress", someemailaddressHostCom)
//                                .object("primaryApplicantAddress", (address) ->
//                                        address.stringType("AddressLine1", "Pret a Manger")
//                                                .stringType("AddressLine2", "St. Georges Hospital")
//                                                .stringType("PostTown", "London")
//                                                .stringType("PostCode", "SW17 0QT")
//                                )
//                                .stringType("deceasedForenames", "Ned")
//                                .stringType("deceasedSurname", "Stark")
//                                .stringMatcher("deceasedDateOfBirth", REGEX_DATE, "1930-01-01")
//                                .stringMatcher("deceasedDateOfDeath", REGEX_DATE, "2018-01-01")
//                                .object("deceasedAddress", (address) ->
//                                        address.stringType("AddressLine1", "Winterfell")
//                                                .stringType("AddressLine2", "Westeros")
//                                                .stringType("PostTown", "London")
//                                                .stringType("PostCode", "SW17 0QT")
//                                )
//                                .stringMatcher("deceasedAddressFound", "Yes|No", "Yes")
//                                .stringMatcher("deceasedAnyOtherNames", "Yes|No", "No")
//                                .minArrayLike("deceasedAliasNameList", 0, 2,
//                                        alias -> alias
//                                                .object("value", (value) ->
//                                                        value.stringType("Forenames", "King")
//                                                                .stringType("LastName", "North")
//                                                ))
//                                .stringMatcher("deceasedMartialStatus", "marriedCivilPartnership|divorcedCivilPartnership|widowed|judicially|neverMarried")
//                                .stringMatcher("deceasedDivorcedInEnglandOrWales", "Yes|No", "No")
//                                .stringMatcher("deceasedSpouseNotApplyingReason", "renunciated|mentallyIncapable|other")
//                                .stringMatcher("deceasedOtherChildren", "Yes|No", "Yes")
//                                .stringMatcher("childrenOverEighteenSurvived", "Yes|No", "Yes")
//                                .stringMatcher("childrenDied", "Yes|No", "No")
//                                .stringMatcher("grandChildrenSurvivedUnderEighteen", "Yes|No", "No")
//                                .stringMatcher("deceasedAnyChildren", "Yes|No", "No")
//                                .stringMatcher("deceasedHasAssetsOutsideUK", "Yes|No", "Yes")
//                                .stringMatcher("deceasedAnyChildren", "Yes|No", "No")
//                                .stringMatcher("ihtFormId", "IHT205|IHT207|IHT400421", "IHT205")
//                                .stringMatcher("ihtFormCompletedOnline", "Yes|No", "No")
//                                .stringType("assetsOverseasNetValue", "10050")
//                                .stringType("ihtGrossValue", "100000")
//                                .stringType("ihtNetValue", "100000")
//                                .stringType("ihtReferenceNumber", "GOT123456")
//                                .stringMatcher("declarationCheckbox", "Yes|No", "No")
//                                .numberType("outsideUKGrantCopies", 6)
//                                .numberType("extraCopiesOfGrant", 3)
//                                .stringType("uploadDocumentUrl", "http://document-management/document/12345")
//                                .stringMatcher("registryLocation", "Oxford|Manchester|Birmingham|Leeds|Liverpool|Brighton|Cardiff|London|Winchester|Newcastle|ctsc", "Oxford");
//
//                        if(withExecutors){
//                            cd.minArrayLike("executorsApplying", 0, 2,
//                                    executorApplying -> executorApplying
//                                            .object("value", (value) ->
//                                                    value.stringType("applyingExecutorName", "Jon Snow")
//                                                            .stringMatcher("applyingExecutorPhoneNumber", "[0-9]+", "07981898999")
//                                                            .stringMatcher("applyingExecutorAgreed", "Yes|No", "Yes")
//                                                            .stringType("applyingExecutorEmail", "address@email.com")
//                                                            .stringType("applyingExecutorInvitationId", "54321")
//                                                            .stringType("applyingExecutorLeadName", "Graham Garderner")
//                                                            .object("applyingExecutorAddress", (address) ->
//                                                                    address.stringType("AddressLine1", "Winterfell")
//                                                                            .stringType("AddressLine2", "Westeros")
//                                                                            .stringType("PostTown", "London")
//                                                                            .stringType("PostCode", "SW17 0QT")
//                                                            ).stringType("applyingExecutorOtherNames", "Graham Poll")
//                                                            .stringType("applyingExecutorOtherNamesReason", "Divorce")
//                                            ));
//                        }
//
//                    }
//            ).object("caseInfo", (ci) ->
//                    ci.stringType("caseId", "12323213323")
//                            .stringMatcher("state", "Draft|PaAppCreated|CaseCreated", "Draft")
//            );
//        }).build();
//    }
//
//
//
//    @Pact(state = "provider returns casedata for invite id with success", provider = "probate_submitService_cases", consumer = "probate_orchestratorService")
//    public RequestResponsePact executeSuccessGetCaseDataByInviteIdPact(PactDslWithProvider builder) throws IOException, JSONException {
//        // @formatter:off
//
//        return builder
//                .given("an invite has been sent for a case")
//                .uponReceiving("a request to GET casedata by inviteId")
//                .path("/cases/invitation/" + SOME_INVITE_ID)
//                .method("GET")
//                .matchQuery("caseType", CaseType.GRANT_OF_REPRESENTATION.name())
//                .headers(AUTHORIZATION, SOME_AUTHORIZATION_TOKEN, SERVICE_AUTHORIZATION, SOME_SERVICE_AUTHORIZATION_TOKEN)
//                .willRespondWith()
//                .status(200)
//                .matchHeader("FormDataContent-Type", "application/json;charset=UTF-8")
//                .body(buildCaseDataPactDsl(SOMEEMAILADDRESS_HOST_COM, true))
//                .toPact();
//        // @formatter:on
//    }
//
//    @Pact(state = "provider returns casedata not found", provider = "probate_submitService_cases", consumer = "probate_orchestratorService")
//    public RequestResponsePact executeNotFoundGetCaseDataPact(PactDslWithProvider builder) {
//        // @formatter:off
//
//        return builder
//                .given("provider returns casedata not found")
//                .uponReceiving("a request to GET casedata not found")
//                .path("/cases/" + SOMEEMAILADDRESS_HOST_COM)
//                .method("GET")
//                .matchQuery("caseType", CaseType.GRANT_OF_REPRESENTATION.toString())
//                .headers(AUTHORIZATION, SOME_AUTHORIZATION_TOKEN, SERVICE_AUTHORIZATION, SOME_SERVICE_AUTHORIZATION_TOKEN)
//                .willRespondWith()
//                .status(404)
//                .toPact();
//        // @formatter:on
//    }
//
//    @Pact(state = "provider POSTS draft casedata with success", provider = "probate_submitService_drafts", consumer = "probate_orchestratorService")
//    public RequestResponsePact executeSuccessPostDraftCaseDataPact(PactDslWithProvider builder) throws IOException, JSONException {
//        // @formatter:off
//
//        return builder
//                .given("provider POSTS draft casedata with success")
//                .uponReceiving("a request to POST draft casedata")
//                .path("/drafts/" + SOMEEMAILADDRESS_HOST_COM)
//                .method("POST")
//                .headers(AUTHORIZATION, SOME_AUTHORIZATION_TOKEN, SERVICE_AUTHORIZATION, SOME_SERVICE_AUTHORIZATION_TOKEN)
//                .matchHeader("FormDataContent-Type", "application/json")
//                .body(contractTestUtils.createJsonObject("intestacyGrantOfRepresentation_full.json"))
//                .willRespondWith()
//                .status(200)
//                .matchHeader("FormDataContent-Type", "application/json;charset=UTF-8")
//                .body(buildCaseDataPactDsl(SOMEEMAILADDRESS_HOST_COM, false))
//                .toPact();
//        // @formatter:on
//    }
//
//
//    @Pact(state = "provider POSTS partial draft casedata with success", provider = "probate_submitService_drafts", consumer = "probate_orchestratorService")
//    public RequestResponsePact executeSuccessPostPartialDraftCaseDataPact(PactDslWithProvider builder) throws IOException, JSONException {
//        // @formatter:off
//
//        return builder
//                .given("provider POSTS partial draft casedata with success")
//                .uponReceiving("a request to POST partial draft casedata")
//                .path("/drafts/" + SOMEEMAILADDRESS_HOST_COM)
//                .method("POST")
//                .headers(AUTHORIZATION, SOME_AUTHORIZATION_TOKEN, SERVICE_AUTHORIZATION, SOME_SERVICE_AUTHORIZATION_TOKEN)
//                .matchHeader("FormDataContent-Type", "application/json")
//                .body(contractTestUtils.createJsonObject("intestacyGrantOfRepresentation_partial_draft.json"))
//                .willRespondWith()
//                .status(200)
//                .matchHeader("FormDataContent-Type", "application/json;charset=UTF-8")
//                .body(contractTestUtils.createJsonObject("intestacyGrantOfRepresentation_partial_draft.json"))
//                .toPact();
//        // @formatter:on
//    }
//
//    @Test
//    @PactTestFor(pactMethod = "executeSuccessGetCaseDataPact")
//    public void verifyExecuteSuccessGetCaseDataPact() {
//
//        ProbateCaseDetails caseDetails = submitServiceApi.getCase(SOME_AUTHORIZATION_TOKEN, SOME_SERVICE_AUTHORIZATION_TOKEN, SOMEEMAILADDRESS_HOST_COM, CaseType.GRANT_OF_REPRESENTATION.toString());
//        assertThat(caseDetails.getCaseInfo().getCaseId(), equalTo(CASE_ID));
//    }
//
//
//    @Test
//    @PactTestFor(pactMethod = "executeSuccessGetCaseDataByInviteIdPact")
//    public void verifyExecuteSuccessGetCaseDataByInviteIdPact() {
//
//        ProbateCaseDetails caseDetails = submitServiceApi.getCaseByInvitationId(SOME_AUTHORIZATION_TOKEN, SOME_SERVICE_AUTHORIZATION_TOKEN, SOME_INVITE_ID, CaseType.GRANT_OF_REPRESENTATION.name());
//        assertThat(caseDetails.getCaseInfo().getCaseId(), equalTo(CASE_ID));
//    }
//
//    @Test
//    @PactTestFor(pactMethod = "executeSuccessPostPartialDraftCaseDataPact")
//    public void verifyExecuteSuccessPostPartialDraftCaseDataPact() throws IOException, JSONException {
//
//        ProbateCaseDetails caseDetails = submitServiceApi.saveCase(SOME_AUTHORIZATION_TOKEN, SOME_SERVICE_AUTHORIZATION_TOKEN, SOMEEMAILADDRESS_HOST_COM, contractTestUtils.getProbateCaseDetails("intestacyGrantOfRepresentation_partial_draft.json"));
//        assertThat(caseDetails.getCaseInfo().getCaseId(), equalTo(CASE_ID));
//    }
//
//    @Test
//    @PactTestFor(pactMethod = "executeSuccessPostDraftCaseDataPact")
//    public void verifyExecuteSuccessPostDraftCaseDataPact() throws IOException, JSONException {
//
//        ProbateCaseDetails caseDetails = submitServiceApi.saveCase(SOME_AUTHORIZATION_TOKEN, SOME_SERVICE_AUTHORIZATION_TOKEN, SOMEEMAILADDRESS_HOST_COM, contractTestUtils.getProbateCaseDetails("intestacyGrantOfRepresentation_full.json"));
//        assertThat(caseDetails.getCaseInfo().getCaseId(), equalTo(CASE_ID));
//    }
//
//    @Test
//    @PactTestFor(pactMethod = "executeNotFoundGetCaseDataPact")
//    public void verifyExecuteNotFoundGetCaseDataPact() {
//        assertThrows(ApiClientException.class, () -> {
//            submitServiceApi.getCase(SOME_AUTHORIZATION_TOKEN, SOME_SERVICE_AUTHORIZATION_TOKEN, SOMEEMAILADDRESS_HOST_COM, CaseType.GRANT_OF_REPRESENTATION.toString());
//        });
//
//    }
//
//}
