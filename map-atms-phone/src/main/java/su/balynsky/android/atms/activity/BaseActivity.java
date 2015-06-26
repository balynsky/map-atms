package su.balynsky.android.atms.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import com.google.inject.Inject;
import com.squareup.otto.Bus;
import roboguice.activity.RoboActionBarActivity;
import su.balynsky.android.atms.ExceptionSubscriber;

/**
 * @author Sergey Balynsky
 *         on 20.01.2015.
 */
public class BaseActivity extends RoboActionBarActivity {
    private static final String TAG = BaseActivity.class.getCanonicalName();
    @Inject
    private Bus bus;

    @Inject
    private ExceptionSubscriber subscriber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "BaseActivity.onCreate");
        super.onCreate(savedInstanceState);
        View content = getContentView(savedInstanceState);
        if (content != null) {
            Log.d(TAG, "BaseActivity.onCreate 2");
            //contentContainer.addView(content);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        bus.register(subscriber);
        bus.register(this);
    }

    protected View getContentView(Bundle savedInstanceState) {
        return null;
    }

    @Override
    protected void onPause() {
        bus.unregister(this);
        bus.unregister(subscriber);
        super.onPause();
    }

    protected Bus getBus() {
        return bus;
    }

    public ViewGroup getContentContainer() {
        return null;
    }

}
