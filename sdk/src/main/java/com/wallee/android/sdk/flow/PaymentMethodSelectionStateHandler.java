package com.wallee.android.sdk.flow;

import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.wallee.android.sdk.flow.config.FlowConfiguration;
import com.wallee.android.sdk.flow.listener.OnPaymentMethodSelectionViewReady;
import com.wallee.android.sdk.flow.listener.OnTokenSelectionViewReady;
import com.wallee.android.sdk.flow.model.LoadedPaymentMethods;
import com.wallee.android.sdk.flow.model.SelectedPaymentMethodConfiguration;
import com.wallee.android.sdk.request.model.method.PaymentMethodConfiguration;
import com.wallee.android.sdk.view.selection.method.PaymentMethodListViewFactory;

import java.util.List;

/**
 * This class is responsible to handle the {@link FlowState#PAYMENT_METHOD_SELECTION} state.
 *
 * <p>The handler shows a view with the payment methods and when a payment method is selected the
 * form for this particular payment method is shown.</p>
 */
final class PaymentMethodSelectionStateHandler implements FlowStateHandler, PaymentMethodListViewFactory.PaymentMethodListViewListener {
    private static final String TAG = PaymentMethodSelectionStateHandler.class.getSimpleName();

    private final CoordinatorCallback stateSetter;
    private final FlowConfiguration configuration;
    private final LoadedPaymentMethods loadedPaymentMethods;

    PaymentMethodSelectionStateHandler(CoordinatorCallback stateSetter, FlowConfiguration configuration, Parcelable stateArgument) {
        this.stateSetter = stateSetter;
        this.configuration = configuration;
        if (stateArgument instanceof LoadedPaymentMethods) {
            this.loadedPaymentMethods = (LoadedPaymentMethods) stateArgument;
        } else {
            throw new IllegalArgumentException("The provided state argument has to be of the type: " + LoadedPaymentMethods.class);
        }
    }

    @Override
    public void initialize() {
        List<OnPaymentMethodSelectionViewReady> listeners = this.configuration.getListenersByType(OnPaymentMethodSelectionViewReady.class);
        for (OnPaymentMethodSelectionViewReady listener : listeners) {
            listener.onPaymentMethodSelectionViewReady();
        }
        this.stateSetter.ready();
    }

    @Override
    public View createView(ViewGroup container) {
        return this.configuration.getPaymentMethodListViewFactory().build(container, this,
                loadedPaymentMethods.getPaymentMethods(), loadedPaymentMethods.getIcons());
    }

    @Override
    public boolean dryTriggerAction(FlowAction action, View currentView) {
        return action == FlowAction.GO_BACK;
    }

    @Override
    public boolean triggerAction(FlowAction action, View currentView) {
        if (dryTriggerAction(action, currentView)) {
            this.stateSetter.changeStateTo(FlowState.TOKEN_LOADING, null);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onPaymentMethodClicked(PaymentMethodConfiguration method) {
        Log.d(TAG, "The payment method configuration with the ID " + method.getId() + " has been selected.");
        SelectedPaymentMethodConfiguration selectedConfiguration = new
                SelectedPaymentMethodConfiguration(method.getId());
        this.stateSetter.changeStateTo(FlowState.PAYMENT_FORM, selectedConfiguration);
    }
}
