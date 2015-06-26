package su.balynsky.android.atms.content.provider;

import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import su.balynsky.android.atms.content.api.ApiServiceHelper;
import su.balynsky.android.atms.content.cache.QueryCacheManager;

import java.util.Arrays;

/**
 * @author Sergey Balynsky
 *         on 26.01.2015.
 */
public abstract class AbstractContentProvider extends BaseContentProvider {

    protected abstract String getTag();

    protected abstract String getTableName();

    protected abstract int getRefreshTtl();

    protected abstract int getValidTtl();

    protected abstract Uri getProviderUri();

    protected abstract Class getServiceClass();

    public static class Columns {
        public static final String LOCAL_ID = "_id";
        public static final String LOCAL_STATUS = "_status";
    }

    @Override
    protected Cursor doQuery(Uri uri, String[] projection, String selection, String[] selectionArgs, String orderBy) throws Exception {
        Log.d(getTag(), "AbstractContentProvider.doQuery");
        Log.d(getTag(), String.format("query uri=%s projection=%s selection=%s selectionArgs=%s orderBy=%s",
                uri, Arrays.toString(projection), selection, Arrays.toString(selectionArgs), orderBy));

        if (QueryCacheManager.isCacheAvailable(uri, selection, selectionArgs)) {
            Log.d(getTag(), "query: isCacheAvailable=true");
            return prepareCursor(
                    getHelper().getWritableDatabase().query(getTableName(), projection, selection,
                            selectionArgs, null, null, orderBy), uri, selection, selectionArgs);
        }

        if (QueryCacheManager.isCacheBeingRefreshed(uri, selection, selectionArgs)) {
            // API query has already been started
            Log.d(getTag(), "query: isCacheBeingRefreshed=true");
            return prepareCursor(getLoadingCursor(getTableName(), uri), uri, selection, selectionArgs);
        }

        // There is no cache, so mark a cache as waiting and start a service query
        QueryCacheManager.markDataBeingRefreshed(uri, selection, selectionArgs, getRefreshTtl());

        Log.d(getTag(), "query: requesting a service " + getServiceClass().getSimpleName());
        ApiServiceHelper.doGet(getContext(), getServiceClass(), uri, projection,
                selection, selectionArgs, orderBy,
                QueryCacheManager.getServiceToken(uri, selection, selectionArgs));

        // This helps client to understand that the cursor is not ready
        return prepareCursor(getLoadingCursor(getTableName(), uri), uri, selection, selectionArgs);
    }

    @Override
    protected void onQueryResponse(Uri uri, String[] projection, String selection, String[] selectionArgs, String orderBy, ContentValues[] response, Bundle serviceToken) throws Exception {
        Log.d(getTag(), String.format("onQueryResponse uri=%s projection=%s selection=%s selectionArgs=%s orderBy=%s response.length=%d",
                uri, Arrays.toString(projection), selection,
                Arrays.toString(selectionArgs), orderBy,
                response == null ? 0 : response.length));

        insertOrUpdateReadyRecords(response);

        Log.d(getTag(), String.format("notifyDataCached uri=%s selection=%s selectionArgs=%s",
                uri, selection, Arrays.toString(selectionArgs)));
        notifyDataCached(uri, selection, selectionArgs, getValidTtl());
    }

    @Override
    protected Uri doInsert(Uri uri, ContentValues contentValues) throws Exception {
        Log.d(getTag(), String.format("insert uri=%s", uri));

        long localId = preInsertRecords(contentValues);
        Uri insertedUri = ContentUris.withAppendedId(uri, localId);
        getContext().getContentResolver().notifyChange(insertedUri, null);
        contentValues.put(Columns.LOCAL_ID, localId);
        ApiServiceHelper.doPost(getContext(), getServiceClass(), insertedUri, contentValues);
        Log.d(getTag(), " insertedUri=" + insertedUri + " getServiceClass=" + getServiceClass().getCanonicalName());
        return insertedUri;
    }

    @Override
    protected void onInsertResponse(Uri uri, ContentValues contentValues, ContentValues[] response) throws Exception {
        int responseLength = response == null ? 0 : response.length;
        Log.d(getTag(), String.format("onInsertResponse uri=%s response.length=%d",
                uri, response == null ? 0 : response.length));

        if (responseLength > 0) {
            insertOrUpdateReadyRecords(response);

            Log.d(getTag(), String.format("notifyDataCached uri=%s selection=null selectionArgs=null",
                    uri));
            notifyDataCached(responseLength > 1 ? getProviderUri() : uri, null, null, getValidTtl());
        }
    }

    @Override
    protected int doUpdate(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) throws Exception {
        Log.d(getTag(), String.format("update uri=%s selection=%s selectionArgs=%s", uri,
                selection, Arrays.toString(selectionArgs)));

        long rowsAffected = 0;
        rowsAffected = preUpdateRecords(contentValues, selection, selectionArgs);

        getContext().getContentResolver().notifyChange(uri, null);
        ApiServiceHelper.doPut(getContext(), getServiceClass(), uri, contentValues, selection, selectionArgs);
        Log.d(getTag(), " getServiceClass=" + getServiceClass().getCanonicalName() + " rowsAffected: " + rowsAffected);
        return (int) rowsAffected;
    }

    @Override
    protected void onUpdateResponse(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs, ContentValues[] response) throws Exception {
        int responseLength = response == null ? 0 : response.length;
        Log.d(getTag(), String.format(
                "onUpdateResponse uri=%s selection=%s selectionArgs=%s response.length=%d",
                uri, selection, Arrays.toString(selectionArgs),
                response == null ? 0 : response.length));

        if (responseLength > 0) {
            insertOrUpdateReadyRecords(response);

            Log.d(getTag(), String.format("notifyDataCached uri=%s selection=%s selectionArgs=%s",
                    uri, selection, Arrays.toString(selectionArgs)));
            notifyDataCached(uri, selection, selectionArgs, getValidTtl());
        }
    }

