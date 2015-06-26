package su.balynsky.android.atms.exceptions;

/**
 * Базовый тип ошибки
 */
public class APIException extends Exception {
    private static final long serialVersionUID = 1L;

    public APIException(String message) {
        super(message);
    }

    public APIException(String message, Throwable e) {
        super(message, e);
    }
}
