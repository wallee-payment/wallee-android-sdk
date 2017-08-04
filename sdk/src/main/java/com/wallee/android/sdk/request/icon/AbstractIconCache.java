package com.wallee.android.sdk.request.icon;

import android.util.Log;

import com.android.volley.VolleyError;
import com.wallee.android.sdk.request.RequestCallback;
import com.wallee.android.sdk.request.model.method.PaymentMethodConfiguration;
import com.wallee.android.sdk.request.model.method.PaymentMethodIcon;
import com.wallee.android.sdk.util.AsynchronousCallback;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Abstract implementation of {@link IconCache} which provides the loading of multiple icons at
 * once. This will be most likely always the same independent of the cache strategy.
 *
 * <p>Additionally it implements strategies to avoid loading the same icon multiple times by
 * identify the same payment methods even when they are different object.</p>
 */
public abstract class AbstractIconCache implements IconCache {

    private final static String TAG = AbstractIconCache.class.getSimpleName();
    private final Timer timer = new Timer();
    private final long timeout;
    private final ConcurrentMap<CacheKey, DelegateRequestCallback> currentCallbacks = new ConcurrentHashMap<>();

    /**
     * Constructs a new cache with the specified {@code timeout} for loading multiple icons.
     *
     * @param timeout the timeout which cannot be exceeded when multiple icons are loaded. The time
     *                is in milliseconds.
     */
    protected AbstractIconCache(long timeout) {
        this.timeout = timeout;
    }

    /**
     * This method has to query the remote host to fetch the icon.
     *
     * <p>This method has to be implemented in a thread-safe manner.</p>
     *
     * @param paymentMethodConfiguration the payment method configuration for which the icon should
     *                                   be fetched for.
     * @param callback                   the callback which has to be invoked once the loading has
     *                                   been completed.
     */
    protected abstract void lookupInRemoteServer(CacheKey paymentMethodConfiguration, RequestCallback<PaymentMethodIcon> callback);

    /**
     * This method is invoked to try to load the icon from the local cache. The method may return
     * {@code null} when the entry does not exists.
     *
     * <p>This method has to be implemented in a thread-safe manner.</p>
     *
     * @param paymentMethodConfiguration the payment method configuration for which the icon should
     *                                   be looked up.
     * @return the icon instance or {@code null} when it does not exists.
     */
    protected abstract PaymentMethodIcon lookupInLocalCache(CacheKey paymentMethodConfiguration);

    /**
     * This method is invoked to persist an entry into the local cache.
     *
     * <p>This method has to be implemented in a thread-safe manner.</p>
     *
     * @param paymentMethodConfiguration the payment method configuration for which the icon should
     *                                   be stored for.
     * @param icon                       the icon which should be stored.
     */
    protected abstract void putInLocalCache(CacheKey paymentMethodConfiguration, PaymentMethodIcon icon);

