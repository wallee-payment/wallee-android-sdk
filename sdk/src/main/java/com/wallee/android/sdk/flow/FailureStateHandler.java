package com.wallee.android.sdk.flow;

import android.view.View;
import android.view.ViewGroup;

import com.wallee.android.sdk.flow.config.FlowConfiguration;
import com.wallee.android.sdk.flow.listener.OnTransactionFailureListener;
import com.wallee.android.sdk.flow.listener.OnTransactionSuccessListener;
import com.wallee.android.sdk.request.model.transaction.Transaction;
import com.wallee.android.sdk.util.Check;

/**
 * This state handler is responsible for handling {@link FlowState#FAILURE}.
 *
 * <p>When the flow fails this handler will show an error view with the failure message.</p>
 */
final class FailureStateHandler implements FlowStateHandler {
    private final CoordinatorCallback coordinatorCallback;
    private final FlowConfiguration configuration;
    private final Transaction transaction;

    FailureStateHandler(CoordinatorCallback coordinatorCallback, FlowConfiguration configuration, Transaction transaction) {
        this.coordinatorCallback = Check.requireNonNull(coordinatorCallback, "The coordinatorCallback is required.");
        this.configuration = Check.requireNonNull(configuration, "The configuration is required.");
        this.transaction = Check.requireNonNull(transaction, "The transaction is required.");
    }

    @Override
    public void initialize() {
        for (OnTransactionFailureListener listener : this.configuration.getListenersByType(OnTransactionFailureListener.class)) {
            listener.onTransactionFailure(this.transaction, this.transaction.getFailureReason(), this.transaction.getUserFailureMessage());
        }
        this.coordinatorCallback.ready();
    }

    @Override
    public View createView(ViewGroup container) {
        return configuration.getFailureViewFactory().build(container, transaction);
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
