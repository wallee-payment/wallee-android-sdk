package com.wallee.android.sdk.flow.listener;


/**
 * This listener is invoked when the payment method selection view is ready resp. displayed.
 */
public interface OnPaymentMethodSelectionViewReady extends FlowListener {

    /**
     * This method is invoked when the payment method selection view is loaded.
     */
    void onPaymentMethodSelectionViewReady();
}
