package su.balynsky.android.atms.content.services;

import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import org.roboguice.shaded.goole.common.base.Function;
import org.roboguice.shaded.goole.common.collect.Lists;
import su.balynsky.android.atms.content.common.BaseContentHelper;
import su.balynsky.android.atms.content.common.SelectionParsingUtils;
import su.balynsky.android.atms.content.database.SQLHelper;
import su.balynsky.android.atms.content.provider.AtmsContentProvider;
import su.balynsky.android.atms.content.services.common.AtmsHelper;
import su.balynsky.android.atms.dao.IAtmsDao;
import su.balynsky.android.atms.dao.json.JsonDAOFactory;
import su.balynsky.android.atms.exceptions.APIException;
import su.balynsky.android.atms.exceptions.DAOFactoryException;
import su.balynsky.android.atms.exceptions.TechnicalException;
import su.balynsky.android.atms.model.atm.AtmInfo;
import su.balynsky.android.atms.model.atm.AtmsInfoResponse;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * @author Sergey Balynsky
 *         on 28.01.2015.
 */
public class AtmsService extends ApiService {
    private final static String TAG = AtmsService.class.getCanonicalName();
    private IAtmsDao configurationDao;

    public AtmsService() {
        super(AtmsService.class.getCanonicalName());
    }

    @Override
    protected void onInitialize() throws TechnicalException {
        super.onInitialize();
        try {
            configurationDao = (IAtmsDao) (new JsonDAOFactory()).createInstance(IAtmsDao.class);
        } catch (DAOFactoryException e) {
            e.printStackTrace();
            throw new TechnicalException(e.getMessage(), e);
        }
    }

    @Override
    protected void doGet(Uri uri, String[] projection, String selection, String[] selectionArgs, String orderBy, Bundle serviceToken) throws IOException, TechnicalException, APIException {
        final Map<String, String[]> parameters = SelectionParsingUtils.parseSelection(selection, selectionArgs);
        final Long lastChange = Long.parseLong(parameters.get(AtmsContentProvider.LAST_DATE_CHANGE)[0]);
        Log.d(TAG, "AtmsService.doGet lastChange=[" + lastChange + "]");
        AtmsInfoResponse atms = configurationDao.getAtmsInfo(new Date(lastChange));
        Log.d(TAG, "AtmsService.doGet AtmsInfoResponse=[" + atms + "]");

        List<ContentValues> accountLimitsValuesList = Lists.transform(Arrays.asList(atms.getModifiedAtms()),
                new Function<AtmInfo, ContentValues>() {
                    public ContentValues apply(AtmInfo atmInfo) {
                        return AtmsHelper.getContentValues(atmInfo);
                    }
                });

        Log.d(TAG, "AtmsService.doGet accountLimitsValuesList=[" + accountLimitsValuesList + "]");
        //Добавление или изменение
        BaseContentHelper.respondToInsert(getApplicationContext(), uri,
                accountLimitsValuesList.toArray(new ContentValues[accountLimitsValuesList.size()]));

        //Удаление...
        StringBuilder deleteSelection = new StringBuilder();
        SQLHelper.addInCondition(deleteSelection, AtmsContentProvider.Columns.CODE, atms.getDeletedAtms().length);
        BaseContentHelper.respondToDelete(getApplicationContext(), uri, deleteSelection.toString(), atms.getDeletedAtms());
    }

    @Override
    protected void doPost(Uri uri, ContentValues contentValues) throws IOException, TechnicalException, APIException {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void doPut(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) throws IOException, TechnicalException, APIException {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void doDelete(Uri uri, String selection, String[] selectionArgs) throws IOException, TechnicalException, APIException {
        throw new UnsupportedOperationException();
    }

}
