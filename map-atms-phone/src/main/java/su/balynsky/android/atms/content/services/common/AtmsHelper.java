package su.balynsky.android.atms.content.services.common;

import android.content.ContentValues;
import su.balynsky.android.atms.content.provider.AtmsContentProvider;
import su.balynsky.android.atms.model.atm.AtmInfo;

/**
 * @author Sergey Balynsky
 *         on 28.01.2015.
 */
public class AtmsHelper {
    private final static int UNKNOWN_INDEX = -1;

    private static final String[] accountLimitsColumns = new String[]{
            AtmsContentProvider.Columns.LOCAL_ID,
            AtmsContentProvider.Columns.CODE,
            AtmsContentProvider.Columns.LATITUDE,
            AtmsContentProvider.Columns.LONGTITUDE,
            AtmsContentProvider.Columns.COMISSION,
            AtmsContentProvider.Columns.DOLLARS,
            AtmsContentProvider.Columns.HOURS,
            AtmsContentProvider.Columns.TELEPHONE,
            AtmsContentProvider.Columns.NAME,
            AtmsContentProvider.Columns.ADDRESSL1,
            AtmsContentProvider.Columns.ADDRESSL2,
            AtmsContentProvider.Columns.CITY,
            AtmsContentProvider.Columns.STATE

    };

    public static ContentValues getContentValues(AtmInfo atm) {
        ContentValues contentValues = new ContentValues();

        if (atm.getCode() != null) {
            contentValues.put(AtmsContentProvider.Columns.CODE, atm.getCode());
        }
        if (atm.getLatitude() != null) {
            contentValues.put(AtmsContentProvider.Columns.LATITUDE, atm.getLatitude());
        }
        if (atm.getLongitude() != null) {
            contentValues.put(AtmsContentProvider.Columns.LONGTITUDE, atm.getLongitude());
        }
        if (atm.getCommissionFlag() != null) {
            contentValues.put(AtmsContentProvider.Columns.COMISSION, atm.getCommissionFlag());
        }
        if (atm.getDollarsFlag() != null) {
            contentValues.put(AtmsContentProvider.Columns.DOLLARS, atm.getDollarsFlag());
        }
        if (atm.getHours() != null) {
            contentValues.put(AtmsContentProvider.Columns.HOURS, atm.getHours());
        }
        if (atm.getTelephone() != null) {
            contentValues.put(AtmsContentProvider.Columns.TELEPHONE, atm.getTelephone());
        }
        if (atm.getName() != null) {
            contentValues.put(AtmsContentProvider.Columns.NAME, atm.getName());
        }
        if (atm.getAddressL1() != null) {
            contentValues.put(AtmsContentProvider.Columns.ADDRESSL1, atm.getAddressL1());
        }
        if (atm.getAddressL2() != null) {
            contentValues.put(AtmsContentProvider.Columns.ADDRESSL2, atm.getAddressL2());
        }
        if (atm.getCity() != null) {
            contentValues.put(AtmsContentProvider.Columns.CITY, atm.getCity());
        }
        if (atm.getState() != null) {
            contentValues.put(AtmsContentProvider.Columns.STATE, atm.getState());
        }

        return contentValues;
    }

}
