package su.balynsky.android.atms.preferences;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * @author Sergey Balynsky
 *         06.02.2015 15:13
 */
public class SharedPreferencesHelper {

    public static SharedPreferences getPrivateSharedPreferences(Context context) {
        return context.getSharedPreferences(context.getApplicationInfo().name, Context.MODE_PRIVATE);
    }

}
