package uk.gov.hmcts.probate.exception;

public class OrchestratorException extends RuntimeException{

    private static final long serialVersionUID = -1444517913064905084L;

    public OrchestratorException(String message) {
        super(message);
    }

    public OrchestratorException(String message, Throwable cause) {
        super(message, cause);
    }
}
