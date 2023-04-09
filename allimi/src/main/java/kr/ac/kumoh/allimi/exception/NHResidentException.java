package kr.ac.kumoh.allimi.exception;

public class NHResidentException extends RuntimeException{
    public NHResidentException() {
        super();
    }

    public NHResidentException(String message) {
        super(message);
    }

    public NHResidentException(String message, Throwable cause) {
        super(message, cause);
    }

    public NHResidentException(Throwable cause) {
        super(cause);
    }

    protected NHResidentException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
