package su.balynsky.android.atms.activity;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import roboguice.inject.InjectView;
import su.balynsky.android.atms.activity.common.DrawerMenuItemIdentifier;
import su.balynsky.android.atms.listeners.common.OpenWebPageOnClickListener;
import su.balynsky.android.atms.phone.R;

/**
 * @author Sergey Balynsky
 *         19.02.2015 14:50
 */
public class AboutAppActivity extends NavigationDrawerBaseActivity {

    @InjectView(R.id.about_app_feedback)
    private Button feedback;

    @InjectView(R.id.app_version)
    private TextView applicationVersion;

    @Override
    protected Integer getSelectedItem() {
        return DrawerMenuItemIdentifier.ABOUT_APP_MENU;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_app);

        String appVersion = "0.0.1";
        try {
            PackageInfo pInfo;
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            appVersion = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        applicationVersion.setText(String.format(getString(R.string.about_app_version), appVersion));
        feedback.setOnClickListener(new OpenWebPageOnClickListener(this, getString(R.string.about_app_feedback_url)));
    }

}
