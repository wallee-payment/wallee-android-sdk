package com.wallee.android.sdk.flow;

import android.os.Parcelable;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.VolleyError;
import com.wallee.android.sdk.flow.config.FlowConfiguration;
import com.wallee.android.sdk.flow.listener.OnTokenSelectedListener;
import com.wallee.android.sdk.flow.listener.OnTokenSelectionViewReady;
import com.wallee.android.sdk.flow.model.LoadedTokens;
import com.wallee.android.sdk.request.RequestCallback;
import com.wallee.android.sdk.request.model.token.Token;
import com.wallee.android.sdk.request.model.token.TokenVersion;
import com.wallee.android.sdk.request.model.transaction.Transaction;
import com.wallee.android.sdk.util.Check;
import com.wallee.android.sdk.view.selection.token.TokenListViewFactory;

import java.util.List;

/**
 * This class handles the {@link FlowState#TOKEN_SELECTION} state.
 *
 * <p>The class is responsible to load the token selection view and to trigger the state changes
 * based up on the selected token.</p>
 */
final class TokenSelectionStateHandler implements TokenListViewFactory.TokenViewListener, FlowStateHandler {
    private final static String TAG = TokenSelectionStateHandler.class.getSimpleName();

    private final CoordinatorCallback coordinatorCallback;
    private final LoadedTokens loadedTokens;
    private final FlowConfiguration configuration;


    TokenSelectionStateHandler(CoordinatorCallback coordinatorCallback, FlowConfiguration configuration, Parcelable argument) {
        this.coordinatorCallback = Check.requireNonNull(coordinatorCallback, "The coordinatorCallback is required.");
        this.loadedTokens = Check.requireNonNull((LoadedTokens) argument, "The state argument cannot be null.");
        this.configuration = Check.requireNonNull(configuration, "The configuration cannot be null.");
    }

    @Override
    public void initialize() {
        List<OnTokenSelectionViewReady> listeners = this.configuration.getListenersByType(OnTokenSelectionViewReady.class);
        for (OnTokenSelectionViewReady listener : listeners) {
            listener.onTokenSelectionViewReady();
        }
        this.coordinatorCallback.ready();
    }

    @Override
    public View createView(ViewGroup container) {
        return configuration.getTokenListViewFactory().build(container, this, loadedTokens.getTokenVersions(), loadedTokens.getIcons());
    }

    @Override
    public boolean dryTriggerAction(FlowAction action, View currentView) {
        return action == FlowAction.SWITCH_TO_PAYMENT_METHOD_SELECTION;
    }

    @Override
    public boolean triggerAction(FlowAction action, View currentView) {
        if (dryTriggerAction(action, currentView)) {
            if (action == FlowAction.SWITCH_TO_PAYMENT_METHOD_SELECTION) {
                this.coordinatorCallback.changeStateTo(FlowState.PAYMENT_METHOD_LOADING, null);
                return true;
            }
        }
        return false;
    }

    @Override
    public void onTokenClicked(TokenVersion selectedToken) {
        TokenVersion version = Check.requireNonNull(selectedToken, "TokenVersion is required. Cannot be null.");
        Token token = Check.requireNonNull(version.getToken(), "Token is requires. Cannot be null");
        this.coordinatorCallback.waiting();

        for (OnTokenSelectedListener listener : this.configuration.getListenersByType(OnTokenSelectedListener.class)) {
            listener.onTokenSelected(selectedToken);
        }

        this.configuration.getWebServiceApiClient().processOneClickToken(token, new RequestCallback<Transaction>() {
            @Override
            public void onSuccess(Transaction transaction) {
                if (transaction.isSuccessful()) {
                    coordinatorCallback.changeStateTo(FlowState.SUCCESS, transaction);
                } else if (transaction.isFailed()) {
                    coordinatorCallback.changeStateTo(FlowState.FAILURE, transaction);
                } else {
                    coordinatorCallback.changeStateTo(FlowState.AWAITING_FINAL_STATE, transaction);
                }
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                ErrorHandler.distributeVolleyError(error, configuration);
            }
        });

    }
}
