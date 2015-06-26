package su.balynsky.android.atms.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;

import java.util.List;

/**
 * @author Sergey Balynsky
 *         on 22.01.2015.
 */
public class PackagesUtils {
    /**
     * Проверяет, установлено ли приложение
     *
     * @param context
     *           - текущий контекст
     * @param packageName
     *           - имя пакета приложения, наличие которого следует проверить
     * @param openMarket
     *           - открывать ли Google Play, если приложение не установлено
     * @return
     */
    public boolean isPackageInstalled(Context context, String packageName, boolean openMarket) {
        boolean result;

        Intent intent = new Intent();
        intent.setPackage(packageName);

        PackageManager pm = context.getPackageManager();
        List<ResolveInfo> infos = pm.queryIntentActivities(intent, 0);

        result = (infos != null && infos.size() > 0);

        if (!result && openMarket) {
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("market://details?id=" + packageName));
        }

        return result;
    }
}
