package su.balynsky.android.atms.content.cache;

import android.net.Uri;
import android.os.Bundle;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by furdey on 01.06.14.
 */
public class QueryCacheManager {
    private static Map<CacheKey, CacheToken> queries;

    private static Map<CacheKey, CacheToken> getQueries() {
        if (queries == null) {
            synchronized (QueryCacheManager.class) {
                if (queries == null) {
                    queries = new ConcurrentHashMap<CacheKey, CacheToken>();
                }
            }
        }

        return queries;
    }

    public static boolean isCacheAvailable(Uri uri, String selection, String[] selectionArgs) {
        CacheKey key = new CacheKey(uri, selection, selectionArgs);
        CacheToken token = getQueries().get(key);
        return token != null && token.isValid();
    }

    public static boolean isCachePartialAvailable(Uri uri, String selection, String[] selectionArgs) {
        CacheKey key = new CacheKey(uri, selection, selectionArgs);
        CacheToken token = getQueries().get(key);
        return token != null && token.isPartialValid();
    }

    public static boolean isCacheBeingRefreshed(Uri uri, String selection, String[] selectionArgs) {
        CacheKey key = new CacheKey(uri, selection, selectionArgs);
        CacheToken token = getQueries().get(key);
        return token != null && token.isBeingRefreshed();
    }

    public static void markDataBeingRefreshed(Uri uri, String selection, String[] selectionArgs, long ttl) {
        setDataCachedStatus(uri, selection, selectionArgs, ttl, CacheToken.Status.REFRESHING);
    }

    public static void markDataCached(Uri uri, String selection, String[] selectionArgs, long ttl) {
        setDataCachedStatus(uri, selection, selectionArgs, ttl, CacheToken.Status.VALID);
    }

    public static Bundle getServiceToken(Uri uri, String selection, String[] selectionArgs) {
        CacheKey key = new CacheKey(uri, selection, selectionArgs);
        CacheToken token = getQueries().get(key);
        return token != null ? token.getServiceToken() : null;
    }

    private static void setDataCachedStatus(Uri uri, String selection, String[] selectionArgs, long ttl, CacheToken.Status status) {
        CacheKey key = new CacheKey(uri, selection, selectionArgs);
        CacheToken token = new CacheToken(ttl, status);
        getQueries().put(key, token);
    }

}
