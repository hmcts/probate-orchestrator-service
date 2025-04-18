package uk.gov.hmcts.probate.model.exception;

import org.springframework.validation.FieldError;
import uk.gov.hmcts.reform.probate.model.client.ErrorResponse;
import uk.gov.hmcts.reform.probate.model.client.ValidationError;
import uk.gov.hmcts.reform.probate.model.client.ValidationErrorResponse;

import java.util.List;
import java.util.function.Function;

public class PhonePinException extends RuntimeException {
    private final List<FieldError> fieldErrors;

    public PhonePinException(
            final String message,
            final List<FieldError> fieldErrors) {
        super(message);
        this.fieldErrors = fieldErrors;
    }

    public ErrorResponse getErrorResponse() {
        final Function<FieldError, ValidationError> convert = fE -> ValidationError.builder()
                .field(fE.getField())
                .code(fE.getCode())
                .message(fE.getDefaultMessage())
                .build();
        final var validationList = fieldErrors.stream()
                .map(convert)
                .toList();
        return new ValidationErrorResponse(validationList);
    }
}
