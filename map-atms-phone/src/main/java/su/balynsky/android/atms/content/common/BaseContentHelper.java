package su.balynsky.android.atms.content.common;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import su.balynsky.android.atms.content.provider.BaseContentProvider;

/**
 * @author Sergey Balynsky
 *         on 28.01.2015.
 */
public class BaseContentHelper {
    private static final String TAG = BaseContentHelper.class.getCanonicalName();

    public static void respondToQuery(Context context, Uri uri, String[] projection, String selection, String[] selectionArgs, String orderBy, ContentValues[] response, Bundle serviceToken) {
        Log.d(TAG, "BaseContentHelper.respondToQuery");
        Bundle extras = new Bundle();
        extras.putStringArray(BaseContentProvider.PROJECTION_PARAM, projection);
        extras.putString(BaseContentProvider.SELECTION_PARAM, selection);
        extras.putStringArray(BaseContentProvider.SELECTION_ARGS_PARAM, selectionArgs);
        extras.putString(BaseContentProvider.ORDER_BY_PARAM, orderBy);
        extras.putParcelableArray(BaseContentProvider.RESPONSE_PARAM, response);
        extras.putBundle(BaseContentProvider.SERVICE_TOKEN_PARAM, serviceToken);
        context.getContentResolver().call(uri, BaseContentProvider.QUERY_RESPONSE_METHOD, uri.toString(), extras);
    }

    public static void respondToInsert(Context context, Uri uri, ContentValues[] response) {
        Log.d(TAG, "BaseContentHelper.respondToInsert");
        Bundle extras = new Bundle();
        extras.putParcelableArray(BaseContentProvider.RESPONSE_PARAM, response);
        context.getContentResolver().call(uri, BaseContentProvider.INSERT_RESPONSE_METHOD, uri.toString(), extras);
    }

    public static void respondToUpdate(Context context, Uri uri, ContentValues contentValues, String selection, String[] selectionArgs, ContentValues[] response) {
        Log.d(TAG, "BaseContentHelper.respondToUpdate");
        Bundle extras = new Bundle();
        extras.putParcelable(BaseContentProvider.CONTENT_VALUES_PARAM, contentValues);
        extras.putString(BaseContentProvider.SELECTION_PARAM, selection);
        extras.putStringArray(BaseContentProvider.SELECTION_ARGS_PARAM, selectionArgs);
        extras.putParcelableArray(BaseContentProvider.RESPONSE_PARAM, response);
        context.getContentResolver().call(uri, BaseContentProvider.UPDATE_RESPONSE_METHOD, uri.toString(), extras);
    }

    public static void respondToDelete(Context context, Uri uri, String selection, String[] selectionArgs) {
        Log.d(TAG, "BaseContentHelper.respondToDelete");
        Bundle extras = new Bundle();
        extras.putString(BaseContentProvider.SELECTION_PARAM, selection);
        extras.putStringArray(BaseContentProvider.SELECTION_ARGS_PARAM, selectionArgs);
        context.getContentResolver().call(uri, BaseContentProvider.DELETE_RESPONSE_METHOD, uri.toString(), extras);
    }
}
