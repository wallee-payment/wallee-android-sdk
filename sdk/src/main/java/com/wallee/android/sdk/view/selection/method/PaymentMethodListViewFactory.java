package com.wallee.android.sdk.view.selection.method;

import android.view.View;
import android.view.ViewGroup;

import com.wallee.android.sdk.request.model.method.PaymentMethodConfiguration;
import com.wallee.android.sdk.request.model.method.PaymentMethodIcon;

import java.util.List;
import java.util.Map;


/**
 * The payment method list view shows the payment methods from which the user can select from.
 *
 * <p>The payment method list allows to select by the user a payment method. The list is fetched
 * dynamically from the wallee web service.</p>
 */
public interface PaymentMethodListViewFactory {

    /**
     * This method is invoked to create a view which lists all the payment methods which the user
     * can use to pay with.
     *
     * <p>The implementor does not need to add the view into the {@code parent} view. This is done
     * by the caller of the method.</p>
     *
     * @param parent                      the parent view into which the created view is inserted
     *                                    into.
     * @param listener                    the listener which allows to listen to events within the
     *                                    view.
     * @param paymentMethodConfigurations the payment methods from which the user can select from.
     * @param icons                       the payment method icons which can be displayed along the
     *                                    payment method name.
     * @return the view which shows the payment methods.
     */
    View build(ViewGroup parent, PaymentMethodListViewListener listener, List<PaymentMethodConfiguration> paymentMethodConfigurations, Map<PaymentMethodConfiguration, PaymentMethodIcon> icons);

    /**
     * This listener informs the implementor about events occurring within the payment method list
     * view.
     */
    interface PaymentMethodListViewListener {

        /**
         * This method is invoked when the {@code method} has been selected by the user.
         *
         * @param method the payment method which has been selected by the user.
         */
        void onPaymentMethodClicked(PaymentMethodConfiguration method);
    }
}
