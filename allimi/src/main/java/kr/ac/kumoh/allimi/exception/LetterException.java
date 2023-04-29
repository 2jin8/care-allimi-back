package kr.ac.kumoh.allimi.exception;

public class LetterException extends RuntimeException{
    public LetterException() {
        super();
    }

    public LetterException(String message) {
        super(message);
    }

    public LetterException(String message, Throwable cause) {
        super(message, cause);
    }

    public LetterException(Throwable cause) {
        super(cause);
    }

    protected LetterException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
