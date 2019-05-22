package uk.gov.hmcts.probate.client.backoffice;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import uk.gov.hmcts.probate.model.backoffice.BackOfficeCallbackRequest;

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
    void raiseCaveat(
        @RequestHeader(AUTHORIZATION) String authorization,
        @RequestHeader(SERVICE_AUTHORIZATION) String serviceAuthorization,
        @RequestBody BackOfficeCallbackRequest backOfficeCallbackRequest
    );

}
