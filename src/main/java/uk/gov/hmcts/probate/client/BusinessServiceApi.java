package uk.gov.hmcts.probate.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import uk.gov.hmcts.reform.probate.model.documents.CheckAnswersSummary;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@FeignClient(
        name = "business-service-api",
        url = "${business-service.api.url}",
        configuration = BusinessServiceConfiguration.class
)
public interface BusinessServiceApi {

    @PostMapping(
            value = "/businessDocument/generateCheckAnswersSummaryPDF",
            headers = CONTENT_TYPE + "=" + APPLICATION_JSON_VALUE
    )
    byte[] generateCheckAnswersSummaryPdf(
            @RequestHeader(AUTHORIZATION) String authorization,
            @RequestHeader(BusinessServiceConfiguration.SERVICE_AUTHORIZATION) String serviceAuthorization,
            @RequestBody CheckAnswersSummary checkAnswersSummary
    );
}
