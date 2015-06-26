package su.balynsky.android.atms.dao;

import su.balynsky.android.atms.exceptions.ConnectorException;
import su.balynsky.android.atms.model.atm.AtmsInfoResponse;
import su.balynsky.android.atms.model.atm.ModificationRequiredInfo;

import java.util.Date;

/**
 * @author Sergey Balynsky
 */
public interface IAtmsDao {
    /**
     * Список банкоматов
     *
     * @param lastUpdateTime - дата последнего обновления в формате "dd.MM.yyyy hh.mm.ss"
     * @throws su.balynsky.android.atms.exceptions.ConnectorException
     */
    AtmsInfoResponse getAtmsInfo(Date lastUpdateTime) throws ConnectorException;

    /**
     * Проверяет необходимость обновления банкоматов
     *
     * @param lastUpdateTime дата последнего обновления
     * @return информация по обновлению базы
     * @throws ConnectorException
     */
    ModificationRequiredInfo isModificationRequired(Date lastUpdateTime) throws ConnectorException;
}
