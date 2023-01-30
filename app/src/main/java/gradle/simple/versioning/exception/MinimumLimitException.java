package gradle.simple.versioning.exception;

public class MinimumLimitException extends Exception {

    public MinimumLimitException() {
    }

    public MinimumLimitException(String message) {
        super(message);
    }

    public MinimumLimitException(Throwable cause) {
        super(cause);
    }

    public MinimumLimitException(String message, Throwable cause) {
        super(message, cause);
    }

    public MinimumLimitException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
