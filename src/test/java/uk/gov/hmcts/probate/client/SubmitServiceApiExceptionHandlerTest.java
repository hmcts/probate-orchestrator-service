package uk.gov.hmcts.probate.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;
import uk.gov.hmcts.reform.probate.model.client.ApiClientErrorResponse;
import uk.gov.hmcts.reform.probate.model.client.ApiClientException;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class SubmitServiceApiExceptionHandlerTest {

    private SubmitServiceApiExceptionHandler exceptionHandler = new SubmitServiceApiExceptionHandler();
    private ApiClientErrorResponse clientApiError;

    @Before
    public void setUp() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"exception\":\"uk.gov.hmcts.ccd.endpoint.exceptions.ResourceNotFoundException\",\"timestamp\":\"2019-03-18T12:42:22.384\",\"status\":404,\"error\":\"Not Found\",\"message\":\"No field found\"}";
        clientApiError = mapper.readValue(json, ApiClientErrorResponse.class);
    }

    @Test
    public void handleApiClientExceptionReturnsResponseStatus500(){
        ResponseEntity responseEntity = exceptionHandler
                .handleApiClientException(new ApiClientException(500, clientApiError));

        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(500);
    }

    @Test
    public void handleApiClientExceptionReturnsResponseStatus400(){
        ResponseEntity responseEntity = exceptionHandler
                .handleApiClientException(new ApiClientException(400, clientApiError));

        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(400);
    }

    @Test
    public void handleApiClientExceptionReturnsResponseStatus500WhenHttpStatusIsUnprocessable(){
        ResponseEntity responseEntity = exceptionHandler
                .handleApiClientException(new ApiClientException(599, clientApiError));

        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(500);
    }

    @Test
    public void handleApiClientExceptionWithBlankMessage(){
        ApiClientErrorResponse clientApiError = new ApiClientErrorResponse();

        ResponseEntity responseEntity = exceptionHandler
                .handleApiClientException(new ApiClientException(400, clientApiError));

        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(400);

    }
}
