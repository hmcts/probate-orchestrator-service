package uk.gov.hmcts.probate.exception.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import uk.gov.hmcts.probate.exception.OrchestratorException;
import uk.gov.hmcts.probate.exception.model.ErrorResponse;

import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice
@ResponseBody
class OrchestratorExceptionHandler extends ResponseEntityExceptionHandler {

    public static final String PROBATE_INTERNAL_ERROR = "Orchestrator Internal Error";
    private static final String JSON_VALIDATION_ERROR = "Json Error";

    @ExceptionHandler(OrchestratorException.class)
    public ResponseEntity<ErrorResponse> handle(OrchestratorException exception) {

        log.warn("Business Document exception: {}", exception.getMessage(), exception);
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), PROBATE_INTERNAL_ERROR, exception.getMessage());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<ErrorResponse>(errorResponse, headers, HttpStatus.valueOf(errorResponse.getCode()));
    }



}
