package uk.gov.hmcts.probate.client;

import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FeignErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {

        log.info("Status code " + response.status() + ", methodKey = " + methodKey);
        log.info("Body " + response.body());

        return new Exception(response.reason());
    }
}