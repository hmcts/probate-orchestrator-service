package uk.gov.hmcts.probate.model.exception;

public class AuthenticationError extends Exception{
    public AuthenticationError(String message) {
        super(message);
    }
}
