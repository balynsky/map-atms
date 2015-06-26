package su.balynsky.android.atms.content.common;

import android.util.Log;
import org.roboguice.shaded.goole.common.base.Joiner;

import java.util.*;

/**
 * @author  Sergey Balynsky
 *         on 28.01.2015.
 */
public class SelectionParsingUtils {
    private static final String TAG = SelectionParsingUtils.class.getCanonicalName();

    public static final String SQL_IN = " IN (%s)";
    public static final String SQL_EQ = "=?";
    public static final String SQL_AND = " AND ";
    public static final String SQL_WILDCARD = "?";

    public interface SelectionJoiner {
        String replaceValue(String fieldName, String value);
    }

    private static final String SELECTION_FIELDS_SPLITTER = " [aA][nN][dD] ";
    private static final String SELECTION_VALUE_SPLITTER = "=";
    private static final String SELECTION_IN_SPLITTER = " [iI][nN] ";
    private static final String SELECTION_VALUE_WILDCARD = "?";
    private static final String SELECTION_VALUES_ENUMERATION_SPLITTER = ",";


    private static String getArgumentValue(String[] selectionArgs, int index) {
        if (selectionArgs == null || selectionArgs.length < index) {
            throw new IllegalArgumentException(
                    String.format("selectionArgs is expected to contain %d values at least, but it contains only %d.",
                            index + 1,
                            selectionArgs == null ? 0 : selectionArgs.length));
        }

        return selectionArgs[index++];
    }

    /**
     * Transforms ContentProvider-like protocol of selection and selectionArgs to a key-value map
     * by replacing wildcard question marks in <code>selection</code> with the values from
     * <code>selectionArgs</code>.
     *
     * @param selection     - a string in a form of "field1=? AND field2=?"
     * @param selectionArgs - values to replace wildcard question marks in <code>selection</code> string
     * @return a key-value map produced from <code>selection</code> and <code>selectionArgs</code>
     */
    public static Map<String, String[]> parseSelection(String selection, String[] selectionArgs) {
        Map<String, String[]> result = new HashMap<String, String[]>();

        if (selection != null && selection.trim().length() > 0) {
            int wildcardIndex = 0;
            //String upperSel = selection.toUpperCase();
            String[] selectionFields = selection.split(SELECTION_FIELDS_SPLITTER);

            Log.d(TAG, "com.fuib.vega.veganet.content.services.ApiService.parseSelection selectionFields: " + Arrays.toString(selectionFields));

            for (String fieldExpression : selectionFields) {
                Log.d(TAG, "fieldExpression: " + fieldExpression);
                String[] expressionParts = fieldExpression.split(SELECTION_VALUE_SPLITTER);
                Log.d(TAG, "expressionParts: " + Arrays.toString(expressionParts));

                if (expressionParts.length != 2) {
                    expressionParts = fieldExpression.split(SELECTION_IN_SPLITTER);
                    Log.d(TAG, "expressionParts (2): " + Arrays.toString(expressionParts));

                    if (expressionParts.length != 2) {
                        throw new IllegalArgumentException(
                                String.format("Expression \"%s\" expected to be in a form of either \"FIELD = ?\" or \"FIELD IN (?,?,...?)\"",
                                        fieldExpression));
                    }
                }

                String field = expressionParts[0].trim();
                String value = expressionParts[1].trim();
                String[] values = null;

                Log.d(TAG, "field=" + field + " value=" + value);

                if (value.compareTo(SELECTION_VALUE_WILDCARD) != 0) {
                    // maybe we've got an enumeration here

                    if (!value.startsWith("(") || !value.endsWith(")")) {
                        throw new IllegalArgumentException(
                                String.format("Expression \"%s\" expected to be in a form of either \"FIELD = ?\" or \"FIELD IN (?,?,...?)\"",
                                        fieldExpression));
                    }

                    values = value.substring(1, value.length() - 1).split(SELECTION_VALUES_ENUMERATION_SPLITTER);
                    Log.d(TAG, "values=" + Arrays.toString(values));
                    int valuesIndex = 0;

                    for (String valueInEnumeration : values) {
                        valueInEnumeration = valueInEnumeration.trim();

                        if (valueInEnumeration.compareTo(SELECTION_VALUE_WILDCARD) != 0) {
                            throw new IllegalArgumentException(
                                    String.format("Expression \"%s\" expected to be in a form of either \"FIELD = ?\" or \"FIELD IN (?,?,...?)\". Specifying an in-place value is not supported",
                                            fieldExpression));
                        }

                        values[valuesIndex++] = getArgumentValue(selectionArgs, wildcardIndex++);
                    }

                    Log.d(TAG, "field=" + field + " values=" + Arrays.toString(values));
                    result.put(field, values);
                } else {
                    value = getArgumentValue(selectionArgs, wildcardIndex++);
                    Log.d(TAG, "field=" + field + " value=" + value);
                    result.put(field, new String[]{value});
                }
            }
        }

        return result;
    }

    public static SelectionHolder parametersToSelection(Map<String, String[]> parameters, SelectionJoiner selectionJoiner) {
        StringBuilder selection = new StringBuilder();
        List<String> selectionArgs = new LinkedList<String>();

        for (Map.Entry<String, String[]> entry : parameters.entrySet()) {
            String addToSelection;

            if (entry.getValue().length == 1 && (
                    entry.getValue()[0].matches(DateIntervalUtils.DATE_INTERVAL_PATTERN)
                            || entry.getValue()[0].matches(DateIntervalUtils.DATETIME_INTERVAL_PATTERN))) {
                // we've got an interval here
                addToSelection = DateIntervalUtils.BETWEEN_FORMAT;

                String[] interval = DateIntervalUtils.splitDatesPair(entry.getValue()[0]);
                selectionArgs.add(interval[0]);
                selectionArgs.add(interval[1]);
            } else if (entry.getValue().length > 1) {
                // we've got IN here
                String[] wildcards = new String[entry.getValue().length];
                for (int i = 0; i < entry.getValue().length; i++) {
                    wildcards[i] = SQL_WILDCARD;
                    selectionArgs.add(entry.getValue()[i]);
                }

                addToSelection = String.format(SQL_IN, Joiner.on(',').join(wildcards));
            } else {
                // a single value
                addToSelection = SQL_EQ;
                selectionArgs.add(entry.getValue()[0]);
            }

            if (selection.length() > 0) {
                selection.append(SQL_AND);
            }

            selection.append(entry.getKey()).append(" ");

            if (selectionJoiner == null) {
                selection.append(addToSelection);
            } else {
                selection.append(selectionJoiner.replaceValue(entry.getKey(), addToSelection));
            }
        }

        return new SelectionHolder(selection.toString(), selectionArgs.toArray(new String[selectionArgs.size()]));
    }

}
