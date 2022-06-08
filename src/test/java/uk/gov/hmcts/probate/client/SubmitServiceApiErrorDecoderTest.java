package uk.gov.hmcts.probate.client;

import feign.Request;
import feign.Response;
import feign.Util;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import uk.gov.hmcts.probate.client.submit.SubmitServiceApiErrorDecoder;
import uk.gov.hmcts.reform.probate.model.client.ApiClientException;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertThrows;


public class SubmitServiceApiErrorDecoderTest {

    private Map<String, Collection<String>> headers = new LinkedHashMap<>();

    private SubmitServiceApiErrorDecoder errorDecoder = new SubmitServiceApiErrorDecoder();

    @Test
    public void throwsApiClientException() throws Throwable {
        assertThrows(ApiClientException.class, () -> {
            Response response = Response.builder()
                    .status(500)
                    .reason("Internal server error")
                    .request(Request.create(HttpMethod.GET.toString(),
                            "/api", Collections.emptyMap(), null, Util.UTF_8))
                    .headers(headers)
                    .build();

            throw errorDecoder.decode("Service#foo()", response);
        });

    }
}
