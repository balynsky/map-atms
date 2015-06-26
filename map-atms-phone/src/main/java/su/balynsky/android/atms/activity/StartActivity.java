package su.balynsky.android.atms.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import su.balynsky.android.atms.phone.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * @author Sergey Balynsky
 *         on 23.01.2015.
 */
public class StartActivity extends Activity {

    private static final int SPLASH_SCREEN_SHOW_DURATION = 3000;    // 2 секунды

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        final Activity activity = this;

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                startActivity(new Intent(activity, GoogleMapFragmentActivity.class));
                finish();
            }
        }, SPLASH_SCREEN_SHOW_DURATION);
    }

}
