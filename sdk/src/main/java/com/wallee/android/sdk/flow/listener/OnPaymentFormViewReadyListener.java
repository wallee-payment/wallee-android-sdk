package com.wallee.android.sdk.flow.listener;

/**
 * This listener is invoked when the payment form view is ready. This gives the caller the option to
 * add the submit button.
 */
public interface OnPaymentFormViewReadyListener extends FlowListener {

    /**
     * This method is invoked when the payment form is ready. This means the payment form is fully
     * loaded and the user may enter some data.
     */
    void onPaymentFormViewReady(boolean userInputRequired);

}
