package uk.gov.hmcts.probate.client;

import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
public class FeignErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {

        log.info("Status code " + response.status() + ", methodKey = " + methodKey);
        if (response.body() != null) {
            try {
                final var body = response.body();
                final var bodyBytes = body.asInputStream().readAllBytes();
                final String bodyString = new String(bodyBytes, StandardCharsets.UTF_8);
                log.info("Body [" + bodyString + "]");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return new Exception(response.reason());
    }
}