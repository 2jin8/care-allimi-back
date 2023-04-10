package kr.ac.kumoh.allimi.exception;

public class UserIdDuplicateException extends RuntimeException{
    public UserIdDuplicateException() {
        super();
    }

    public UserIdDuplicateException(String message) {
        super(message);
    }

    public UserIdDuplicateException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserIdDuplicateException(Throwable cause) {
        super(cause);
    }

    protected UserIdDuplicateException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
