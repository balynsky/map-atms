package su.balynsky.android.atms.content.database;

import org.roboguice.shaded.goole.common.base.Joiner;
import su.balynsky.android.atms.content.common.SelectionParsingUtils;
import su.balynsky.android.atms.content.provider.AbstractContentProvider;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Sergey Balynsky
 *         05.02.2015 14:09
 */
public class SQLHelper {

    protected static String getIdSelection() {
        return AbstractContentProvider.Columns.LOCAL_ID + "=?";
    }

    protected static String[] getIdSelectionArgs(long id) {
        return new String[]{Long.toString(id)};
    }

    @Deprecated
    public static String addCondition(String selection, String condition) {
        if (selection.length() > 0) {
            selection = selection.concat(SelectionParsingUtils.SQL_AND);
        }

        selection = selection.concat(condition);
        return selection;
    }

    public static StringBuilder addCondition(StringBuilder selection, String condition, String compareSign) {
        if (selection.length() > 0) {
            selection.append(SelectionParsingUtils.SQL_AND);
        }
        selection.append(condition).append(compareSign);
        return selection;
    }

    public static StringBuilder addInCondition(StringBuilder selection, String condition, int count) {
        if (selection.length() > 0) {
            selection.append(SelectionParsingUtils.SQL_AND);
        }
        String[] inStr = new String[count];
        for (int i = 0; i < count; i++) {
            inStr[i] = "?";
        }

        selection.append(condition).append(String.format(SelectionParsingUtils.SQL_IN,
                Joiner.on(',').join(inStr)));
        return selection;
    }

    public static <T> String listToEnumeration(List<T> list, boolean wildcard) {
        String enumeration = "";

        for (T item : list) {
            if (enumeration.length() > 0) {
                enumeration = enumeration.concat(",");
            }

            enumeration = enumeration.concat(wildcard ? "?" : item.toString());
        }

        return enumeration;
    }

    public static <T> List<String> listToStringList(List<T> list) {
        List<String> stringList = new LinkedList<String>();

        for (T item : list) {
            stringList.add(item.toString());
        }

        return stringList;
    }

}
