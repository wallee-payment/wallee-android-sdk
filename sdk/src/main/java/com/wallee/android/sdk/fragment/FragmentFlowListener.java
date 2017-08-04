package com.wallee.android.sdk.fragment;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.android.volley.VolleyError;
import com.wallee.android.sdk.R;
import com.wallee.android.sdk.flow.FlowAction;
import com.wallee.android.sdk.flow.FlowCoordinator;
import com.wallee.android.sdk.flow.listener.OnBeforeLoadingPaymentMethodListener;
import com.wallee.android.sdk.flow.listener.OnErrorListener;
import com.wallee.android.sdk.flow.listener.OnPaymentContainerResizeListener;
import com.wallee.android.sdk.flow.listener.OnPaymentFormValidationListener;
import com.wallee.android.sdk.flow.listener.OnPaymentFormViewReadyListener;
import com.wallee.android.sdk.flow.listener.OnPaymentMethodSelectionViewReady;
import com.wallee.android.sdk.flow.listener.OnTokenSelectedListener;
import com.wallee.android.sdk.flow.listener.OnTokenSelectionViewReady;
import com.wallee.android.sdk.flow.listener.OnTransactionFailureListener;
import com.wallee.android.sdk.flow.listener.OnTransactionSuccessListener;
import com.wallee.android.sdk.request.model.base.ClientError;
import com.wallee.android.sdk.request.model.base.ClientErrorType;
import com.wallee.android.sdk.request.model.base.FailureReason;
import com.wallee.android.sdk.request.model.base.ServerError;
import com.wallee.android.sdk.request.model.token.TokenVersion;
import com.wallee.android.sdk.request.model.transaction.Transaction;
import com.wallee.android.sdk.util.Check;
import com.wallee.android.sdk.util.HttpError;

import java.util.List;

/**
 * The fragment flow listener maps the events triggered by {@link FlowCoordinator} to the fragment.
 * This class may be overridden to realize a custom behavior of the fragment.
 *
 * @param <T> the type of the fragment which is modified by this listener.
 */
