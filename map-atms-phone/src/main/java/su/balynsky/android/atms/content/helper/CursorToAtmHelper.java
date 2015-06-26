package su.balynsky.android.atms.content.helper;

import android.database.Cursor;
import su.balynsky.android.atms.content.provider.AtmsContentProvider.Columns;
import su.balynsky.android.atms.model.atm.AtmInfo;

/**
 * @author Sergey Balynsky
 *         17.02.2015 15:56
 */
public class CursorToAtmHelper {
    public static AtmInfo getAtmInfo(Cursor data) {
        if (data == null) {
            return null;
        }
        AtmInfo result = new AtmInfo();
        result.setLatitude(data.getString(data.getColumnIndex(Columns.LATITUDE)));
        result.setLongitude(data.getString(data.getColumnIndex(Columns.LONGTITUDE)));
        result.setAddressL1(data.getString(data.getColumnIndex(Columns.ADDRESSL1)));
        result.setAddressL2(data.getString(data.getColumnIndex(Columns.ADDRESSL2)));
        result.setCity(data.getString(data.getColumnIndex(Columns.CITY)));
        result.setCode(data.getString(data.getColumnIndex(Columns.CODE)));
        result.setCommissionFlag(Boolean.parseBoolean(data.getString(data.getColumnIndex(Columns.COMISSION))));
        result.setDollarsFlag(Boolean.parseBoolean(data.getString(data.getColumnIndex(Columns.DOLLARS))));
        result.setHours(data.getString(data.getColumnIndex(Columns.HOURS)));
        result.setName(data.getString(data.getColumnIndex(Columns.NAME)));
        result.setState(data.getString(data.getColumnIndex(Columns.STATE)));
        result.setTelephone(data.getString(data.getColumnIndex(Columns.TELEPHONE)));
        return result;
    }
}
