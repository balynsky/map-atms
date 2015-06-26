package su.balynsky.android.atms.content.common;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Sergey Balynsky
 *         on 28.01.2015.
 */
public class DateUtils {
    public static final String DATE_TIME_FORMAT_STRING = "dd.MM.yyyy HH:mm:ss";
    public static final String SHORT_DATE_FORMAT_STRING = "dd.MM.yyyy";
    public static final String HHMM_DATE_FORMAT_STRING = "HH:mm";

    public static final DateFormat DATE_FORMAT = new SimpleDateFormat(DATE_TIME_FORMAT_STRING);
    public static final DateFormat SHORT_DATE_FORMAT = new SimpleDateFormat(SHORT_DATE_FORMAT_STRING);
    public static final DateFormat HHMM_DATE_FORMAT = new SimpleDateFormat(HHMM_DATE_FORMAT_STRING);

    public static final DateFormat DATE_FORMAT_FOR_SIGN_BUFFER = new SimpleDateFormat("yyyyMMdd");

    public static final DateFormat DATE_FORMAT_FOR_LOGON = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    public static String changeDateFormat(String date, DateFormat srcFormat, DateFormat dstFormat) throws ParseException {
        return dstFormat.format(srcFormat.parse(date));
    }

    public static boolean isToday(String date) {
        String today = SHORT_DATE_FORMAT.format(new Date());
        return date.startsWith(today);
    }
}