public class FragmentFlowListener<T extends AbstractPaymentFragment> implements OnTokenSelectionViewReady, OnErrorListener,
        OnPaymentFormValidationListener, OnPaymentFormViewReadyListener, OnPaymentContainerResizeListener,
        OnTransactionSuccessListener, OnTransactionFailureListener, OnTokenSelectedListener, OnBeforeLoadingPaymentMethodListener {

    private final static String TAG = FragmentFlowListener.class.getSimpleName();

    private final T fragment;
    private final PaymentContainerEnlarger enlarger;
    private final FragmentTerminationListener fragmentTerminationListener;
    private final ViewGroup fragmentView;
    private FlowCoordinator coordinator;
    private final Button formSubmitButton;
    private final Button otherPaymentMethodButton;

    /**
     * This method creates a new listener.
     *
     * @param fragment                    the fragment onto which the events should be mapped to.
     * @param enlarger                    the enlarge to use. The enlarge is responsible to enlarge
     *                                    the fragment when more space is required.
     * @param fragmentTerminationListener the listener which acts up on the final state of the
     *                                    payment flow.
     * @param fragmentView                the view of the fragment which can be modified.
     */
    protected FragmentFlowListener(T fragment, PaymentContainerEnlarger enlarger, FragmentTerminationListener fragmentTerminationListener, ViewGroup fragmentView) {
        this.fragment = Check.requireNonNull(fragment, "The fragment is required.");
        this.enlarger = Check.requireNonNull(enlarger, "The enlarger is required.");
        this.fragmentTerminationListener = Check.requireNonNull(fragmentTerminationListener, "The fragment termination listener is required.");
        this.fragmentView = Check.requireNonNull(fragmentView, "The fragment view cannot be empty.");
        this.formSubmitButton = this.setupFormSubmitButton();
        this.otherPaymentMethodButton = this.setupOtherPaymentMethodButton();
    }

    /**
     * Sets the coordinator. We cannot set the coordinator in the constructor because we need the
     * listener to create the coordinator.
     *
     * @param coordinator the coordinator
     */
    public final void setFlowCoordinator(FlowCoordinator coordinator) {
        if (this.coordinator != null) {
            throw new IllegalStateException("Once the coordinator has been set it cannot be set again.");
        }
        this.coordinator = coordinator;
    }

    /**
     * @return the fragment which is modified by this listener.
     */
    protected T getFragment() {
        return this.fragment;
    }

    /**
     * @return the fragment enlarger which is used.
     */
    protected PaymentContainerEnlarger getPaymentContainerEnlarger() {
        return this.enlarger;
    }

    /**
     * @return the android context in which the listener is executed in.
     */
    protected Context getContext() {
        return this.fragment.getActivity().getApplicationContext();
    }

    /**
     * Short cut for {@link android.app.Activity#runOnUiThread(Runnable)}.
     *
     * @param task the task which should be executed in the main thread.
     */
    protected void runOnUiThread(Runnable task) {
        this.fragment.getActivity().runOnUiThread(task);
    }

    /**
     * @return the button which triggers the submit.
     */
    protected final Button getFormSubmitButton() {
        return formSubmitButton;
    }

    /**
     * @return the button which triggers a switch from the token list to the payment method
     * selection list.
     */
    protected final Button getOtherPaymentMethodButton() {
        return otherPaymentMethodButton;
    }

    /**
     * @return the view which holds the whole fragment.
     */
    protected ViewGroup getFragmentView() {
        return fragmentView;
    }

    /**
     * This method setups a button which triggers the submit of the payment form.
     *
     * @return the submit button to use.
     */
    protected Button setupFormSubmitButton() {
        final Button button = (Button) this.getFragmentView().findViewById(R.id.wallee_button_submit_payment_form);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button.setVisibility(View.GONE);
                if (!coordinator.triggerAction(FlowAction.VALIDATE_PAYMENT_FORM)) {
                    button.setVisibility(View.VISIBLE);
                }
            }
        });
        return button;
    }

    /**
     * This method setups a button which triggers the switch form the token selection view to the
     * payment method selection view.
     *
     * @return the button which should be used to switch to the payment method selection view.
     */
    protected Button setupOtherPaymentMethodButton() {
        final Button button = (Button) this.getFragmentView().findViewById(R.id.wallee_button_other_payment_method);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button.setVisibility(View.GONE);
                if (!coordinator.triggerAction(FlowAction.SWITCH_TO_PAYMENT_METHOD_SELECTION)) {
                    button.setVisibility(View.VISIBLE);
                }
            }
        });
        return button;
    }

    @Override
    public void onValidationFailure(List<String> errors) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                getFormSubmitButton().setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onValidationSuccess() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (!coordinator.triggerAction(FlowAction.SUBMIT_PAYMENT_FORM)) {
                    getFormSubmitButton().setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public void onNetworkError(VolleyError error) {
        Log.e(TAG, "Unexpected network error occurred.", error);
        this.fragmentTerminationListener.onFailureTermination(getContext().getString(R.string.wallee_unexpected_error), null);
    }

    @Override
    public void onClientError(ClientError error) {
        if (error.getType() == ClientErrorType.END_USER_ERROR) {
            Log.i(TAG, "User error occurred. UUID: " + error.getId() + " Message: " + error.getDefaultMessage());
            this.fragmentTerminationListener.onFailureTermination(error.getMessage(), null);
        } else {
            Log.e(TAG, "Unexpected server error occurred. UUID for support: " + error.getId() +
                    " Date: " + error.getDate() + " Message: " + error.getMessage());
            this.fragmentTerminationListener.onFailureTermination(getContext().getString(R.string.wallee_unexpected_error), null);
        }
    }

    @Override
    public void onServerError(ServerError error) {
        Log.e(TAG, "Unexpected server error occurred. UUID for support: " + error.getId() +
                " Date: " + error.getDate() + " Message: " + error.getMessage());
        this.fragmentTerminationListener.onFailureTermination(getContext().getString(R.string.wallee_unexpected_error), null);
    }

    @Override
    public void onTokenSelectionViewReady() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                getOtherPaymentMethodButton().setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onPaymentFormViewReady(boolean userInputRequired) {
        if (userInputRequired) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    getFormSubmitButton().setVisibility(View.VISIBLE);
                }
            });
        } else {
            // When no user input is required we can directly send the form.
            if (!coordinator.triggerAction(FlowAction.SUBMIT_PAYMENT_FORM)) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        getFormSubmitButton().setVisibility(View.VISIBLE);
                    }
                });
            }
        }
    }

    @Override
    public void onHttpError(HttpError error) {
        Log.e(TAG, "Unexpected HTTP error: " + error.getHttpStatusCode() + " " + error.getMessage(), error);
        this.fragmentTerminationListener.onFailureTermination(getContext().getString(R.string.wallee_unexpected_error), null);
    }

    @Override
    public void onPaymentContainerEnlarge() {
        this.enlarger.enlarge();
    }

    @Override
    public void onPaymentContainerReduce() {
        this.enlarger.restore();
    }

    @Override
    public void onTransactionSuccess(Transaction transaction) {
        this.fragmentTerminationListener.onSuccessfulTermination(transaction);
    }

    @Override
    public void onTransactionFailure(Transaction transaction, FailureReason failureReason, String userFailureMessage) {
        Log.i(TAG, "The transaction failed. Failure ID: " + failureReason.getId() + " Message: " + failureReason.getDescription());
        this.fragmentTerminationListener.onFailureTermination(userFailureMessage, transaction);
    }

    @Override
    public void onTokenSelected(TokenVersion selectedTokenVersion) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                getOtherPaymentMethodButton().setVisibility(View.GONE);
            }
        });
    }


    @Override
    public void onBeforeLoadingPaymentMethod() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                getFormSubmitButton().setVisibility(View.GONE);
            }
        });
    }
}
