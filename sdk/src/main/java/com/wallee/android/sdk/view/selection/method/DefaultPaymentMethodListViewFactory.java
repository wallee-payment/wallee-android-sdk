package com.wallee.android.sdk.view.selection.method;

import android.support.v7.widget.LinearLayoutManager;
import android.view.ViewGroup;

import com.wallee.android.sdk.request.model.method.PaymentMethodConfiguration;
import com.wallee.android.sdk.request.model.method.PaymentMethodIcon;

import java.util.List;
import java.util.Map;

/**
 * Default implementation of {@link PaymentMethodListViewFactory} which uses a recycler view to
 * render the payment method list.
 */
public final class DefaultPaymentMethodListViewFactory implements PaymentMethodListViewFactory {

    @Override
    public DefaultPaymentMethodListView build(ViewGroup parent, PaymentMethodListViewListener listener, List<PaymentMethodConfiguration> paymentMethodConfigurations, Map<PaymentMethodConfiguration, PaymentMethodIcon> icons) {
        DefaultPaymentMethodListView recyclerView = new DefaultPaymentMethodListView(parent.getContext());
        DefaultPaymentMethodListViewAdapter adapter = new DefaultPaymentMethodListViewAdapter(paymentMethodConfigurations, listener, icons);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(parent.getContext()));
        return recyclerView;
    }
}
