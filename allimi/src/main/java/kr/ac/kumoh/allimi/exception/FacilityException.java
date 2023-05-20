package kr.ac.kumoh.allimi.exception;

public class FacilityException extends RuntimeException{
    public FacilityException() {
        super();
    }

    public FacilityException(String message) {
        super(message);
    }

    public FacilityException(String message, Throwable cause) {
        super(message, cause);
    }

    public FacilityException(Throwable cause) {
        super(cause);
    }

    protected FacilityException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
