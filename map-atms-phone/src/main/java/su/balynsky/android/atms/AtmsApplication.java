package su.balynsky.android.atms;

import android.app.Application;
import android.content.Context;
import com.splunk.mint.Mint;
import roboguice.RoboGuice;
import su.balynsky.android.atms.exceptions.UncaughtExceptionHandler;

/**
 * @author Sergey Balynsky
 *         on 20.01.2015.
 */
public class AtmsApplication extends Application {
    public static final Boolean BUGSENSE_ENABLED = true;
    public static final String API_KEY = "9b6879e4";

    static {
        RoboGuice.setUseAnnotationDatabases(false);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (BUGSENSE_ENABLED) {
            Mint.initAndStartSession(AtmsApplication.this, API_KEY);
            Mint.enableLogging(true);
            Mint.setLogging(1000);
            Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler());
        }
    }


    @Override
    public void onTerminate() {
        super.onTerminate();
        if (BUGSENSE_ENABLED) {
            Mint.closeSession(AtmsApplication.this);
        }
    }

    /**
     * We need to register an injector before our content providers start 'cause
     * we're doing some DI during content providers' onCreate
     *
     * @param base
     */
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }
}
