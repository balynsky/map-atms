package su.balynsky.android.atms.content.cache;

import android.os.Bundle;

/**
 * Created by furdey on 01.06.14.
 */
class CacheToken {

    enum Status {
        REFRESHING, VALID, PARTIAL
    }

    private long created;
    private long ttl;
    private Status status;
    private Bundle serviceToken;

    CacheToken(long ttl, Status status, Bundle serviceToken) {
        if (status == null) {
            throw new IllegalArgumentException("Status can't be null");
        }

        this.created = System.currentTimeMillis();
        this.ttl = ttl;
        this.status = status;
        this.serviceToken = serviceToken;
    }

    CacheToken(long ttl, Status status) {
        this(ttl, status, null);
    }

    public boolean isValid() {
        long time = System.currentTimeMillis();
        return time - created <= ttl && (status == Status.VALID || status == Status.PARTIAL);
    }

    public boolean isPartialValid() {
        long time = System.currentTimeMillis();
        return time - created <= ttl && status == Status.PARTIAL;
    }

    public boolean isBeingRefreshed() {
        long time = System.currentTimeMillis();
        return time - created <= ttl && status == Status.REFRESHING;
    }

    public Bundle getServiceToken() {
        return serviceToken;
    }
}
