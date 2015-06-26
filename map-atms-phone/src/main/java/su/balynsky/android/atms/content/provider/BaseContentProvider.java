package su.balynsky.android.atms.content.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import su.balynsky.android.atms.content.cache.QueryCacheManager;
import su.balynsky.android.atms.content.common.ExtrasCursor;
import su.balynsky.android.atms.content.database.PartialCursor;
import su.balynsky.android.atms.database.AtmsDatabaseHelper;

/**
 * @author Sergey Balynsky
 *         on 19.01.2015.
 */
public abstract class BaseContentProvider extends ContentProvider {
    private static final String TAG = BaseContentProvider.class.getCanonicalName();

    public enum ContentStatus {
        READY, INSERTING, UPDATING, DELETING
    }

    private static final String DATABASE_NAME = "atms.db";
    private static final int DATABASE_VERSION = 1;

    private static final String CURSOR_NOT_READY_FLAG = "cursorNotReady";
    public static final String QUERY_RESPONSE_METHOD = "respondToQuery";
    public static final String INSERT_RESPONSE_METHOD = "respondToInsert";
    public static final String UPDATE_RESPONSE_METHOD = "respondToUpdate";
    public static final String DELETE_RESPONSE_METHOD = "respondToDelete";

    public static final String PROJECTION_PARAM = "projection";
    public static final String SELECTION_PARAM = "selection";
    public static final String SELECTION_ARGS_PARAM = "selectionArgs";
    public static final String ORDER_BY_PARAM = "orderBy";
    public static final String CONTENT_VALUES_PARAM = "contentValues";
    public static final String RESPONSE_PARAM = "contentValuesArray";
    public static final String SERVICE_TOKEN_PARAM = "serviceToken";

    private static volatile SQLiteDatabase.CursorFactory cursorFactory;
    private static volatile SQLiteOpenHelper helper;


    public boolean onCreate() {
        if (helper == null) {
            synchronized (this) {
                if (helper == null) {
                    helper = new AtmsDatabaseHelper(getContext(), DATABASE_NAME, getCursorFactory(getContext().getApplicationContext()), DATABASE_VERSION) {
                        @Override
                        public void initializeDataBase() {
                            super.initializeDataBase();
                            //Initialize other database
                        }
                    };
                    ((AtmsDatabaseHelper) helper).initializeDataBase();
                }
            }
        }
        return true;
    }

    protected SQLiteOpenHelper getHelper() {
        return helper;
    }

    @Override
    public Bundle call(String method, String arg, Bundle extras) {
        if (method == null)
            throw new IllegalArgumentException("method can't be null");

        if (arg == null)
            throw new IllegalArgumentException("arg can't be null");

        if (extras == null)
            throw new IllegalArgumentException("extras can't be null");

        Uri uri = Uri.parse(arg);
        String[] projection = extras.getStringArray(PROJECTION_PARAM);
        String selection = extras.getString(SELECTION_PARAM);
        String[] selectionArgs = extras.getStringArray(SELECTION_ARGS_PARAM);
        String orderBy = extras.getString(ORDER_BY_PARAM);
        ContentValues contentValues = extras.getParcelable(CONTENT_VALUES_PARAM);
        ContentValues[] response = (ContentValues[]) extras.getParcelableArray(RESPONSE_PARAM);
        Bundle serviceToken = extras.getBundle(SERVICE_TOKEN_PARAM);

        try {
            if (method.compareTo(QUERY_RESPONSE_METHOD) == 0) {
                onQueryResponse(uri, projection, selection, selectionArgs, orderBy, response, serviceToken);
            } else if (method.compareTo(INSERT_RESPONSE_METHOD) == 0) {
                onInsertResponse(uri, contentValues, response);
            } else if (method.compareTo(UPDATE_RESPONSE_METHOD) == 0) {
                onUpdateResponse(uri, contentValues, selection, selectionArgs, response);
            } else if (method.compareTo(DELETE_RESPONSE_METHOD) == 0) {
                onDeleteResponse(uri, selection, selectionArgs);
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }

        return null;
    }

    protected abstract Cursor doQuery(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) throws Exception;

    protected abstract void onQueryResponse(Uri uri, String[] projection, String selection, String[] selectionArgs, String orderBy, ContentValues[] response, Bundle serviceToken) throws Exception;

    protected abstract Uri doInsert(Uri uri, ContentValues values) throws Exception;

    protected abstract void onInsertResponse(Uri uri, ContentValues contentValues, ContentValues[] response) throws Exception;

    protected abstract int doUpdate(Uri uri, ContentValues values, String selection, String[] selectionArgs) throws Exception;

    protected abstract void onUpdateResponse(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs, ContentValues[] response) throws Exception;

    protected abstract int doDelete(Uri uri, String selection, String[] selectionArgs) throws Exception;

    protected abstract void onDeleteResponse(Uri uri, String selection, String[] selectionArgs) throws Exception;


    protected final Cursor getLoadingCursor(String tableName, Uri uri) {
        Cursor cursor = new ExtrasCursor();//getHelper().getWritableDatabase().query(tableName, null, "1=2", null, null, null, null);
        Log.d(TAG, "getLoadingCursor cursor=[" + DatabaseUtils.dumpCursorToString(cursor) + "]");
        Bundle extras = new Bundle();
        extras.putBoolean(CURSOR_NOT_READY_FLAG, true);
        cursor.respond(extras);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        Log.d(TAG, "getLoadingCursor extras=[" + cursor.getExtras() + "]");
        return cursor;
    }

    public static boolean isCursorReady(Cursor cursor) {
        return cursor.getExtras() != null && !cursor.getExtras().getBoolean(CURSOR_NOT_READY_FLAG, false);
    }

    private SQLiteDatabase.CursorFactory getCursorFactory(Context context) {
        if (cursorFactory == null) {
            synchronized (this) {
                if (cursorFactory == null) {
                    cursorFactory = null;
                }
            }
        }

        return cursorFactory;
    }

    protected final Cursor prepareCursor(Cursor cursor, Uri uri, String selection, String[] selectionArgs) {
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        if (QueryCacheManager.isCachePartialAvailable(uri, selection, selectionArgs)) {
            ((PartialCursor) cursor).setCursorPartial(true);
        }
        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        try {
            return doQuery(uri, projection, selection, selectionArgs, sortOrder);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
            ;
        }

        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        try {
            return doInsert(uri, values);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }

        return null;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        try {
            return doUpdate(uri, values, selection, selectionArgs);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }

        return 0;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        try {
            return doDelete(uri, selection, selectionArgs);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }

        return 0;
    }

    protected void notifyDataCached(Uri uri, String selection, String[] selectionArgs, long ttl) {
        QueryCacheManager.markDataCached(uri, selection, selectionArgs, ttl);
        getContext().getContentResolver().notifyChange(uri, null);
    }

}
