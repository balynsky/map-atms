package su.balynsky.android.atms.exceptions;

/**
 * Ошибка фабрики DAO интерфейса
 */
public class DAOFactoryException extends APIException {
    private static final long serialVersionUID = 1L;

    public DAOFactoryException(String message) {
        super(message);
    }

    public DAOFactoryException(String message, Throwable e) {
        super(message, e);
    }
}
