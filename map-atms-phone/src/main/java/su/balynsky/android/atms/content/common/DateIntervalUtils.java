package su.balynsky.android.atms.content.common;


import java.text.ParseException;
import java.util.Date;

/**
 * @author Sergey Balynsky
 *         on 28.01.2015.
 */
public class DateIntervalUtils {
    public static final String DATES_SEPARATOR = "#";
    public static final String BETWEEN_FORMAT = " BETWEEN ? AND ? ";
    public static final String DATE_INTERVAL_PATTERN = "^\\d{4}-\\d{2}-\\d{2}#\\d{4}-\\d{2}-\\d{2}$";
    public static final String DATETIME_INTERVAL_PATTERN = "^\\d{2}\\.\\d{2}\\.\\d{4} \\d{2}:\\d{2}:\\d{2}#\\d{2}\\.\\d{2}\\.\\d{4} \\d{2}:\\d{2}:\\d{2}$";

    public static String mergeDates(Date dt1, Date dt2) {
        StringBuilder sb = new StringBuilder();
        sb.append(DateUtils.DATE_FORMAT.format(dt1))
                .append(DATES_SEPARATOR)
                .append(DateUtils.DATE_FORMAT.format(dt2));
        return sb.toString();
    }

    public static String[] splitDatesPair(String datesPair) {
        return datesPair.split(DATES_SEPARATOR);
    }

    public static Date[] splitDatesPairToDates(String datesPair) throws ParseException {
        String[] strDates = splitDatesPair(datesPair);
        Date[] dates = new Date[2];
        dates[0] = DateUtils.DATE_FORMAT.parse(strDates[0]);
        dates[1] = DateUtils.DATE_FORMAT.parse(strDates[1]);
        return dates;
    }

}
