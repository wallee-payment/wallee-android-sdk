package com.wallee.android.sdk.flow;

import android.os.Parcelable;

import com.wallee.android.sdk.flow.config.FlowConfiguration;
import com.wallee.android.sdk.flow.model.SelectedPaymentMethodConfiguration;
import com.wallee.android.sdk.request.model.transaction.Transaction;

/**
 * The flow state represents the different states in which the {@link FlowCoordinator} can switch
 * into.
 *
 * <p>Each state has a handler which is created newly whenever the coordinator enters the particular
 * state. Each handler can display a view with a particular content. The handler can also trigger a
 * state change which makes itself obsolete and a new state handler will take over the
 * responsibility.</p>
 *
 * <p>The state is package protected to avoid that the app itself depends on the particular states.
 * The app should rather react on the different {@link com.wallee.android.sdk.flow.listener.FlowListener}.
 * They give more precise and more consistent view into the state of the payment flow. Especially
 * the allow to react based upon certain events.</p>
 */
enum FlowState {

    /**
     * The loading token state loads the available tokens from the remote server. Once they have
     * been loaded we switch into the {@link #TOKEN_SELECTION} state. In case there are no token
     * available the coordinator will switch into the {@link #PAYMENT_METHOD_LOADING} state.
     *
     * <p>This is always the initial state.</p>
     *
     * <p><strong>Valid Next States:</strong> {@link #TOKEN_SELECTION}, {@link
     * #PAYMENT_METHOD_LOADING}</p>
     */
    TOKEN_LOADING {
        @Override
        FlowStateHandler createStateHandler(CoordinatorCallback coordinatorCallback, FlowConfiguration configuration, Parcelable argument) {
            return new TokenLoadingStateHandler(coordinatorCallback, configuration);
        }
    },

    /**
     * The token selection state allows the user to select from a set of stored tokens one to
     * process the transaction.
     *
     * <p><strong>Valid Next States:</strong> {@link #SUCCESS}, {@link #FAILURE}, {@link
     * #AWAITING_FINAL_STATE}, {@link #PAYMENT_METHOD_LOADING}</p>
     */
    TOKEN_SELECTION {
        @Override
        FlowStateHandler createStateHandler(CoordinatorCallback coordinatorCallback, FlowConfiguration configuration, Parcelable argument) {
            return new TokenSelectionStateHandler(coordinatorCallback, configuration, argument);
        }
    },

    /**
     * The payment method loading state loads the payment methods form the remove server. Once they
     * have been loaded the coordinator switches into the {@link #PAYMENT_METHOD_SELECTION} state.
     *
     * <p><strong>Valid Next States:</strong> {@link #PAYMENT_METHOD_SELECTION}</p>
     */
    PAYMENT_METHOD_LOADING {
        @Override
        FlowStateHandler createStateHandler(CoordinatorCallback coordinatorCallback, FlowConfiguration configuration, Parcelable argument) {
            return new PaymentMethodLoadingStateHandler(coordinatorCallback, configuration);
        }
    },

    /**
     * The payment method selection state presents to the user a screen with different payment
     * methods from which once can be selected.
     *
     * <p><strong>Valid Next States:</strong> {@link #PAYMENT_FORM}</p>
     */
    PAYMENT_METHOD_SELECTION {
        @Override
        FlowStateHandler createStateHandler(CoordinatorCallback coordinatorCallback, FlowConfiguration configuration, Parcelable argument) {
            return new PaymentMethodSelectionStateHandler(coordinatorCallback, configuration, argument);
        }
    },

    /**
     * The payment form state presents to the user a form which collects the relevant payment
     * information form the user.
     *
     * <p><strong>Valid Next States:</strong> {@link #SUCCESS}, {@link #FAILURE}, {@link
     * #AWAITING_FINAL_STATE}</p>
     */
    PAYMENT_FORM {
        @Override
        FlowStateHandler createStateHandler(CoordinatorCallback coordinatorCallback, FlowConfiguration configuration, Parcelable argument) {
            return new PaymentFormStateFlowHandler(coordinatorCallback, configuration, (SelectedPaymentMethodConfiguration) argument);
        }
    },

    /**
     * The success state will present a success view to the user.
     *
     * <p><strong>Valid Next States:</strong> This state is final.</p>
     */
    SUCCESS {
        @Override
        FlowStateHandler createStateHandler(CoordinatorCallback coordinatorCallback, FlowConfiguration configuration, Parcelable argument) {
            return new SuccessStateHandler(coordinatorCallback, configuration, (Transaction)argument);
        }
    },

    /**
     * The failure state will present a failure view to the user.
     *
     * <p><strong>Valid Next States:</strong> This state is final.</p>
     */
    FAILURE {
        @Override
        FlowStateHandler createStateHandler(CoordinatorCallback coordinatorCallback, FlowConfiguration configuration, Parcelable argument) {
            return new FailureStateHandler(coordinatorCallback, configuration, (Transaction) argument);
        }
    },

    /**
     * The transaction may not switch into a final state immediately. This means that the user may
     * need to wait a while until a final result can be presented. This state presents to the user a
     * message which explains this and eventually switch into a final state.
     *
     * <p><strong>Valid Next States:</strong> {@link #SUCCESS}, {@link #FAILURE}</p>
     */
    AWAITING_FINAL_STATE {
        @Override
        FlowStateHandler createStateHandler(CoordinatorCallback coordinatorCallback, FlowConfiguration configuration, Parcelable argument) {
            return new AwaitingFinalStateHandler(coordinatorCallback, configuration, (Transaction) argument);
        }
    };

    abstract FlowStateHandler createStateHandler(CoordinatorCallback coordinatorCallback, FlowConfiguration configuration, Parcelable argument);

}
