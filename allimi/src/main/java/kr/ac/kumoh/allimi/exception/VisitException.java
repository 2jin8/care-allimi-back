package kr.ac.kumoh.allimi.exception;

public class VisitException extends RuntimeException {
    public VisitException() {
        super();
    }

    public VisitException(String message) {
        super(message);
    }

    public VisitException(String message, Throwable cause) {
        super(message, cause);
    }

    public VisitException(Throwable cause) {
        super(cause);
    }

    protected VisitException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
