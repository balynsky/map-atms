package su.balynsky.android.atms.content.provider;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;
import org.roboguice.shaded.goole.common.collect.ImmutableMap;
import su.balynsky.android.atms.content.api.ApiServiceHelper;
import su.balynsky.android.atms.content.cache.QueryCacheManager;
import su.balynsky.android.atms.content.common.SelectionHolder;
import su.balynsky.android.atms.content.common.SelectionParsingUtils;
import su.balynsky.android.atms.content.file.AssetFileUtils;
import su.balynsky.android.atms.content.services.AtmsService;
import su.balynsky.android.atms.preferences.SharedPreferencesHelper;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

/**
 * @author Sergey Balynsky
 *         on 19.01.2015.
 */
public class AtmsContentProvider extends AbstractContentProvider {
    public static final String ATMS_TABLE = "atms";
    private static final String AUTHORITY = AtmsContentProvider.class.getCanonicalName();
    private static final String TAG = AtmsContentProvider.class.getCanonicalName();

    public static final Uri ATMS_URI = Uri.parse("content://" + AUTHORITY + "/" + ATMS_TABLE);
    private static final int REFRESH_TTL = 100000; // 100 seconds
    private static final int VALID_TTL = 600000; // 60 minute

    public static final String DATABASE_INI_DATE = "database.initial";
    public static final String DATABASE_STAMP_FORMAT = "yyyy-MM-dd";
    public static final SimpleDateFormat df = new SimpleDateFormat(DATABASE_STAMP_FORMAT);

    public static final String LAST_DATE_CHANGE = "last_date_change";

    public static final class Columns extends AbstractContentProvider.Columns {
        public static final String CODE = "code";
        public static final String LATITUDE = "latitude";
        public static final String LONGTITUDE = "longitude";
        public static final String COMISSION = "commissionFlag";
        public static final String DOLLARS = "dollarsFlag";
        public static final String HOURS = "hours";
        public static final String TELEPHONE = "telephone";
        public static final String NAME = "name";
        public static final String ADDRESSL1 = "addressL1";
        public static final String ADDRESSL2 = "addressL2";
        public static final String CITY = "city";
        public static final String STATE = "state";

        public static String[] getColumnList() {
            return new String[]{LOCAL_ID, LOCAL_STATUS, CODE, LATITUDE, LONGTITUDE, COMISSION, DOLLARS, HOURS, TELEPHONE, NAME, ADDRESSL1, ADDRESSL2, CITY, STATE};
        }
    }

    @Override
    protected String getTag() {
        return TAG;
    }

    @Override
    protected String getTableName() {
        return ATMS_TABLE;
    }

    @Override
    protected int getRefreshTtl() {
        return REFRESH_TTL;
    }

    @Override
    protected int getValidTtl() {
        return VALID_TTL;
    }

    @Override
    protected Uri getProviderUri() {
        return ATMS_URI;
    }

    @Override
    protected Class getServiceClass() {
        return AtmsService.class;
    }

    @Override
    protected Long getLocalIdByUniqueKey(SQLiteDatabase db, ContentValues contentValues) {
        if (contentValues.containsKey(Columns.CODE)) {
            String[] columns = {Columns.LOCAL_ID};
            String selection = String.format("%s='%s'",
                    Columns.CODE, contentValues.getAsString(Columns.CODE));
            Cursor cursor = db.query(getTableName(), columns, selection, null, null, null, null);
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                return cursor.getLong(cursor.getColumnIndex(Columns.LOCAL_ID));
            }
        }
        return null;
    }

    @Override
    protected Cursor doQuery(Uri uri, String[] projection, String selection, String[] selectionArgs, String orderBy) throws Exception {
        Log.d(TAG, "AtmsContentProvider.doQuery");
        Log.d(getTag(), String.format("query uri=%s projection=%s selection=%s selectionArgs=%s orderBy=%s",
                uri, Arrays.toString(projection), selection, Arrays.toString(selectionArgs), orderBy));

        if (QueryCacheManager.isCacheBeingRefreshed(uri, selection, selectionArgs)) {
            // API query has already been started
            Log.d(getTag(), "query: isCacheBeingRefreshed=true");
            return prepareCursor(getLoadingCursor(getTableName(), uri), uri, selection, selectionArgs);
        }

        Long lastChangeDateLong = getLastChangeDate(getContext());
        Date lastChangeDate = new Date(lastChangeDateLong);

        if (!checkDate(lastChangeDate))
            if (true) {
                Log.d(TAG, "request to service");
                QueryCacheManager.markDataBeingRefreshed(uri, selection, selectionArgs, getRefreshTtl());
                SelectionHolder selectionHolder = SelectionParsingUtils.parametersToSelection(ImmutableMap.of(LAST_DATE_CHANGE, new String[]{"" + lastChangeDateLong}), null);
                Log.d(TAG, "ApiServiceHelper.doGet Service class=[" + getServiceClass() + "], uri=[" + uri + "], selection=[" + selectionHolder.getSelection() + "], selectionArgs=[" +
                        Arrays.toString(selectionHolder.getSelectionArgs()) + "]");
                ApiServiceHelper.doGet(getContext(), getServiceClass(), uri, projection,
                        selectionHolder.getSelection(), selectionHolder.getSelectionArgs(), orderBy,
                        QueryCacheManager.getServiceToken(uri, selectionHolder.getSelection(), selectionHolder.getSelectionArgs()));
            }


        Log.d(getTag(), "query: loaded from database");
        return prepareCursor(
                getHelper().getWritableDatabase().query(getTableName(), projection, selection,
                        selectionArgs, null, null, orderBy), uri, selection, selectionArgs);

    }

    private boolean checkDate(Date lastChangeDate) {
        Calendar c1 = Calendar.getInstance(); // today
        Calendar c2 = Calendar.getInstance();
        c2.setTime(lastChangeDate);

        return c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR)
                && c1.get(Calendar.DAY_OF_YEAR) == c2.get(Calendar.DAY_OF_YEAR);
    }

    public static Long getLastChangeDate(Context context) throws IOException, ParseException {
        SharedPreferences preferences = SharedPreferencesHelper.getPrivateSharedPreferences(context);
        long aLong = preferences.getLong(ATMS_TABLE, 0);
        if (aLong == 0) {
            Log.d(TAG, "AtmsContentProvider.getLastChangeDate preferences is null");
            Properties properties = AssetFileUtils.loadStartProperties(context);
            String property = properties.getProperty(DATABASE_INI_DATE);
            Log.d(TAG, "AtmsContentProvider.getLastChangeDate properties " + property);
            Date databaseStamp = df.parse(property);
            return databaseStamp != null ? databaseStamp.getTime() : 0;
        }
        return aLong;
    }

    public static void setLastChangeDate(Context context, Long date) {
        SharedPreferences preferences = SharedPreferencesHelper.getPrivateSharedPreferences(context);
        preferences.edit().putLong(ATMS_TABLE, date);
        preferences.edit().apply();
    }

    @Override
    protected Uri doInsert(Uri uri, ContentValues contentValues) throws Exception {
        setLastChangeDate(getContext(), (new Date()).getTime());
        return super.doInsert(uri, contentValues);
    }

    @Override
    protected int doUpdate(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) throws Exception {
        setLastChangeDate(getContext(), (new Date()).getTime());
        return super.doUpdate(uri, contentValues, selection, selectionArgs);
    }

    @Override
    protected int doDelete(Uri uri, String selection, String[] selectionArgs) throws Exception {
        setLastChangeDate(getContext(), (new Date()).getTime());
        return super.doDelete(uri, selection, selectionArgs);
    }
}
