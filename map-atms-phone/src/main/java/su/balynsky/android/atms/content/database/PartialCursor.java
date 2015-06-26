package su.balynsky.android.atms.content.database;

import android.database.Cursor;

/**
 * Created by furdey on 14.06.14.
 */
public interface PartialCursor extends Cursor {
    boolean isCursorPartial();
    void setCursorPartial(boolean partial);
}
