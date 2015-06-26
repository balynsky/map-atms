package su.balynsky.android.atms.listeners;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import su.balynsky.android.atms.activity.support.LogcatActivity;

import java.lang.ref.WeakReference;

/**
 * @author Sergey Balynsky
 *         18.02.2015 16:20
 */
public class LogcatOnClickListener implements View.OnClickListener {

    private WeakReference<Activity> activity;

    public LogcatOnClickListener(Activity activity) {
        this.activity = new WeakReference<Activity>(activity);
    }

    public void onClick(View view) {
        Intent intent = new Intent(activity.get(), LogcatActivity.class);
        activity.get().startActivity(intent);
    }

}
