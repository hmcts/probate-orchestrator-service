package uk.gov.hmcts.probate.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import uk.gov.hmcts.reform.probate.model.cases.ProbateCaseDetails;
import uk.gov.hmcts.reform.probate.model.cases.ProbatePaymentDetails;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@FeignClient(
    name = "submit-service-api",
    url = "${submit-service.api.url}",
    configuration = SubmitServiceConfiguration.class
)
public interface SubmitServiceApi {

    String SERVICE_AUTHORIZATION = "ServiceAuthorization";

    @RequestMapping(
        method = RequestMethod.GET,
        value = "/cases/{applicantEmail}",
        headers = CONTENT_TYPE + "=" + APPLICATION_JSON_VALUE
    )
    ProbateCaseDetails getCase(
        @RequestHeader(AUTHORIZATION) String authorisation,
        @RequestHeader(SERVICE_AUTHORIZATION) String serviceAuthorization,
        @PathVariable("applicantEmail") String applicantEmail,
        @RequestParam("caseType") String caseType
    );

    @RequestMapping(
        method = RequestMethod.POST,
        value = "/drafts/{applicantEmail}",
        headers = CONTENT_TYPE + "=" + APPLICATION_JSON_VALUE
    )
    ProbateCaseDetails saveDraft(
        @RequestHeader(AUTHORIZATION) String authorisation,
        @RequestHeader(SERVICE_AUTHORIZATION) String serviceAuthorization,
        @PathVariable("applicantEmail") String applicantEmail,
        @RequestBody ProbateCaseDetails probateCaseDetails
    );

    @RequestMapping(
        method = RequestMethod.POST,
        value = "/submissions/{applicantEmail}",
        headers = CONTENT_TYPE + "=" + APPLICATION_JSON_VALUE
    )
    ProbateCaseDetails submit(
        @RequestHeader(AUTHORIZATION) String authorisation,
        @RequestHeader(SERVICE_AUTHORIZATION) String serviceAuthorization,
        @PathVariable("applicantEmail") String applicantEmail,
        @RequestBody ProbateCaseDetails probateCaseDetails
    );

    @RequestMapping(
        method = RequestMethod.POST,
        value = "/payments/{applicantEmail}",
        headers = CONTENT_TYPE + "=" + APPLICATION_JSON_VALUE
    )
    ProbateCaseDetails updatePayments(
        @RequestHeader(AUTHORIZATION) String authorisation,
        @RequestHeader(SERVICE_AUTHORIZATION) String serviceAuthorization,
        @PathVariable("applicantEmail") String applicantEmail,
        @RequestBody ProbatePaymentDetails probatePaymentDetails
    );
}
