package su.balynsky.android.atms.listeners.common;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.View;

import java.lang.ref.WeakReference;

/**
 * @author Sergey Balynsky
 *         18.02.2015 16:17
 */
public class OpenWebPageOnClickListener implements View.OnClickListener {

    private WeakReference<Activity> activity;
    private String url;

    public OpenWebPageOnClickListener(Activity activity, String url) {
        super();
        this.activity = new WeakReference<Activity>(activity);
        this.url = url;
    }

    public void onClick(View arg0) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        activity.get().startActivity(intent);
    }

}
