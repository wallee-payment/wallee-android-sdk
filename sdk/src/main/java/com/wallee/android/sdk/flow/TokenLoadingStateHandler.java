package com.wallee.android.sdk.flow;

import android.view.View;
import android.view.ViewGroup;

import com.android.volley.VolleyError;
import com.wallee.android.sdk.flow.config.FlowConfiguration;
import com.wallee.android.sdk.flow.listener.OnBeforeLoadingPaymentMethodListener;
import com.wallee.android.sdk.flow.listener.OnBeforeLoadingTokenListener;
import com.wallee.android.sdk.flow.model.LoadedTokens;
import com.wallee.android.sdk.request.RequestCallback;
import com.wallee.android.sdk.request.model.method.PaymentMethodConfiguration;
import com.wallee.android.sdk.request.model.token.TokenVersion;
import com.wallee.android.sdk.util.AsynchronousCallback;
import com.wallee.android.sdk.util.Check;
import com.wallee.android.sdk.request.model.method.PaymentMethodIcon;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * This class handles the {@link FlowState#TOKEN_LOADING} state.
 *
 * <p>The class is responsible for loading the tokens and moving into the next state.</p>
 */
final class TokenLoadingStateHandler implements FlowStateHandler {
    private static final String TAG = TokenLoadingStateHandler.class.getSimpleName();

    private final CoordinatorCallback coordinatorCallback;
    private final FlowConfiguration configuration;

    @Override
    public boolean triggerAction(FlowAction action, View currentView) {
        return false;
    }

    @Override
    public boolean dryTriggerAction(FlowAction action, View currentView) {
        return false;
    }


    TokenLoadingStateHandler(final CoordinatorCallback coordinatorCallback, final FlowConfiguration configuration) {
        this.coordinatorCallback = Check.requireNonNull(coordinatorCallback, "The coordinatorCallback is required.");
        this.configuration = Check.requireNonNull(configuration, "The configuration is required.");
    }

    @Override
    public void initialize() {
        for (OnBeforeLoadingTokenListener listener : configuration.getListenersByType(OnBeforeLoadingTokenListener.class)) {
            listener.onBeforeLoadingToken();
        }
        this.configuration.getWebServiceApiClient().fetchTokenVersions(new RequestCallback<List<TokenVersion>>() {
            @Override
            public void onSuccess(final List<TokenVersion> tokenVersionList) {
                if (tokenVersionList.size() == 0) {
                    coordinatorCallback.changeStateTo(FlowState.PAYMENT_METHOD_LOADING, null);
                } else {

                    Collection<PaymentMethodConfiguration> paymentMethodConfigurations = new HashSet<>();
                    for (TokenVersion version : tokenVersionList) {
                        paymentMethodConfigurations.add(version.getPaymentConnectorConfiguration().getPaymentMethodConfiguration());
                    }
                    configuration.getIconCache().fetchIcons(paymentMethodConfigurations, new AsynchronousCallback<Map<PaymentMethodConfiguration, PaymentMethodIcon>>() {
                        @Override
                        public void process(Map<PaymentMethodConfiguration, PaymentMethodIcon> parameter) {
                            LoadedTokens loadedTokens = new LoadedTokens(tokenVersionList, parameter);
                            coordinatorCallback.changeStateTo(FlowState.TOKEN_SELECTION, loadedTokens);
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
    public View createView(ViewGroup container) {
        throw new IllegalStateException("This method should not be called in State " + this.getClass().getCanonicalName());
    }
}
