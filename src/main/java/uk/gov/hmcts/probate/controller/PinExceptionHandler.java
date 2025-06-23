package uk.gov.hmcts.probate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import uk.gov.hmcts.probate.model.exception.PhonePinException;
import uk.gov.hmcts.reform.probate.model.client.ErrorResponse;

@Slf4j
@ControllerAdvice
@ResponseBody
public class PinExceptionHandler {
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
