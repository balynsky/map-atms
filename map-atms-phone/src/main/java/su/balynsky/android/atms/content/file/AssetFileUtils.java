package su.balynsky.android.atms.content.file;

import android.content.Context;

import java.io.IOException;
import java.util.Properties;

/**
 * @author Sergey Balynsky
 *         06.02.2015 17:42
 */
public class AssetFileUtils {
    public static final String STARTUP_PROPERTIES = "startup.properties";

    public static Properties loadStartProperties(Context context) throws IOException {
        Properties result = new Properties();
        result.load(context.getAssets().open(STARTUP_PROPERTIES));
        return result;
    }

}
