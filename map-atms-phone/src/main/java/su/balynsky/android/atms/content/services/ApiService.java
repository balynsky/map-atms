package su.balynsky.android.atms.content.services;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import com.google.inject.Inject;
import roboguice.service.RoboIntentService;
import su.balynsky.android.atms.bus.BusClient;
import su.balynsky.android.atms.exceptions.APIException;
import su.balynsky.android.atms.exceptions.TechnicalException;
import su.balynsky.android.atms.phone.R;

import java.io.IOException;

/**
 * @author Sergey Balynsky
 *         on 26.01.2015.
 */
public abstract class ApiService extends RoboIntentService {

    private static final String TAG = ApiService.class.getCanonicalName();

    public static final String ACTION_PARAM = "action";
    public static final String URI_PARAM = "uri";
    public static final String PROJECTION_PARAM = "projection";
    public static final String SELECTION_PARAM = "selection";
    public static final String SELECTION_ARGS_PARAM = "selectionArgs";
    public static final String ORDER_BY_PARAM = "orderBy";
    public static final String CONTENT_VALUES_PARAM = "contentValues";
    public static final String SERVICE_TOKEN_PARAM = "serviceToken";

    public static final int ACTION_UNKNOWN = -1;
    public static final int ACTION_GET = 0;
    public static final int ACTION_POST = 1;
    public static final int ACTION_PUT = 2;
    public static final int ACTION_DELETE = 3;

    @Inject
    private BusClient busClient;

    private volatile boolean isInitialized = false;

    public ApiService(String name) {
        super(name);
        Log.d(TAG, "name=" + name);
    }

    /**
     * Any initialization that can result in exception can be done here
     *
     * @throws TechnicalException
     */
    protected void onInitialize() throws TechnicalException {
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, "onHandleIntent");

        try {
            if (!isInitialized) {
                synchronized (ApiService.class.getClass()) {
                    if (!isInitialized) {
                        onInitialize();
                        isInitialized = true;
                    }
                }
            }

            int action = intent.getIntExtra(ACTION_PARAM, ACTION_UNKNOWN);
            String uriStr = intent.getStringExtra(URI_PARAM);
            String[] projection = intent.getStringArrayExtra(PROJECTION_PARAM);
            String selection = intent.getStringExtra(SELECTION_PARAM);
            String[] selectionArgs = intent.getStringArrayExtra(SELECTION_ARGS_PARAM);
            String orderBy = intent.getStringExtra(ORDER_BY_PARAM);
            ContentValues contentValues = intent.getParcelableExtra(CONTENT_VALUES_PARAM);
            Bundle serviceToken = intent.getBundleExtra(SERVICE_TOKEN_PARAM);

            Uri uri = uriStr != null ? Uri.parse(uriStr) : null;

            switch (action) {
                case ACTION_GET:
                    doGet(uri, projection, selection, selectionArgs, orderBy, serviceToken);
                    break;
                case ACTION_POST:
                    doPost(uri, contentValues);
                    break;
                case ACTION_PUT:
                    doPut(uri, contentValues, selection, selectionArgs);
                    break;
                case ACTION_DELETE:
                    doDelete(uri, selection, selectionArgs);
                    break;
                default:
                    throw new APIException(String.format("Action %d is not supported", action));
            }
        } catch (IOException e) {
            e.printStackTrace();
            busClient.post(new APIException(getString(R.string.common_error_network_problem), e));
        } catch (TechnicalException e) {
            e.printStackTrace();
            busClient.post(new APIException(getString(R.string.common_error_unknown_technical_problem), e));
        } catch (APIException e) {
            e.printStackTrace();
            busClient.post(e);
        }
    }

    protected abstract void doGet(Uri uri, String[] projection, String selection, String[] selectionArgs, String orderBy, Bundle serviceToken) throws IOException, TechnicalException, APIException;

    protected abstract void doPost(Uri uri, ContentValues contentValues) throws IOException, TechnicalException, APIException;

    protected abstract void doPut(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) throws IOException, TechnicalException, APIException;

    protected abstract void doDelete(Uri uri, String selection, String[] selectionArgs) throws IOException, TechnicalException, APIException;

}
