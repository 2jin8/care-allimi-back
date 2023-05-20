package kr.ac.kumoh.allimi.exception;

public class DataAlreadyExistsException2 extends RuntimeException{
    public DataAlreadyExistsException2() {
        super();
    }

    public DataAlreadyExistsException2(String message) {
        super(message);
    }

    public DataAlreadyExistsException2(String message, Throwable cause) {
        super(message, cause);
    }

    public DataAlreadyExistsException2(Throwable cause) {
        super(cause);
    }

    protected DataAlreadyExistsException2(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
