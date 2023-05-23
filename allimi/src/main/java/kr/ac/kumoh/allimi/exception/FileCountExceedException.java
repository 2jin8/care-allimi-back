package kr.ac.kumoh.allimi.exception;

public class FileCountExceedException extends RuntimeException {
    public FileCountExceedException() {
        super();
    }

    public FileCountExceedException(String message) {
        super(message);
    }

    public FileCountExceedException(String message, Throwable cause) {
        super(message, cause);
    }

    public FileCountExceedException(Throwable cause) {
        super(cause);
    }
}
