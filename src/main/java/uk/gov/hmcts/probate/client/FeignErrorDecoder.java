package uk.gov.hmcts.probate.client;

import feign.Response;
import feign.codec.ErrorDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FeignErrorDecoder implements ErrorDecoder {
    private static final Logger log = LoggerFactory.getLogger(FeignErrorDecoder.class);

    @Override
    public Exception decode(String methodKey, Response response) {

        log.info("Status code " + response.status() + ", methodKey = " + methodKey);
        if (response.body() != null) {
            log.info("Body " + response.body().toString());
        }

        return new Exception(response.reason());
    }
}
