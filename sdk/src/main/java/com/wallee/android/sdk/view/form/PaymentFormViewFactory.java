package com.wallee.android.sdk.view.form;


import android.view.View;
import android.view.ViewGroup;

import com.wallee.android.sdk.request.model.transaction.MobileSdkUrl;
import com.wallee.android.sdk.util.AsynchronousCallback;

/**
 * The payment form factory is responsible to create a view which holds the payment form.
 *
 * <p>The payment form contains the input fields for collecting the payment details. Typically this
 * is a {@link android.webkit.WebView} with a form loaded from the remote server.</p>
 *
 * @param <V> the type of the payment form which is created.
 */
public interface PaymentFormViewFactory<V extends View & PaymentFormView> {

    /**
     * This method is responsible to create the view for handling the payment input.
     *
     * <p>The implementor does not need to add the created view into the parent. This is done by the
     * caller of the method.</p>
     *
     * @param parent                               the parent view into which the form is integrated
     *                                             into.
     * @param listener                             the listener which is informed about state
     *                                             changes within the view.
     * @param selectedPaymentMethodConfigurationId the payment method for which the form should be
     *                                             created for.
     * @param urlFetcher                           the URL fetcher is responsible to load the SDK
     *                                             URL from which the form is loaded.
     * @return the view which contains the payment form.
     */
    V build(ViewGroup parent, PaymentFormView.Listener listener, long selectedPaymentMethodConfigurationId, UrlFetcher urlFetcher);

    /**
     * The URL fetcher is responsible for loading the {@link MobileSdkUrl} which is used to load the
     * payment form.
     */
    interface UrlFetcher {

        /**
         * Called to fetch the {@link MobileSdkUrl}. The implementor does not need to cache the
         * result.
         *
         * @param onReadyCallback the callback which should be invoked once the URL is
         *                        onTokenSelectionViewReady.
         */
        void fetchMobileSdkUrl(AsynchronousCallback<MobileSdkUrl> onReadyCallback);
    }

}
