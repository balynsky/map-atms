package su.balynsky.android.atms.exceptions;

/**
 * Ошибка коннектора
 */
public class ConnectorException extends APIException {
    private static final long serialVersionUID = 1L;

    public ConnectorException(String message) {
        super(message);
    }

    public ConnectorException(String message, Throwable e) {
        super(message, e);
    }

}
