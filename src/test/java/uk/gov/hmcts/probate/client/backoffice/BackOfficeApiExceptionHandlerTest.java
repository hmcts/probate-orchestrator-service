package uk.gov.hmcts.probate.client.backoffice;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;
import uk.gov.hmcts.reform.probate.model.client.ApiClientException;
import uk.gov.hmcts.reform.probate.model.client.ErrorResponse;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class BackOfficeApiExceptionHandlerTest {

    private BackOfficeApiExceptionHandler exceptionHandler = new BackOfficeApiExceptionHandler();

    @Mock
    private ErrorResponse errorResponse;

    @Test
    public void handleApiClientExceptionReturnsResponseStatus500() {
        ResponseEntity responseEntity = exceptionHandler
            .handleApiClientException(new ApiClientException(500, errorResponse));

        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(500);
    }

}