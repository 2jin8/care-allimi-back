package kr.ac.kumoh.allimi.exception;

public class AllNoticeException extends RuntimeException{
    public AllNoticeException() {
        super();
    }

    public AllNoticeException(String message) {
        super(message);
    }

    public AllNoticeException(String message, Throwable cause) {
        super(message, cause);
    }

    public AllNoticeException(Throwable cause) {
        super(cause);
    }

    protected AllNoticeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
