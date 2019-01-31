package uk.gov.hmcts.probate.client;

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
        url = "${submit-service.api.url}",
        configuration = SubmitServiceConfiguration.class
)
public interface SubmitServiceApi {

    @GetMapping(
            value = "/cases/{applicantEmail}",
            headers = CONTENT_TYPE + "=" + APPLICATION_JSON_VALUE
    )
    ProbateCaseDetails getCase(
            @RequestHeader(AUTHORIZATION) String authorisation,
            @RequestHeader(SubmitServiceConfiguration.SERVICE_AUTHORIZATION) String serviceAuthorization,
            @PathVariable(SubmitServiceConfiguration.APPLICANT_EMAIL) String applicantEmail,
            @RequestParam("caseType") String caseType
    );

    @PostMapping(
            value = "/drafts/{applicantEmail}",
            headers = CONTENT_TYPE + "=" + APPLICATION_JSON_VALUE
    )
    ProbateCaseDetails saveDraft(
            @RequestHeader(AUTHORIZATION) String authorisation,
            @RequestHeader(SubmitServiceConfiguration.SERVICE_AUTHORIZATION) String serviceAuthorization,
            @PathVariable(SubmitServiceConfiguration.APPLICANT_EMAIL) String applicantEmail,
            @RequestBody ProbateCaseDetails probateCaseDetails
    );

    @PostMapping(
            value = "/submissions/{applicantEmail}",
            headers = CONTENT_TYPE + "=" + APPLICATION_JSON_VALUE
    )
    SubmitResult submit(
            @RequestHeader(AUTHORIZATION) String authorisation,
            @RequestHeader(SubmitServiceConfiguration.SERVICE_AUTHORIZATION) String serviceAuthorization,
            @PathVariable(SubmitServiceConfiguration.APPLICANT_EMAIL) String applicantEmail,
            @RequestBody ProbateCaseDetails probateCaseDetails
    );

    @PutMapping(
            value = "/submissions/{applicantEmail}",
            headers = CONTENT_TYPE + "=" + APPLICATION_JSON_VALUE
    )
    SubmitResult update(
            @RequestHeader(AUTHORIZATION) String authorisation,
            @RequestHeader(SubmitServiceConfiguration.SERVICE_AUTHORIZATION) String serviceAuthorization,
            @PathVariable(SubmitServiceConfiguration.APPLICANT_EMAIL) String applicantEmail,
            @RequestBody ProbateCaseDetails probateCaseDetails
    );

    @PostMapping(
            value = "/payments/{applicantEmail}",
            headers = CONTENT_TYPE + "=" + APPLICATION_JSON_VALUE
    )
    ProbateCaseDetails updatePayments(
            @RequestHeader(AUTHORIZATION) String authorisation,
            @RequestHeader(SubmitServiceConfiguration.SERVICE_AUTHORIZATION) String serviceAuthorization,
            @PathVariable(SubmitServiceConfiguration.APPLICANT_EMAIL) String applicantEmail,
            @RequestBody ProbatePaymentDetails probatePaymentDetails
    );
}
