package su.balynsky.android.atms.content.common;

/**
 * @author Sergey Balynsky
 *         on 28.01.2015.
 */
public class SelectionHolder {

    private String selection;
    private String[] selectionArgs;

    public SelectionHolder(String selection, String[] selectionArgs) {
        this.selection = selection;
        this.selectionArgs = selectionArgs;
    }

    public String getSelection() {
        return selection;
    }

    public void setSelection(String selection) {
        this.selection = selection;
    }

    public String[] getSelectionArgs() {
        return selectionArgs;
    }

    public void setSelectionArgs(String[] selectionArgs) {
        this.selectionArgs = selectionArgs;
    }
}
