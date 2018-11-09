package uk.gov.hmcts.probate.exception;

public class OrchestratorException extends RuntimeException{

    public OrchestratorException(String message) {
        super(message);
    }

    public OrchestratorException(String message, Throwable cause) {
        super(message, cause);
    }
}
