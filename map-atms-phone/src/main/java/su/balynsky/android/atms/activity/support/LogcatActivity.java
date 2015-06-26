package su.balynsky.android.atms.activity.support;

import android.os.Bundle;
import su.balynsky.android.atms.activity.NavigationDrawerBaseActivity;
import su.balynsky.android.atms.activity.common.DrawerMenuItemIdentifier;
import su.balynsky.android.atms.phone.R;

/**
 * @author Sergey Balynsky
 *         19.02.2015 11:27
 */
public class LogcatActivity extends NavigationDrawerBaseActivity {
    @Override
    protected Integer getSelectedItem() {
        return DrawerMenuItemIdentifier.LOGCAT_MENU;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logcat);
    }
}
