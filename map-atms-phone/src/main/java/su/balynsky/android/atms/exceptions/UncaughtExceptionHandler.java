package su.balynsky.android.atms.exceptions;

import com.splunk.mint.Mint;
import su.balynsky.android.atms.AtmsApplication;

/**
 * @author Sergey Balynsky
 *         19.02.2015 15:53
 */
public class UncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {

    private Thread.UncaughtExceptionHandler originalUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();

    public void uncaughtException(Thread thread, Throwable throwable) {

        throwable.printStackTrace();

        if (Exception.class.isInstance(throwable)) {
            if (AtmsApplication.BUGSENSE_ENABLED) {
                Mint.logException((Exception) throwable);
                Mint.flush();
            }
        }

        originalUncaughtExceptionHandler.uncaughtException(thread, throwable);
    }

}
