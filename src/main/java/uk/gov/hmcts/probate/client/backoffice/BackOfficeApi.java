package uk.gov.hmcts.probate.client.backoffice;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import uk.gov.hmcts.probate.client.submit.SubmitServiceConfiguration;
import uk.gov.hmcts.probate.model.backoffice.BackOfficeCallbackRequest;
import uk.gov.hmcts.probate.model.backoffice.BackOfficeCaveatResponse;
import uk.gov.hmcts.probate.model.backoffice.GrantDelayedResponse;

import static org.springframework.http.HttpHeaders.ACCEPT;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@FeignClient(
    name = "back-office-api",
    url = "${back.office.api.url}",
    configuration = BackOfficeConfiguration.class
)
public interface BackOfficeApi {

    String SERVICE_AUTHORIZATION = "ServiceAuthorization";

    @PostMapping(
        value = "/caveat/raise",
        headers = {
            CONTENT_TYPE + "=" + APPLICATION_JSON_VALUE,
            ACCEPT + "=" + APPLICATION_JSON_VALUE
        }
    )
    BackOfficeCaveatResponse raiseCaveat(
        @RequestHeader(AUTHORIZATION) String authorization,
        @RequestHeader(SERVICE_AUTHORIZATION) String serviceAuthorization,
        @RequestBody BackOfficeCallbackRequest backOfficeCallbackRequest
    );
    
    @PostMapping(
        value = "/data-extract/hmrc",
        headers = CONTENT_TYPE + "=" + APPLICATION_JSON_VALUE
    )
    ResponseEntity<String> initiateHmrcExtract(
        @RequestHeader(AUTHORIZATION) String authorisation,
        @RequestHeader(SubmitServiceConfiguration.SERVICE_AUTHORIZATION) String serviceAuthorization,
        @RequestParam(value = "fromDate", required = false) String fromDate,
        @RequestParam(value = "toDate", required = false) String toDate
    );

    @PostMapping(
        value = "/data-extract/iron-mountain",
        headers = CONTENT_TYPE + "=" + APPLICATION_JSON_VALUE
    )
    ResponseEntity<String> initiateIronMountainExtract(
        @RequestHeader(AUTHORIZATION) String authorisation,
        @RequestHeader(SubmitServiceConfiguration.SERVICE_AUTHORIZATION) String serviceAuthorization,
        @RequestParam(value = "date") String date
    );

    @PostMapping(
        value = "/data-extract/exela",
        headers = CONTENT_TYPE + "=" + APPLICATION_JSON_VALUE
    )
    ResponseEntity<String> initiateExelaExtract(
        @RequestHeader(AUTHORIZATION) String authorisation,
        @RequestHeader(SubmitServiceConfiguration.SERVICE_AUTHORIZATION) String serviceAuthorization,
        @RequestParam(value = "date") String date
    );

    @PostMapping(
        value = "/notify/grant-delayed-scheduled",
        headers = CONTENT_TYPE + "=" + APPLICATION_JSON_VALUE
    )
    GrantDelayedResponse initiateGrantDelayedNotification(
        @RequestHeader(AUTHORIZATION) String authorisation,
        @RequestHeader(SubmitServiceConfiguration.SERVICE_AUTHORIZATION) String serviceAuthorization,
        @RequestParam(value = "date") String date
    );

}
