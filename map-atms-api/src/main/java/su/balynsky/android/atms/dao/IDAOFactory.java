package su.balynsky.android.atms.dao;

import su.balynsky.android.atms.exceptions.DAOFactoryException;

/**
 * Abstract factory for create DAO*
 *
 * @author Sergey Balynsky
 */
public interface IDAOFactory {
    /**
     * Создает экземпляр класса, который реализует интерфейс requiredInterface
     *
     * @param requiredInterface - Интерфейс, реализация которого запрашивается
     * @return Экземпляр класса, который реализует интерфейс requiredInterface
     */
    Object createInstance(Class<?> requiredInterface) throws DAOFactoryException;

}
