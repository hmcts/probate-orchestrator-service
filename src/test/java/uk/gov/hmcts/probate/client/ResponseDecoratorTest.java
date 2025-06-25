package uk.gov.hmcts.probate.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Request;
import feign.Response;
import feign.Util;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import uk.gov.hmcts.probate.TestUtils;
import uk.gov.hmcts.reform.probate.model.client.ErrorResponse;
import uk.gov.hmcts.reform.probate.model.client.ErrorType;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import static feign.Util.UTF_8;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ResponseDecoratorTest {

    private final Map<String, Collection<String>> headers = new LinkedHashMap<>();

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void bodyToStringShouldReturnString() throws ReflectiveOperationException {
        Response response = Response.builder()
                .status(400)
                .reason("Bad Request")
                .request(Request.create(HttpMethod.GET.toString(), "/api", Collections.emptyMap(), null, Util.UTF_8))
                .headers(headers)
                .body("hello world", UTF_8)
                .build();

        ResponseDecorator responseDecorator = new ResponseDecorator(response, objectMapper);
        String body = responseDecorator.bodyToString();

        assertThat(body).isEqualTo("hello world");
    }

    @Test
    void bodyToStringShouldReturnEmptyStringIfResponseBodyIsNull() {
        Response response = Response.builder()
                .status(400)
                .reason("Bad Request")
                .request(Request.create(HttpMethod.GET.toString(), "/api", Collections.emptyMap(), null, Util.UTF_8))
                .headers(headers)
                .build();

        ResponseDecorator responseDecorator = new ResponseDecorator(response, objectMapper);
        String body = responseDecorator.bodyToString();

        assertThat(response.body()).isNull();
        assertThat(body).isEqualTo("");
    }

    @Test
    void bodyToStringShouldReturnEmptyStringIfResponseBodyIsNotNull() {
        Response response = Response.builder()
                .status(400)
                .reason("Bad Request")
                .request(Request.create(HttpMethod.GET.toString(), "/api", Collections.emptyMap(), null, Util.UTF_8))
                .headers(headers)
                .body(new InputStream() {
                    @Override
                    public int read() throws IOException {
                        throw new IOException();
                    }
                }, 1)
                .build();
        ResponseDecorator responseDecorator = new ResponseDecorator(response, objectMapper);

        String body = responseDecorator.bodyToString();

        assertEquals("", body);
    }

    @Test
    void mapToErrorResponseShouldReturnValidationErrorResponse() throws IOException {
        String validationErrorResponse = TestUtils.getJsonFromFile("errorResponse/validationErrorResponse.json");

        Response response = Response.builder()
                .status(500)
                .reason("Bad Request")
                .request(Request.create(HttpMethod.GET.toString(), "/api", Collections.emptyMap(), null, Util.UTF_8))
                .headers(headers)
                .body(validationErrorResponse, UTF_8)
                .build();

        ResponseDecorator responseDecorator = new ResponseDecorator(response, objectMapper);
        ErrorResponse errorResponse = responseDecorator.mapToErrorResponse();

        assertThat(errorResponse.getType()).isEqualTo(ErrorType.VALIDATION);
    }

    @Test
    void mapToErrorResponseShouldReturnApiClientErrorResponse() throws IOException {
        String apiClientErrorResponse = TestUtils.getJsonFromFile("errorResponse/apiClientErrorResponse.json");

        Response response = Response.builder()
                .status(500)
                .reason("Bad Request")
                .request(Request.create(HttpMethod.GET.toString(), "/api", Collections.emptyMap(), null, Util.UTF_8))
                .headers(headers)
                .body(apiClientErrorResponse, UTF_8)
                .build();

        ResponseDecorator responseDecorator = new ResponseDecorator(response, objectMapper);
        ErrorResponse errorResponse = responseDecorator.mapToErrorResponse();

        assertThat(errorResponse.getType()).isEqualTo(ErrorType.API_CLIENT);
    }

    @Test
    void mapToErrorResponseResponseBodyIsNull() {

        Response response = Response.builder()
                .status(500)
                .reason("Bad Request")
                .request(Request.create(HttpMethod.GET.toString(), "/api", Collections.emptyMap(), null, Util.UTF_8))
                .headers(headers)
                .build();

        ResponseDecorator responseDecorator = new ResponseDecorator(response, objectMapper);
        ErrorResponse errorResponse = responseDecorator.mapToErrorResponse();

        assertThat(errorResponse).isNull();
    }
}
