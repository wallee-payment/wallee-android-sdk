package com.wallee.android.sdk.request.icon;

import com.android.volley.VolleyError;
import com.wallee.android.sdk.request.RequestCallback;
import com.wallee.android.sdk.request.model.method.PaymentMethodConfiguration;
import com.wallee.android.sdk.util.Check;
import com.wallee.android.sdk.request.model.method.PaymentMethodIcon;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * This implementation of {@link IconCache} stores the icons in the memory by using a static field.
 *
 * <p>The implementation will reused the stored icons. Whenever the JVM is killed the cache is
 * onResetView. This means the class will reuse the stored icons as long as the app is running. On a
 * restart the icons will be refreshed.</p>
 *
 * <p>For most use cases this is a desired behavior because at some point the data has to be removed
 * resp. refreshed. We use the app restart to onResetView the cache and reload the icons.</p>
 */
public final class InMemoryIconCache extends AbstractIconCache implements IconCache {
    private static final String TAG = InMemoryIconCache.class.getSimpleName();
    private final static ConcurrentMap<CacheKey, PaymentMethodIcon> icons = new ConcurrentHashMap<>();
    private final IconRequestManager iconRequestManager;

    /**
     * Constructs an instance based on a {@link IconRequestManager}.
     *
     * @param iconRequestManager the request manager to use.
     * @param timeout            the timeout which cannot be exceeded when multiple icons are
     *                           loaded. The time is in milliseconds.
     */
    public InMemoryIconCache(IconRequestManager iconRequestManager, long timeout) {
        super(timeout);
        this.iconRequestManager = Check.requireNonNull(iconRequestManager);
    }

    @Override
    protected PaymentMethodIcon lookupInLocalCache(CacheKey cacheKey) {
        return icons.get(cacheKey);
    }

    @Override
    protected void lookupInRemoteServer(CacheKey cacheKey, RequestCallback<PaymentMethodIcon> callback) {
        iconRequestManager.fetchIcon(cacheKey.getPaymentMethodConfiguration(), callback);
    }

    @Override
    protected void putInLocalCache(CacheKey cacheKey, PaymentMethodIcon icon) {
        icons.putIfAbsent(cacheKey, icon);
    }

}
