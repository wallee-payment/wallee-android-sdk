package com.wallee.android.sdk.view.form;

import android.view.ViewGroup;

/**
 * This implementation of {@link PaymentFormViewFactory} creates a {@link DefaultPaymentFormView}.
 *
 * <p>This factory should be overridden when a subclass of {@link DefaultPaymentFormView} should be
 * created.</p>
 */
public final class DefaultPaymentFormViewFactory implements PaymentFormViewFactory<DefaultPaymentFormView> {

    @Override
    public DefaultPaymentFormView build(ViewGroup parent, PaymentFormView.Listener listener, long selectedPaymentMethodConfigurationId, UrlFetcher urlFetcher) {
        return new DefaultPaymentFormView(parent.getContext(), listener, urlFetcher, selectedPaymentMethodConfigurationId);
    }
}
