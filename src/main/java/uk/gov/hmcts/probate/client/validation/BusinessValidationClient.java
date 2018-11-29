package uk.gov.hmcts.probate.client.validation;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import uk.gov.hmcts.probate.configuration.FeignClientConfiguration;
import uk.gov.hmcts.probate.dto.formdata.FormDataDTO;
import uk.gov.hmcts.probate.dto.validation.BusinessValidationResponseDTO;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@FeignClient(name = "businessvalidationservice",
        url = "${business-validation-service.url}",
        configuration = FeignClientConfiguration.class
)
public interface BusinessValidationClient {
    String SERVICE_AUTHORIZATION = "ServiceAuthorization";

    @RequestMapping(
            method = RequestMethod.POST,
            value = "/probateTypes/intestacy/validations",
            headers = CONTENT_TYPE + "=" + APPLICATION_JSON_UTF8_VALUE

    )
    BusinessValidationResponseDTO valididateIntestacy(
            @RequestHeader(AUTHORIZATION) String authorisation,
            @RequestHeader(SERVICE_AUTHORIZATION) String serviceAuthorisation,
            @RequestBody FormDataDTO formDataDTO
    );

}
