package uk.gov.hmcts.probate.client.backoffice;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import uk.gov.hmcts.probate.client.submit.SubmitServiceConfiguration;
import uk.gov.hmcts.probate.model.backoffice.BackOfficeCallbackRequest;
import uk.gov.hmcts.probate.model.backoffice.BackOfficeCaveatResponse;
import uk.gov.hmcts.probate.model.backoffice.GrantScheduleResponse;
import uk.gov.hmcts.reform.probate.model.ProbateDocument;

import java.util.List;

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
            value = "/data-extract/exela",
            headers = CONTENT_TYPE + "=" + APPLICATION_JSON_VALUE
    )
    ResponseEntity<String> initiateExelaExtractDateRange(
            @RequestHeader(AUTHORIZATION) String authorisation,
            @RequestHeader(SubmitServiceConfiguration.SERVICE_AUTHORIZATION) String serviceAuthorization,
            @RequestParam(value = "fromDate", required = true) String fromDate,
            @RequestParam(value = "toDate", required = true) String toDate
    );

    @PostMapping(
        value = "/data-extract/smee-and-ford",
        headers = CONTENT_TYPE + "=" + APPLICATION_JSON_VALUE
    )
    ResponseEntity<String> initiateSmeeAndFordExtractDateRange(
        @RequestHeader(AUTHORIZATION) String authorisation,
        @RequestHeader(SubmitServiceConfiguration.SERVICE_AUTHORIZATION) String serviceAuthorization,
        @RequestParam(value = "fromDate", required = true) String fromDate,
        @RequestParam(value = "toDate", required = true) String toDate
    );

    @PostMapping(
            value = "/notify/application-received",
            headers = {
                    CONTENT_TYPE + "=" + APPLICATION_JSON_VALUE,
                    ACCEPT + "=" + APPLICATION_JSON_VALUE
            }
    )
    ProbateDocument applicationReceived(
            @RequestHeader(AUTHORIZATION) String authorization,
            @RequestHeader(SERVICE_AUTHORIZATION) String serviceAuthorization,
            @RequestBody BackOfficeCallbackRequest backOfficeCallbackRequest
    );


    @PostMapping(
        value = "/notify/grant-delayed-scheduled",
        headers = CONTENT_TYPE + "=" + APPLICATION_JSON_VALUE
    )
    GrantScheduleResponse initiateGrantDelayedNotification(
        @RequestHeader(AUTHORIZATION) String authorisation,
        @RequestHeader(SubmitServiceConfiguration.SERVICE_AUTHORIZATION) String serviceAuthorization,
        @RequestParam(value = "date") String date
    );

    @PostMapping(
        value = "/notify/grant-awaiting-documents-scheduled",
        headers = CONTENT_TYPE + "=" + APPLICATION_JSON_VALUE
    )
    GrantScheduleResponse initiateGrantAwaitingDocumentsNotification(
        @RequestHeader(AUTHORIZATION) String authorisation,
        @RequestHeader(SubmitServiceConfiguration.SERVICE_AUTHORIZATION) String serviceAuthorization,
        @RequestParam(value = "date") String date
    );

    @PostMapping(
        value = "/document/upload",
        consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    List<String> uploadDocument(
        @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization,
        @RequestHeader(SubmitServiceConfiguration.SERVICE_AUTHORIZATION) String serviceAuthorization,
        @RequestPart("file") MultipartFile file
    );

    @PostMapping(
            value = "/data-extract/make-dormant",
            headers = CONTENT_TYPE + "=" + APPLICATION_JSON_VALUE
    )
    ResponseEntity<String> makeDormant(
            @RequestHeader(AUTHORIZATION) String authorisation,
            @RequestHeader(SubmitServiceConfiguration.SERVICE_AUTHORIZATION) String serviceAuthorization,
            @RequestParam(value = "date", required = true) String date
    );

    @PostMapping(
            value = "/data-extract/reactivate-dormant",
            headers = CONTENT_TYPE + "=" + APPLICATION_JSON_VALUE
    )
    ResponseEntity<String> reactivateDormant(
            @RequestHeader(AUTHORIZATION) String authorisation,
            @RequestHeader(SubmitServiceConfiguration.SERVICE_AUTHORIZATION) String serviceAuthorization,
            @RequestParam(value = "fromDate", required = true) String fromDate,
            @RequestParam(value = "toDate", required = true) String toDate
    );
}
