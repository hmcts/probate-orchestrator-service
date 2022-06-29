package uk.gov.hmcts.probate.client;

import feign.Request;
import feign.Response;
import feign.Util;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertThrows;

class FeignErrorDecoderTest {
    private Map<String, Collection<String>> headers = new LinkedHashMap<>();
    private FeignErrorDecoder decoder = new FeignErrorDecoder();

    @Test
    void throwsException() {
        Response response = Response.builder()
                .status(500)
                .reason("Internal server error")
                .request(Request.create(Request.HttpMethod.GET,
                        "/api", Collections.emptyMap(), null, Util.UTF_8, null))
                .headers(headers)
                .build();

        assertThrows(Exception.class, () -> {
            throw decoder.decode("Service#foo()", response);
        });
    }
}
