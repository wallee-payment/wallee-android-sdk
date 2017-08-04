package com.wallee.android.sdk.flow.listener;

import com.wallee.android.sdk.request.model.method.PaymentMethodConfiguration;

/**
 * This listener is triggered when a payment method has been selected.
 */
public interface OnPaymentMethodSelectionListener extends FlowListener {

    /**
     * This method is invoked when the user has chosen a payment method configuration.
     *
     * @param selectedPaymentMethod the configuration which has been selected by the user.
     */
    void onPaymentMethodSelection(PaymentMethodConfiguration selectedPaymentMethod);

}
