package su.balynsky.android.atms.content.cache;

import android.net.Uri;

import java.util.Arrays;

/**
 * Created by furdey on 01.06.14.
 */
class CacheKey {
    private Uri uri;
    private String   selection;
    private String[] selectionArgs;

    CacheKey(Uri uri, String selection, String[] selectionArgs) {
        if (uri == null)
            throw new IllegalArgumentException("uri can't be null");

        this.uri = uri;
        this.selection = selection;
        this.selectionArgs = selectionArgs;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CacheKey)) return false;

        CacheKey cacheKey = (CacheKey) o;

        if (selection != null ? !selection.equals(cacheKey.selection) : cacheKey.selection != null)
            return false;
        if (!Arrays.equals(selectionArgs, cacheKey.selectionArgs)) return false;
        return uri.equals(cacheKey.uri);

    }

    @Override
    public int hashCode() {
        int result = uri.hashCode();
        result = 31 * result + (selection != null ? selection.hashCode() : 0);
        result = 31 * result + (selectionArgs != null ? Arrays.hashCode(selectionArgs) : 0);
        return result;
    }
}
