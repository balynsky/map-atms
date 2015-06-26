package su.balynsky.android.atms.content.helper;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import su.balynsky.android.atms.content.common.SelectionHolder;
import su.balynsky.android.atms.content.common.SelectionParsingUtils;
import su.balynsky.android.atms.content.database.SQLHelper;
import su.balynsky.android.atms.content.provider.AtmsContentProvider;
import su.balynsky.android.atms.content.provider.AtmsContentProvider.Columns;
import su.balynsky.android.atms.database.AtmsTable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Sergey Balynsky
 *         05.02.2015 13:58
 */
public final class AtmsProviderHelper extends SQLHelper {
    private static final String TAG = AtmsProviderHelper.class.getCanonicalName();
    // Квадрат радиуса окружности в градусах,
    // в пределах которой показываем банкоматы
    private static final String MAX_DISTANCE = "4";

    public static Loader<Cursor> getLoader(Context context, Bundle args) {
        Log.d(TAG, "su.balynsky.android.atms.content.helper.AtmsProviderHelper.getLoader");
        SelectionHolder selectionHolder = getFilterSelection(args);
        Log.d(TAG, "selection = " + selectionHolder.getSelection());
        Log.d(TAG, "selectionArgs = " + Arrays.toString(selectionHolder.getSelectionArgs()));

        return new CursorLoader(context,
                AtmsContentProvider.ATMS_URI,
                Columns.getColumnList(), selectionHolder.getSelection(),
                selectionHolder.getSelectionArgs(), null);
    }

    /**
     * Получение условий для выбора банкоматов
     */
    private static SelectionHolder getFilterSelection(Bundle args) {
        StringBuilder selectionBuilder = new StringBuilder();
        List<String> selectionArgs = new ArrayList<String>();
        //String latitude, String longitude, Boolean commissionFlag, Boolean restrictDistance
        if (args.get(Columns.LATITUDE) != null && args.get(Columns.LONGTITUDE) != null) {
            appendDistanceCalculationToSelection(args.getString(Columns.LATITUDE), args.getString(Columns.LONGTITUDE), selectionBuilder);
        }
        if (args.get(Columns.COMISSION) != null) {
            addCondition(selectionBuilder, Columns.COMISSION, SelectionParsingUtils.SQL_EQ);
            selectionArgs.add(args.getString(Columns.COMISSION));
        }
        return new SelectionHolder(selectionBuilder.toString(), selectionArgs.toArray(new String[0]));
    }

    /**
     * Добавить в WHERE clause вычисление расстояния от текущей точки до
     * банкомата
     *
     * @param latitude
     * @param longitude
     * @param selection
     */
    private static void appendDistanceCalculationToSelection(String latitude, String longitude, StringBuilder selection) {
        StringBuilder latitudeDiff = new StringBuilder().append("(").append(AtmsTable.COLUMN_LATITUDE).append(" - ")
                .append(latitude).append(") ");
        StringBuilder longitudeDiff = new StringBuilder().append("(").append(AtmsTable.COLUMN_LONGITUDE).append(" - ")
                .append(longitude).append(") ");
        selection.append(latitudeDiff).append(" * ").append(latitudeDiff).append(" + ").append(longitudeDiff)
                .append(" * ").append(longitudeDiff).append(" <= ").append(MAX_DISTANCE);
    }


}

