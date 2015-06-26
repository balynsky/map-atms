package su.balynsky.android.atms.listeners;

import android.app.Activity;
import android.view.View;

import java.lang.ref.WeakReference;

/**
 * @author Sergey Balynsky
 *         18.02.2015 16:20
 */
public class ExitClickListener implements View.OnClickListener {

    private WeakReference<Activity> activity;

    public ExitClickListener(Activity activity) {
        this.activity = new WeakReference<Activity>(activity);
    }

    public void onClick(View view) {
        activity.get().finish();
        System.exit(0);
    }

}
