package uk.gov.hmcts.probate.client.submit;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import uk.gov.hmcts.reform.probate.model.cases.ProbateCaseDetails;
import uk.gov.hmcts.reform.probate.model.cases.ProbatePaymentDetails;
import uk.gov.hmcts.reform.probate.model.cases.SubmitResult;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@FeignClient(
    name = "submit-service-api",
    url = "${submit.service.api.url}",
    configuration = SubmitServiceConfiguration.class
)
public interface SubmitServiceApi {



    @GetMapping(
            value = "/cases/invitation/{invitationId}",
            headers = CONTENT_TYPE + "=" + APPLICATION_JSON_VALUE
    )
    ProbateCaseDetails getCaseByInvitationId(
            @RequestHeader(AUTHORIZATION) String authorisation,
            @RequestHeader(SubmitServiceConfiguration.SERVICE_AUTHORIZATION) String serviceAuthorization,
            @PathVariable(SubmitServiceConfiguration.INVITATION_ID) String invitationId,
            @RequestParam("caseType") String caseType
    );

    @GetMapping(
        value = "/cases/{applicationId}",
        headers = CONTENT_TYPE + "=" + APPLICATION_JSON_VALUE
    )
    ProbateCaseDetails getCase(
        @RequestHeader(AUTHORIZATION) String authorisation,
        @RequestHeader(SubmitServiceConfiguration.SERVICE_AUTHORIZATION) String serviceAuthorization,
        @PathVariable(SubmitServiceConfiguration.APPLICATION_ID) String applicationId,
        @RequestParam("caseType") String caseType
    );

    @PostMapping(
        value = "/cases/{applicationId}",
        headers = CONTENT_TYPE + "=" + APPLICATION_JSON_VALUE
    )
    ProbateCaseDetails saveCase(
        @RequestHeader(AUTHORIZATION) String authorisation,
        @RequestHeader(SubmitServiceConfiguration.SERVICE_AUTHORIZATION) String serviceAuthorization,
        @PathVariable(SubmitServiceConfiguration.APPLICATION_ID) String applicationId,
        @RequestBody ProbateCaseDetails probateCaseDetails
    );

    @PutMapping(
        value = "/cases/{applicationId}/validations",
        headers = CONTENT_TYPE + "=" + APPLICATION_JSON_VALUE
    )
    ProbateCaseDetails validate(
        @RequestHeader(AUTHORIZATION) String authorisation,
        @RequestHeader(SubmitServiceConfiguration.SERVICE_AUTHORIZATION) String serviceAuthorization,
        @PathVariable(SubmitServiceConfiguration.APPLICATION_ID) String applicationId,
        @RequestParam("caseType") String caseType
    );

    @GetMapping(
        value = "/cases",
        headers = CONTENT_TYPE + "=" + APPLICATION_JSON_VALUE
    )
    ProbateCaseDetails getCaseById(
        @RequestHeader(AUTHORIZATION) String authorisation,
        @RequestHeader(SubmitServiceConfiguration.SERVICE_AUTHORIZATION) String serviceAuthorization,
        @RequestParam(SubmitServiceConfiguration.CASE_ID) String caseId
    );

    @PostMapping(
        value = "/drafts/{applicationId}",
        headers = CONTENT_TYPE + "=" + APPLICATION_JSON_VALUE
    )
    ProbateCaseDetails saveDraft(
        @RequestHeader(AUTHORIZATION) String authorisation,
        @RequestHeader(SubmitServiceConfiguration.SERVICE_AUTHORIZATION) String serviceAuthorization,
        @PathVariable(SubmitServiceConfiguration.APPLICATION_ID) String applicationId,
        @RequestBody ProbateCaseDetails probateCaseDetails
    );

    @PostMapping(
        value = "/submissions/{applicationId}",
        headers = CONTENT_TYPE + "=" + APPLICATION_JSON_VALUE
    )
    SubmitResult submit(
        @RequestHeader(AUTHORIZATION) String authorisation,
        @RequestHeader(SubmitServiceConfiguration.SERVICE_AUTHORIZATION) String serviceAuthorization,
        @PathVariable(SubmitServiceConfiguration.APPLICATION_ID) String applicationId,
        @RequestBody ProbateCaseDetails probateCaseDetails
    );

    @PostMapping (
        value = "/submissions/update/{applicationId}",
        headers = CONTENT_TYPE + "=" + APPLICATION_JSON_VALUE
    )
    SubmitResult update(
        @RequestHeader(AUTHORIZATION) String authorisation,
        @RequestHeader(SubmitServiceConfiguration.SERVICE_AUTHORIZATION) String serviceAuthorization,
        @PathVariable(SubmitServiceConfiguration.APPLICATION_ID) String applicationId,
        @RequestBody ProbateCaseDetails probateCaseDetails
    );

    @PostMapping(
        value = "/payments/{applicationId}",
        headers = CONTENT_TYPE + "=" + APPLICATION_JSON_VALUE
    )
    ProbateCaseDetails updatePayments(
        @RequestHeader(AUTHORIZATION) String authorisation,
        @RequestHeader(SubmitServiceConfiguration.SERVICE_AUTHORIZATION) String serviceAuthorization,
        @PathVariable(SubmitServiceConfiguration.APPLICATION_ID) String applicationId,
        @RequestBody ProbatePaymentDetails probatePaymentDetails
    );

    @PostMapping(
        value = "/payments/{applicationId}/cases",
        headers = CONTENT_TYPE + "=" + APPLICATION_JSON_VALUE
    )
    ProbateCaseDetails createCase(
        @RequestHeader(AUTHORIZATION) String authorisation,
        @RequestHeader(SubmitServiceConfiguration.SERVICE_AUTHORIZATION) String serviceAuthorization,
        @PathVariable(SubmitServiceConfiguration.APPLICATION_ID) String applicationId,
        @RequestBody ProbateCaseDetails probateCaseDetails
    );

    @PostMapping(
        value = "/ccd-case-payments/{caseId}",
        headers = CONTENT_TYPE + "=" + APPLICATION_JSON_VALUE
    )
    ProbateCaseDetails updatePaymentsByCaseId(
        @RequestHeader(AUTHORIZATION) String authorisation,
        @RequestHeader(SubmitServiceConfiguration.SERVICE_AUTHORIZATION) String serviceAuthorization,
        @PathVariable("caseId") String caseId,
        @RequestBody ProbatePaymentDetails probatePaymentDetails
    );

    @PostMapping(
        value = "/ccd-case-update/{caseId}",
        headers = CONTENT_TYPE + "=" + APPLICATION_JSON_VALUE
    )
    ProbateCaseDetails updateByCaseId(
        @RequestHeader(AUTHORIZATION) String authorisation,
        @RequestHeader(SubmitServiceConfiguration.SERVICE_AUTHORIZATION) String serviceAuthorization,
        @PathVariable(SubmitServiceConfiguration.CASE_ID) String caseId,
        @RequestBody ProbateCaseDetails probateCaseDetails
    );
}
