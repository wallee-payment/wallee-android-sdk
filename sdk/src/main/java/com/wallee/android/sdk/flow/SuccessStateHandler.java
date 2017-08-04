package com.wallee.android.sdk.flow;

import android.view.View;
import android.view.ViewGroup;

import com.wallee.android.sdk.flow.config.FlowConfiguration;
import com.wallee.android.sdk.flow.listener.OnTransactionFailureListener;
import com.wallee.android.sdk.flow.listener.OnTransactionSuccessListener;
import com.wallee.android.sdk.request.model.transaction.Transaction;
import com.wallee.android.sdk.util.Check;

/**
 * This class is responsible for handling the {@link FlowState#SUCCESS} state.
 */
final class SuccessStateHandler implements FlowStateHandler {
    private final CoordinatorCallback coordinatorCallback;
    private final FlowConfiguration configuration;
    private final Transaction transaction;

    SuccessStateHandler(CoordinatorCallback coordinatorCallback, FlowConfiguration configuration, Transaction transaction) {
        this.coordinatorCallback = Check.requireNonNull(coordinatorCallback, "The coordinatorCallback is required.");
        this.configuration = Check.requireNonNull(configuration, "The configuration is required.");
        this.transaction = Check.requireNonNull(transaction, "The transaction is required.");
    }

    @Override
    public void initialize() {
        for (OnTransactionSuccessListener listener : this.configuration.getListenersByType(OnTransactionSuccessListener.class)) {
            listener.onTransactionSuccess(this.transaction);
        }
        this.coordinatorCallback.ready();
    }

    @Override
    public View createView(ViewGroup container) {
        return configuration.getSuccessViewFactory().build(container, transaction);
    }

    @Override
    public boolean dryTriggerAction(FlowAction action, View currentView) {
        return false;
    }

    @Override
    public boolean triggerAction(FlowAction action, View currentView) {
        return false;
    }
}
