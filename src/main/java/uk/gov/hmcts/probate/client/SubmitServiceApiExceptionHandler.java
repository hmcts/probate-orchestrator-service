package uk.gov.hmcts.probate.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import uk.gov.hmcts.reform.probate.model.client.ApiClientErrorResponse;
import uk.gov.hmcts.reform.probate.model.client.ApiClientException;

@Slf4j
@ControllerAdvice
public class SubmitServiceApiExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ApiClientException.class)
    public ResponseEntity<ApiClientErrorResponse> handleApiClientException(final ApiClientException exception) {
        HttpStatus status = HttpStatus.resolve(exception.getStatus());

        if (status == null) {
            log.debug("SubmitService responded with unprocessable HttpStatus");
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return new ResponseEntity<>(exception.getErrorReponse(), status);
    }
}
