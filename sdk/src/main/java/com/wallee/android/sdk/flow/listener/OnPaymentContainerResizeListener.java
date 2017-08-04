package com.wallee.android.sdk.flow.listener;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wallee.android.sdk.view.form.PaymentFormView;

/**
 * This listener is invoked when the payment container requires the whole screen. Means when the
 * user should focus on the payment flow and the content may require more space. This is the case
 * when the user is forwarded to an external service such as 3-D secure authorization screen or a
 * external payment service.
 */
public interface OnPaymentContainerResizeListener extends FlowListener {

    /**
     * This method is invoked when the provided view should be enlarged to render more complex
     * content (e.g. the 3-D secure authorization form).
     */
    void onPaymentContainerEnlarge();

    /**
     * This method is called when the view size can be reduced again.
     */
    void onPaymentContainerReduce();

}