    @Override
    public void loadIcon(final PaymentMethodConfiguration paymentMethodConfiguration, final RequestCallback<PaymentMethodIcon> callback) {
        final CacheKey cacheKey = new CacheKey(paymentMethodConfiguration);
        final PaymentMethodIcon icon = lookupInLocalCache(cacheKey);
        if (icon == null) {
            // We check if we can attach the current requested icon to an existing request execution.
            // If so we attach the callback to the existing one.
            DelegateRequestCallback existingCallback = currentCallbacks.get(cacheKey);
            boolean added = false;
            if (existingCallback != null && !existingCallback.invoked) {
                added = existingCallback.addCallback(callback);
            }

            // When we were not able to attach the callback to the existing one, we have to setup a
            // new delegate callback and add the callback to it.
            if (!added) {
                // We use here a wrapper so we can update the local cache once we receive the result.
                RequestCallback<PaymentMethodIcon> wrapper = new RequestCallback<PaymentMethodIcon>() {
                    @Override
                    public void onSuccess(PaymentMethodIcon object) {
                        putInLocalCache(cacheKey, object);

                        // We need to reload the icon to ensure we really do only keep on instance of
                        // the image in the memory. Otherwise eventually we would keep two instances of
                        // the same image in the memory.
                        callback.onSuccess(lookupInLocalCache(cacheKey));
                    }

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callback.onErrorResponse(error);
                    }
                };
                DelegateRequestCallback delegateRequestCallback = new DelegateRequestCallback(cacheKey, currentCallbacks);
                delegateRequestCallback.addCallback(wrapper);
                lookupInRemoteServer(cacheKey, delegateRequestCallback);
            }
        } else {
            callback.onSuccess(icon);
        }
    }

    @Override
    public final void fetchIcons(final Collection<PaymentMethodConfiguration> paymentMethodConfigurations, final AsynchronousCallback<Map<PaymentMethodConfiguration, PaymentMethodIcon>> callback) {
        final ConcurrentMap<PaymentMethodConfiguration, PaymentMethodIcon> icons = new ConcurrentHashMap<>();
        final AtomicInteger numberOfIconsToLoad = new AtomicInteger(paymentMethodConfigurations.size());
        final AtomicBoolean callbackInvoked = new AtomicBoolean(false);

        // We use here a time and not actually a request timeout to allow later arriving icons to
        // be put into the cache and as such they will be available even when the loading was not
        // completed within the time. So we do not throw away loaded data we may be able to use.

        final TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                // When we reach the timeout we try to fetch the icons from the local cache because
                // a parallel running request may has loaded the icon already and as such we do
                // not need to load it again. This depends on how the cache has been implemented and
                // as such it may be successful but it does not have to be.
                Map<PaymentMethodConfiguration, PaymentMethodIcon> copy = new HashMap<>(icons);
                for (final PaymentMethodConfiguration method : paymentMethodConfigurations) {
                    PaymentMethodIcon fromLocalCache = lookupInLocalCache(new CacheKey(method));
                    if (fromLocalCache != null) {
                        copy.put(method, fromLocalCache);
                    }
                }
                if (callbackInvoked.compareAndSet(false, true)) {
                    callback.process(Collections.unmodifiableMap(copy));
                }
            }
        };
        timer.schedule(timerTask, timeout);
        for (final PaymentMethodConfiguration method : paymentMethodConfigurations) {
            this.loadIcon(method, new RequestCallback<PaymentMethodIcon>() {
                @Override
                public void onSuccess(PaymentMethodIcon icon) {
                    icons.put(method, icon);
                    if (numberOfIconsToLoad.decrementAndGet() == 0 && callbackInvoked.compareAndSet(false, true)) {
                        // We make a copy so that the result is going to be stable.
                        callback.process(Collections.unmodifiableMap(new HashMap<>(icons)));
                    }
                }

                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.i(TAG, "The loading of the image from the remote server failed. URL: " +
                            method.getResolvedImageUrl(), error);
                }
            });
        }
    }

    /**
     * The cache key uses the fact that the payment method configuration is uniquely identifiable by
     * the version and id. The cache key is a wrapper around the payment method configuration to
     * allow a efficient storage in hash maps.
     */
    protected static class CacheKey {

        private final PaymentMethodConfiguration method;
        private final int version;
        private final long id;

        private CacheKey(PaymentMethodConfiguration method) {
            this.method = method;
            this.version = method.getVersion();
            this.id = method.getId();
        }

        public PaymentMethodConfiguration getPaymentMethodConfiguration() {
            return this.method;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            CacheKey cacheKey = (CacheKey) o;

            if (version != cacheKey.version) return false;
            return id == cacheKey.id;

        }

        @Override
        public int hashCode() {
            int result = version;
            result = 31 * result + (int) (id ^ (id >>> 32));
            return result;
        }
    }

    /**
     * We use this class to collapse multiple callbacks into one. So we can add a callback to an
     * existing one when there is already a request going on.
     */
    private static class DelegateRequestCallback implements RequestCallback<PaymentMethodIcon> {

        private final Collection<RequestCallback<PaymentMethodIcon>> callbacks = new ArrayList<>();
        private final Object lock = new Object();
        private volatile boolean invoked = false;
        private final ConcurrentMap<CacheKey, DelegateRequestCallback> currentCallbacks;
        private final CacheKey cacheKey;

        private DelegateRequestCallback(CacheKey cacheKey, ConcurrentMap<CacheKey, DelegateRequestCallback> currentCallbacks) {
            this.cacheKey = cacheKey;
            this.currentCallbacks = currentCallbacks;
            currentCallbacks.put(cacheKey, this);
        }


        @Override
        public void onSuccess(PaymentMethodIcon object) {
            final Collection<RequestCallback<PaymentMethodIcon>> copy;
            synchronized (lock) {
                copy = new ArrayList<>(callbacks);
                this.invoked = true;
                currentCallbacks.remove(cacheKey);
            }
            for (RequestCallback<PaymentMethodIcon> callback : copy) {
                callback.onSuccess(object);
            }
        }

        @Override
        public void onErrorResponse(VolleyError error) {
            final Collection<RequestCallback<PaymentMethodIcon>> copy;
            synchronized (lock) {
                copy = new ArrayList<>(callbacks);
                this.invoked = true;
                currentCallbacks.remove(cacheKey);
            }
            for (RequestCallback<PaymentMethodIcon> callback : copy) {
                callback.onErrorResponse(error);
            }
        }

        boolean addCallback(RequestCallback<PaymentMethodIcon> callback) {
            synchronized (lock) {
                if (!invoked) {
                    callbacks.add(callback);
                    return true;
                } else {
                    return false;
                }
            }
        }
    }


}