    @Override
    protected int doDelete(Uri uri, String selection, String[] selectionArgs) throws Exception {
        Log.d(getTag(), String.format("delete uri=%s selection=%s selectionArgs=%s", uri,
                selection, Arrays.toString(selectionArgs)));

        int rowsAffected = 0;
        ContentValues contentValues = new ContentValues();
        rowsAffected = preDeleteRecords(contentValues, selection, selectionArgs);

        getContext().getContentResolver().notifyChange(uri, null);
        ApiServiceHelper.doDelete(getContext(), getServiceClass(), uri, selection, selectionArgs);
        return rowsAffected;
    }

    @Override
    protected void onDeleteResponse(Uri uri, String selection, String[] selectionArgs) throws Exception {
        Log.d(getTag(), String.format(
                "onDeleteResponse uri=%s selection=%s selectionArgs=%s",
                uri, selection, Arrays.toString(selectionArgs)));

        deleteRecordsFromCache(selection, selectionArgs);

        Log.d(getTag(), String.format("notifyDataCached uri=%s selection=%s selectionArgs=%s",
                uri, selection, Arrays.toString(selectionArgs)));
        notifyDataCached(uri, selection, selectionArgs, getValidTtl());
    }

    /* ******************************************************* */

    /**
     * Save/override rows in cache and set him status INSERTING
     *
     * @param contentValues
     * @throws Exception
     */
    protected long preInsertRecords(ContentValues contentValues) throws Exception {
        SQLiteDatabase db = getHelper().getWritableDatabase();
        long rowsAffected = 0;

        try {
            db.beginTransaction();
            setContentValueStatus(contentValues, ContentStatus.INSERTING);
            rowsAffected = insertOrUpdateRecord(db, contentValues);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        return rowsAffected;
    }


    /**
     * Save/override rows in cache and set him status UPDATING
     *
     * @param contentValues
     * @param selection
     * @param selectionArgs
     * @return
     * @throws Exception
     */
    protected int preUpdateRecords(ContentValues contentValues, String selection, String[] selectionArgs) throws Exception {
        setContentValueStatus(contentValues, ContentStatus.UPDATING);
        return updateRecordsInCache(contentValues, selection, selectionArgs);
    }

    /**
     * Mark rows in cache for deleting
     *
     * @param contentValues
     * @param selection
     * @param selectionArgs
     * @return
     * @throws Exception
     */
    protected int preDeleteRecords(ContentValues contentValues, String selection, String[] selectionArgs) throws Exception {
        setContentValueStatus(contentValues, ContentStatus.DELETING);
        return updateRecordsInCache(contentValues, selection, selectionArgs);
    }

    private int updateRecordsInCache(ContentValues contentValues, String selection, String[] selectionArgs) {
        SQLiteDatabase db = getHelper().getWritableDatabase();

        int rowsAffected = 0;
        try {
            db.beginTransaction();
            rowsAffected = db.update(getTableName(), contentValues, selection, selectionArgs);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        return rowsAffected;
    }

    /* ******************************************************* */

    /**
     * Save array of rows (ContentValues) and set him READY status
     * All this is performed in one transaction
     *
     * @param contentValues
     * @throws Exception
     */
    protected long insertOrUpdateReadyRecords(ContentValues[] contentValues) throws Exception {
        Log.d(getTag(), " insertOrUpdateReadyRecords");

        if (contentValues == null)
            return 0;

        SQLiteDatabase db = getHelper().getWritableDatabase();
        try {
            db.beginTransaction();

            for (ContentValues row : contentValues) {
                setContentValueStatus(row, ContentStatus.READY);
                insertOrUpdateRecord(db, row);
            }

            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }

        return contentValues.length;
    }

    /**
     * Finally delete records from cache
     *
     * @param selection
     * @param selectionArgs
     * @throws Exception
     */
    protected void deleteRecordsFromCache(String selection, String[] selectionArgs) throws Exception {
        SQLiteDatabase db = getHelper().getWritableDatabase();

        try {
            db.beginTransaction();
            db.delete(getTableName(), selection, selectionArgs);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    /* ******************************************************* */


    protected long insertOrUpdateRecord(SQLiteDatabase db, ContentValues contentValues) throws Exception {
        if (!contentValues.containsKey(Columns.LOCAL_ID)) {
            Long localId = getLocalIdByUniqueKey(db, contentValues);
            if (localId != null) {
                contentValues.put(Columns.LOCAL_ID, localId);
            }
        }

        boolean inserting = !contentValues.containsKey(Columns.LOCAL_ID);

        if (inserting) {
            initDefaultValues(contentValues);
        }

        ContentValues contentValuesEnc = new ContentValues(contentValues);
        if (inserting) {
            Log.d(getTag(), " getTableName()=" + getTableName() + " insert " + contentValues.toString());
            return db.insert(getTableName(), null, contentValuesEnc);
        } else {
            Log.d(getTag(), " getTableName()=" + getTableName() + " update " + contentValues.toString());
            return db.update(getTableName(), contentValuesEnc, "_id=?",
                    new String[]{contentValuesEnc.getAsString(Columns.LOCAL_ID)});
        }
    }

    protected void initDefaultValues(ContentValues contentValues) {
    }

    protected abstract Long getLocalIdByUniqueKey(SQLiteDatabase db, ContentValues contentValues);

    /* ******************************************************* */

    protected void setContentValueStatus(ContentValues contentValues, ContentStatus status) {
        contentValues.put(Columns.LOCAL_STATUS, status.toString());
    }
}
