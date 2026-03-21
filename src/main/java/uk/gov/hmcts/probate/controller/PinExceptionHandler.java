package uk.gov.hmcts.probate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import uk.gov.hmcts.probate.model.exception.PhonePinException;
import uk.gov.hmcts.reform.probate.model.client.ErrorResponse;

@ControllerAdvice
@ResponseBody
public class PinExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(PinExceptionHandler.class);
    public static final String PHONE_PIN_EXCEPTION = "PhonePin Error";

    @ExceptionHandler(PhonePinException.class)
    public ResponseEntity<ErrorResponse> handle(PhonePinException exception) {
        log.warn("PhonePin exception: {}", exception.getMessage(), exception);

        ErrorResponse errorResponse = exception.getErrorResponse();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return ResponseEntity.badRequest()
                .headers(headers)
                .body(errorResponse);
    }
}
