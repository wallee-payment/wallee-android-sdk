package com.wallee.android.sdk.flow;

import android.view.View;
import android.view.ViewGroup;

import com.android.volley.VolleyError;
import com.wallee.android.sdk.flow.config.FlowConfiguration;
import com.wallee.android.sdk.flow.listener.OnBeforeLoadingPaymentMethodListener;
import com.wallee.android.sdk.flow.model.LoadedPaymentMethods;
import com.wallee.android.sdk.flow.model.SelectedPaymentMethodConfiguration;
import com.wallee.android.sdk.request.RequestCallback;
import com.wallee.android.sdk.request.model.method.PaymentMethodConfiguration;
import com.wallee.android.sdk.util.AsynchronousCallback;
import com.wallee.android.sdk.request.model.method.PaymentMethodIcon;

import java.util.List;
import java.util.Map;

/**
 * The payment method loading state handler is responsible to handle the {@link
 * FlowState#PAYMENT_METHOD_LOADING}.
 *
 * <p>This handler is loading the {@link PaymentMethodConfiguration}
 * which can be chosen form by the user.</p>
 */
final class PaymentMethodLoadingStateHandler implements FlowStateHandler {

    private final FlowConfiguration configuration;
    private final CoordinatorCallback coordinatorCallback;

    PaymentMethodLoadingStateHandler(CoordinatorCallback coordinatorCallback, final FlowConfiguration configuration) {
        this.configuration = configuration;
        this.coordinatorCallback = coordinatorCallback;
    }

    @Override
    public void initialize() {
        for (OnBeforeLoadingPaymentMethodListener listener : configuration.getListenersByType(OnBeforeLoadingPaymentMethodListener.class)) {
            listener.onBeforeLoadingPaymentMethod();
        }

        configuration.getWebServiceApiClient().fetchPaymentMethodConfigurations(new RequestCallback<List<PaymentMethodConfiguration>>() {
            @Override
            public void onSuccess(final List<PaymentMethodConfiguration> paymentMethods) {
                if (paymentMethods.size() == 1) {
                    PaymentMethodConfiguration paymentMethod = paymentMethods.get(0);
                    coordinatorCallback.changeStateTo(FlowState.PAYMENT_FORM, new SelectedPaymentMethodConfiguration(paymentMethod.getId()));
                } else {
                    configuration.getIconCache().fetchIcons(paymentMethods, new AsynchronousCallback<Map<PaymentMethodConfiguration, PaymentMethodIcon>>() {
                        @Override
                        public void process(Map<PaymentMethodConfiguration, PaymentMethodIcon> icons) {
                            coordinatorCallback.changeStateTo(FlowState.PAYMENT_METHOD_SELECTION, new LoadedPaymentMethods(paymentMethods, icons));
                        }
                    });
                }
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                ErrorHandler.distributeVolleyError(error, configuration);
            }
        });
    }

    @Override
    public boolean triggerAction(FlowAction action, View currentView) {
        return false;
    }

    @Override
    public boolean dryTriggerAction(FlowAction action, View currentView) {
        return false;
    }


    @Override
    public View createView(ViewGroup container) {
        throw new IllegalStateException("This method should not be called in State " + this.getClass().getCanonicalName());
    }


}
