package uk.gov.hmcts.probate.client;

import feign.Request;
import feign.Response;
import feign.Util;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import uk.gov.hmcts.probate.TestUtils;
import uk.gov.hmcts.reform.probate.model.client.ErrorResponse;
import uk.gov.hmcts.reform.probate.model.client.ErrorType;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import static feign.Util.UTF_8;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
public class ResponseDecoratorTest {

    private Map<String, Collection<String>> headers = new LinkedHashMap<>();

    @Test
    public void bodyToStringShouldReturnString() {
        Response response = Response.builder()
                .status(400)
                .reason("Bad Request")
                .request(Request.create(HttpMethod.GET.toString(), "/api", Collections.emptyMap(), null, Util.UTF_8))
                .headers(headers)
                .body("hello world", UTF_8)
                .build();

        ResponseDecorator responseDecorator = new ResponseDecorator(response);
        String body = responseDecorator.bodyToString();

        assertThat(body).isEqualTo("hello world");
    }

    @Test
    public void bodyToStringShouldReturnEmptyStringIfResponseBodyIsNull() {
        Response response = Response.builder()
                .status(400)
                .reason("Bad Request")
                .request(Request.create(HttpMethod.GET.toString(), "/api", Collections.emptyMap(), null, Util.UTF_8))
                .headers(headers)
                .build();

        ResponseDecorator responseDecorator = new ResponseDecorator(response);
        String body = responseDecorator.bodyToString();

        assertThat(response.body()).isNull();
        assertThat(body).isEqualTo("");
    }

    @Test
    public void mapToErrorResponseShouldReturnValidationErrorResponse() throws IOException {
        String validationErrorResponse = TestUtils.getJsonFromFile("errorResponse/validationErrorResponse.json");

        Response response = Response.builder()
                .status(500)
                .reason("Bad Request")
                .request(Request.create(HttpMethod.GET.toString(), "/api", Collections.emptyMap(), null, Util.UTF_8))
                .headers(headers)
                .body(validationErrorResponse, UTF_8)
                .build();

        ResponseDecorator responseDecorator = new ResponseDecorator(response);
        ErrorResponse errorResponse = responseDecorator.mapToErrorResponse();

        assertThat(errorResponse.getType()).isEqualTo(ErrorType.VALIDATION);
    }

    @Test
    public void mapToErrorResponseShouldReturnApiClientErrorResponse() throws IOException {
        String apiClientErrorResponse = TestUtils.getJsonFromFile("errorResponse/apiClientErrorResponse.json");

        Response response = Response.builder()
                .status(500)
                .reason("Bad Request")
                .request(Request.create(HttpMethod.GET.toString(), "/api", Collections.emptyMap(), null, Util.UTF_8))
                .headers(headers)
                .body(apiClientErrorResponse, UTF_8)
                .build();

        ResponseDecorator responseDecorator = new ResponseDecorator(response);
        ErrorResponse errorResponse = responseDecorator.mapToErrorResponse();

        assertThat(errorResponse.getType()).isEqualTo(ErrorType.API_CLIENT);
    }

    @Test
    public void mapToErrorResponseResponseBodyIsNull() {

        Response response = Response.builder()
                .status(500)
                .reason("Bad Request")
                .request(Request.create(HttpMethod.GET.toString(), "/api", Collections.emptyMap(), null, Util.UTF_8))
                .headers(headers)
                .build();

        ResponseDecorator responseDecorator = new ResponseDecorator(response);
        ErrorResponse errorResponse = responseDecorator.mapToErrorResponse();

        assertThat(errorResponse).isNull();
    }
}
