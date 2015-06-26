package su.balynsky.android.atms.content.api;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import su.balynsky.android.atms.content.services.ApiService;

/**
 * @author Sergey Balynsky
 *         on 26.01.2015.
 */
public class ApiServiceHelper {
    private static final String TAG = ApiServiceHelper.class.getCanonicalName();

    public static void doGet(Context context, Class<?> serviceClass, Uri uri, String[] projection, String selection, String[] selectionArgs, String orderBy, Bundle serviceToken) {
        Log.d(TAG, "ApiServiceHelper.doGet");
        Intent intent = new Intent(context, serviceClass);
        intent.putExtra(ApiService.ACTION_PARAM, ApiService.ACTION_GET);
        intent.putExtra(ApiService.URI_PARAM, uri != null ? uri.toString() : null);
        intent.putExtra(ApiService.PROJECTION_PARAM, projection);
        intent.putExtra(ApiService.SELECTION_PARAM, selection);
        intent.putExtra(ApiService.SELECTION_ARGS_PARAM, selectionArgs);
        intent.putExtra(ApiService.ORDER_BY_PARAM, orderBy);
        intent.putExtra(ApiService.SERVICE_TOKEN_PARAM, serviceToken);
        context.startService(intent);
    }

    public static void doPost(Context context, Class<?> serviceClass, Uri uri, ContentValues contentValues) {
        Log.d(TAG, "ApiServiceHelper.doPost");
        Intent intent = new Intent(context, serviceClass);
        intent.putExtra(ApiService.ACTION_PARAM, ApiService.ACTION_POST);
        intent.putExtra(ApiService.URI_PARAM, uri != null ? uri.toString() : null);
        intent.putExtra(ApiService.CONTENT_VALUES_PARAM, contentValues);
        context.startService(intent);
    }

    public static void doPut(Context context, Class<?> serviceClass, Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        Log.d(TAG, "ApiServiceHelper.doPut");
        Intent intent = new Intent(context, serviceClass);
        intent.putExtra(ApiService.ACTION_PARAM, ApiService.ACTION_PUT);
        intent.putExtra(ApiService.URI_PARAM, uri != null ? uri.toString() : null);
        intent.putExtra(ApiService.CONTENT_VALUES_PARAM, contentValues);
        intent.putExtra(ApiService.SELECTION_PARAM, selection);
        intent.putExtra(ApiService.SELECTION_ARGS_PARAM, selectionArgs);
        context.startService(intent);
    }

    public static void doDelete(Context context, Class<?> serviceClass, Uri uri, String selection, String[] selectionArgs) {
        Log.d(TAG, "ApiServiceHelper.doDelete");
        Intent intent = new Intent(context, serviceClass);
        intent.putExtra(ApiService.ACTION_PARAM, ApiService.ACTION_DELETE);
        intent.putExtra(ApiService.URI_PARAM, uri != null ? uri.toString() : null);
        intent.putExtra(ApiService.SELECTION_PARAM, selection);
        intent.putExtra(ApiService.SELECTION_ARGS_PARAM, selectionArgs);
        context.startService(intent);
    }

}
