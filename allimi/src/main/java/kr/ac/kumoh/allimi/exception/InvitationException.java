package kr.ac.kumoh.allimi.exception;

public class InvitationException extends RuntimeException{
    public InvitationException() {
        super();
    }

    public InvitationException(String message) {
        super(message);
    }

    public InvitationException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvitationException(Throwable cause) {
        super(cause);
    }

    protected InvitationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
