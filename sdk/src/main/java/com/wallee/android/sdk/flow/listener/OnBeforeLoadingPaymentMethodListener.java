package com.wallee.android.sdk.flow.listener;

/**
 * This listener is invoked before the payment method list is loaded from the remote server.
 */
public interface OnBeforeLoadingPaymentMethodListener extends FlowListener {

    /**
     * This method is invoked before the payment method list is loaded.
     */
    void onBeforeLoadingPaymentMethod();

}
