package com.wallee.android.sdk.flow;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.VolleyError;
import com.wallee.android.sdk.flow.config.FlowConfiguration;
import com.wallee.android.sdk.request.RequestCallback;
import com.wallee.android.sdk.request.model.transaction.Transaction;
import com.wallee.android.sdk.util.Check;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * This handler is responsible for the {@link FlowState#AWAITING_FINAL_STATE} state.
 *
 * <p>The handler shows a view which indicates to the user that there is not final result yet. In
 * the background the handler starts a thread which regularly checks if there is a final result.
 * Once a final result has been received the corresponding state is invoked.</p>
 */
final class AwaitingFinalStateHandler implements FlowStateHandler {

    private static final String TAG = AwaitingFinalStateHandler.class.getSimpleName();

    private final FlowConfiguration configuration;
    private final Transaction transaction;
    private final CoordinatorCallback coordinatorCallback;
    private final Timer timer = new Timer();
    private final AtomicInteger counter = new AtomicInteger();
    private static final int INTERVAL = 2000;

    AwaitingFinalStateHandler(CoordinatorCallback coordinatorCallback, FlowConfiguration configuration, Transaction transaction) {
        this.configuration = Check.requireNonNull(configuration, "The configuration is required.");
        this.transaction = Check.requireNonNull(transaction, "The transaction is required.");
        this.coordinatorCallback = Check.requireNonNull(coordinatorCallback, "The coordinatorCallback is required.");
    }

    @Override
    public void initialize() {
        this.coordinatorCallback.ready();
        timer.schedule(new TransactionFetcherTask(this), INTERVAL);
    }

    @Override
    public View createView(ViewGroup container) {
        return this.configuration.getAwaitingFinalStateViewFactory().build(container, this.transaction);
    }

    @Override
    public boolean dryTriggerAction(FlowAction action, View currentView) {
        return false;
    }

    @Override
    public boolean triggerAction(FlowAction action, View currentView) {
        return false;
    }

    private static class TransactionFetcherTask extends TimerTask {

        private final AwaitingFinalStateHandler stateHandler;

        private TransactionFetcherTask(AwaitingFinalStateHandler stateHandler) {
            this.stateHandler = stateHandler;
            Log.i(TAG, "Waiting for final result of the transaction.");
        }

        @Override
        public void run() {
            stateHandler.configuration.getWebServiceApiClient().readTransaction(new RequestCallback<Transaction>() {
                @Override
                public void onSuccess(Transaction object) {
                    if (object.isAwaitingFinalState()) {
                        reschedule();
                    } else if (object.isFailed()) {
                        stateHandler.coordinatorCallback.changeStateTo(FlowState.FAILURE, object);
                        Log.i(TAG, "Finally received a final transaction state: The state is failed.");
                    } else if (object.isSuccessful()) {
                        stateHandler.coordinatorCallback.changeStateTo(FlowState.SUCCESS, object);
                        Log.i(TAG, "Finally received a final transaction state: The state is successful.");
                    }
                }

                @Override
                public void onErrorResponse(VolleyError error) {
                    // In case we face here an error we try to reschedule. Because this typically
                    // happens when there is a bad internet connectivity and as such a retry makes
                    // sense.
                    reschedule();
                }
            });
        }

        private void reschedule() {
            int value = stateHandler.counter.incrementAndGet();
            long delay = (long) (INTERVAL * Math.pow(2, value));
            stateHandler.timer.schedule(new TransactionFetcherTask(stateHandler), delay);
        }

    }
}
