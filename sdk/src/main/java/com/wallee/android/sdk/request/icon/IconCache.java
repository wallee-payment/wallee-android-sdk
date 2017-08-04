package com.wallee.android.sdk.request.icon;

import com.wallee.android.sdk.request.RequestCallback;
import com.wallee.android.sdk.request.model.method.PaymentMethodConfiguration;
import com.wallee.android.sdk.request.model.method.PaymentMethodIcon;
import com.wallee.android.sdk.util.AsynchronousCallback;

import java.util.Collection;
import java.util.Map;

/**
 * The icon cache caches the downloaded payment method icons.
 *
 * <p>Depending on the payment method the icon has to be different. The payment methods are not
 * fixed they are depend on the configuration of the space within the wallee backend. The icon
 * itself can be also changed through the wallee backend. As such the icons depend on the
 * configuration and cannot be delivered within the app or the SDK.</p>
 *
 * <p>This cache should store the downloaded images so when they are reused a download is not
 * required.</p>
 *
 * <p>The implementor may choose different strategies to cache the images. This really depends on
 * how often the images will be exchanged, how many images there are and how the user will use the
 * app. A minimal implementation may use an in-memory cache which gets onResetView whenever the app
 * is restarted. A file based approach may also be feasible however there has to be a retention
 * strategy in place. To detect if a particular image has changed the {@link
 * PaymentMethodConfiguration#getVersion()} can be tracked. Whenever the version changes the icon
 * has been eventually changed.</p>
 */
public interface IconCache {

    /**
     * The default loading timeout of the icons in the payment method and token selection view.
     * This time is in milliseconds.
     */
    long DEFAULT_ICON_LOADING_TIMEOUT = 10_000;

    /**
     * This method is invoked when for the {@code methodConfiguration} the images should be
     * downloaded.
     *
     * <p>The implementor has to make sure that a parallel invocation of this method does not cause
     * a memory leak. This means that a storage of the result should be implemented in a way that
     * duplicate requests get detected and handled properly. For example by using a {@link
     * java.util.concurrent.ConcurrentMap} or some kind of {@link java.util.concurrent.atomic.AtomicReference}.</p>
     *
     * <p>The URL from which the icon should be downloaded from can be found on {@link
     * PaymentMethodConfiguration#getResolvedImageUrl()}.</p>
     *
     * <p>There is also a {@link IconRequestManager} which helps with the invocation of the remote
     * server.</p>
     *
     * @param methodConfiguration the payment method configuration for which the icon should be
     *                            provided for.
     * @param callback            the callback which should be invoked once the icon has been loaded
     *                            either form the source or from the cache.
     */
    void loadIcon(PaymentMethodConfiguration methodConfiguration, RequestCallback<PaymentMethodIcon> callback);

    /**
     * This method fetches for a collection of {@link PaymentMethodConfiguration} the associated
     * icons.
     *
     * <p>The method will stop after a specified timeout. The resulting map will have no entry for
     * those which could not be loaded within the timeout.</p>
     *
     * @param paymentMethodConfigurations the configurations for which the icons should be loaded
     *                                    for.
     * @param callback                    the callback which is invoked once the loading has
     *                                    completed or the timeout has been reached.
     */
    void fetchIcons(Collection<PaymentMethodConfiguration> paymentMethodConfigurations, AsynchronousCallback<Map<PaymentMethodConfiguration, PaymentMethodIcon>> callback);

}
