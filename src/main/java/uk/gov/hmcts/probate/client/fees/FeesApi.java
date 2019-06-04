package uk.gov.hmcts.probate.client.fees;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import uk.gov.hmcts.reform.probate.model.fees.FeeLookupResponseDto;

import java.math.BigDecimal;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@FeignClient(
    name = "fee-api",
    url = "${fees.api.url}",
    configuration = FeesConfiguration.class
)
public interface FeesApi {

    String SERVICE_AUTHORIZATION = "ServiceAuthorization";

    @GetMapping(
        value = "/fees-register/fees/lookup",
        headers = CONTENT_TYPE + "=" + APPLICATION_JSON_VALUE
    )
    FeeLookupResponseDto getFeesLookup(
        @RequestHeader(AUTHORIZATION) String authorisation,
        @RequestHeader(SERVICE_AUTHORIZATION) String serviceAuthorization,
        @RequestParam("service") String service,
        @RequestParam("jurisdiction1") String jurisdiction1,
        @RequestParam("jurisdiction2") String jurisdiction2,
        @RequestParam("channel") String channel,
        @RequestParam("event") String event,
        @RequestParam("applicant_type") String applicantType,
        @RequestParam("amount_or_volume") BigDecimal amount
    );

    @GetMapping(
        value = "/fees-register/fees/lookup",
        headers = CONTENT_TYPE + "=" + APPLICATION_JSON_VALUE
    )
    FeeLookupResponseDto getFeesLookupWithKeyword(
        @RequestHeader(AUTHORIZATION) String authorisation,
        @RequestHeader(SERVICE_AUTHORIZATION) String serviceAuthorization,
        @RequestParam("service") String service,
        @RequestParam("jurisdiction1") String jurisdiction1,
        @RequestParam("jurisdiction2") String jurisdiction2,
        @RequestParam("channel") String channel,
        @RequestParam("event") String event,
        @RequestParam("applicant_type") String applicantType,
        @RequestParam("keyword") String keyword,
        @RequestParam("amount_or_volume") BigDecimal amount
    );
}
