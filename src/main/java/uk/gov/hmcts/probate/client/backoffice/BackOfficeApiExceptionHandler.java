package uk.gov.hmcts.probate.client.backoffice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import uk.gov.hmcts.reform.probate.model.client.ApiClientException;
import uk.gov.hmcts.reform.probate.model.client.ErrorResponse;

@ControllerAdvice
public class BackOfficeApiExceptionHandler extends ResponseEntityExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(BackOfficeApiExceptionHandler.class);

    @ExceptionHandler(ApiClientException.class)
    public ResponseEntity<ErrorResponse> handleApiClientException(final ApiClientException exception) {
        HttpStatus status = HttpStatus.resolve(exception.getStatus());

        if (status == null) {
            log.debug("BackOffice responded with unprocessable HttpStatus");
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return new ResponseEntity<>(exception.getErrorResponse(), status);
    }
}
