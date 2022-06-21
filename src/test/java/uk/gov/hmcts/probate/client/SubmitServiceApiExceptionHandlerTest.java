package uk.gov.hmcts.probate.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import uk.gov.hmcts.probate.TestUtils;
import uk.gov.hmcts.probate.client.submit.SubmitServiceApiExceptionHandler;
import uk.gov.hmcts.reform.probate.model.client.ApiClientErrorResponse;
import uk.gov.hmcts.reform.probate.model.client.ApiClientException;
import uk.gov.hmcts.reform.probate.model.client.ErrorResponse;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
public class SubmitServiceApiExceptionHandlerTest {

    private SubmitServiceApiExceptionHandler exceptionHandler = new SubmitServiceApiExceptionHandler();
    private ErrorResponse errorResponse;

    @BeforeEach
    public void setUp() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = TestUtils.getJsonFromFile("errorResponse/apiClientErrorResponse.json");
        errorResponse = mapper.readValue(json, ErrorResponse.class);
    }

    @Test
    public void handleApiClientExceptionReturnsResponseStatus500() {
        ResponseEntity responseEntity = exceptionHandler
                .handleApiClientException(new ApiClientException(500, errorResponse));

        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(500);
    }

    @Test
    public void handleApiClientExceptionReturnsResponseStatus400() {
        ResponseEntity responseEntity = exceptionHandler
                .handleApiClientException(new ApiClientException(400, errorResponse));

        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(400);
    }

    @Test
    public void handleApiClientExceptionReturnsResponseStatus500WhenHttpStatusIsUnprocessable() {
        ResponseEntity responseEntity = exceptionHandler
                .handleApiClientException(new ApiClientException(599, errorResponse));

        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(500);
    }

    @Test
    public void handleApiClientExceptionWithBlankMessage() {
        ApiClientErrorResponse errorResponse = new ApiClientErrorResponse();

        ResponseEntity responseEntity = exceptionHandler
                .handleApiClientException(new ApiClientException(400, errorResponse));

        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(400);
    }

    @Test
    public void handleApiClientExceptionWithValidationErrorResponse() throws IOException {
        String json = TestUtils.getJsonFromFile("errorResponse/validationErrorResponse.json");
        ObjectMapper mapper = new ObjectMapper();
        errorResponse = mapper.readValue(json, ErrorResponse.class);
        ResponseEntity responseEntity = exceptionHandler
                .handleApiClientException(new ApiClientException(400, errorResponse));

        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(400);
        assertThat(responseEntity.hasBody()).isTrue();
    }

    @Test
    public void handleApiClientExceptionWithApiClientErrorResponse() throws IOException {
        ResponseEntity responseEntity = exceptionHandler
                .handleApiClientException(new ApiClientException(400, errorResponse));

        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(400);
        assertThat(responseEntity.hasBody()).isTrue();
    }
}
