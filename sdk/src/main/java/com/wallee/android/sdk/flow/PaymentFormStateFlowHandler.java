package com.wallee.android.sdk.flow;

import android.view.View;
import android.view.ViewGroup;

import com.android.volley.VolleyError;
import com.wallee.android.sdk.flow.config.FlowConfiguration;
import com.wallee.android.sdk.flow.listener.OnHttpErrorListener;
import com.wallee.android.sdk.flow.listener.OnPaymentContainerResizeListener;
import com.wallee.android.sdk.flow.listener.OnPaymentFormValidationListener;
import com.wallee.android.sdk.flow.listener.OnPaymentFormViewReadyListener;
import com.wallee.android.sdk.flow.model.SelectedPaymentMethodConfiguration;
import com.wallee.android.sdk.request.RequestCallback;
import com.wallee.android.sdk.request.model.transaction.MobileSdkUrl;
import com.wallee.android.sdk.request.model.transaction.Transaction;
import com.wallee.android.sdk.util.AsynchronousCallback;
import com.wallee.android.sdk.util.HttpError;
import com.wallee.android.sdk.view.form.PaymentFormView;
import com.wallee.android.sdk.view.form.PaymentFormViewFactory;

import java.util.List;

/**
 * This state handler is responsible for the {@link FlowState#PAYMENT_FORM} state.
 *
 * <p>The handler creates a {@link PaymentFormView} and coordinates the validation and submission of
 * the form.</p>
 *
 * @param <T> the type of the payment form view.
 */
final class PaymentFormStateFlowHandler<T extends View & PaymentFormView> implements PaymentFormView.Listener<T>, FlowStateHandler {

    private final CoordinatorCallback stateChanger;
    private final FlowConfiguration configuration;
    private final SelectedPaymentMethodConfiguration selectedPaymentMethodConfiguration;

    PaymentFormStateFlowHandler(CoordinatorCallback stateChanger, FlowConfiguration configuration, SelectedPaymentMethodConfiguration selectedPaymentMethodConfiguration) {
        this.stateChanger = stateChanger;
        this.configuration = configuration;
        this.selectedPaymentMethodConfiguration = selectedPaymentMethodConfiguration;
        this.stateChanger.waiting();
    }

    @Override
    public void initialize() {
        this.stateChanger.ready();
    }


    @Override
    public boolean dryTriggerAction(FlowAction action, View currentView) {
        return currentView instanceof PaymentFormView &&
                (action == FlowAction.SUBMIT_PAYMENT_FORM ||
                        action == FlowAction.VALIDATE_PAYMENT_FORM ||
                        (action == FlowAction.GO_BACK && !((PaymentFormView) currentView).isSubmitted()));
    }

    @Override
    public boolean triggerAction(FlowAction action, View currentView) {
        if (dryTriggerAction(action, currentView)) {
            if (action == FlowAction.SUBMIT_PAYMENT_FORM) {
                ((PaymentFormView) currentView).submit();
            } else if (action == FlowAction.VALIDATE_PAYMENT_FORM) {
                ((PaymentFormView) currentView).validate();
            } else if (action == FlowAction.GO_BACK) {
                if (((PaymentFormView) currentView).isSubmitted()) {
                    throw new IllegalStateException("This should not happen. We check that before in the dry method.");
                }
                this.stateChanger.changeStateTo(FlowState.PAYMENT_METHOD_LOADING, null);
            } else {
                throw new IllegalStateException("When the dry is correctly implemented we should never reach this code.");
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onHttpError(T view, HttpError error) {
        for (OnHttpErrorListener listener : this.configuration.getListenersByType(OnHttpErrorListener.class)) {
            listener.onHttpError(error);
        }
    }

    @Override
    public void onExpired(T view) {
        stateChanger.changeStateTo(FlowState.PAYMENT_FORM, this.selectedPaymentMethodConfiguration);
    }

    @Override
    public void onLoading(T view) {
        this.stateChanger.waiting();
    }

    @Override
    public void onReady(T view) {
        this.stateChanger.ready();
        for (OnPaymentFormViewReadyListener listener : this.configuration.getListenersByType(OnPaymentFormViewReadyListener.class)) {
            listener.onPaymentFormViewReady(view.getCurrentTargetHeight() > 0);
        }
    }

    @Override
    public void onEnlargeView(T currentView) {
        for (OnPaymentContainerResizeListener listener : this.configuration.getListenersByType(OnPaymentContainerResizeListener.class)) {
            listener.onPaymentContainerEnlarge();
        }
    }

    @Override
    public void onResetView(T currentView) {
        for (OnPaymentContainerResizeListener listener : this.configuration.getListenersByType(OnPaymentContainerResizeListener.class)) {
            listener.onPaymentContainerReduce();
        }
    }

    @Override
    public void onValidationSuccess(T currentView) {
        for (OnPaymentFormValidationListener listener : this.configuration.getListenersByType(OnPaymentFormValidationListener.class)) {
            listener.onValidationSuccess();
        }
    }

    @Override
    public void onValidationFailure(T currentView, List<String> errors) {
        for (OnPaymentFormValidationListener listener : this.configuration.getListenersByType(OnPaymentFormValidationListener.class)) {
            listener.onValidationFailure(errors);
        }
    }

    @Override
    public void onSuccess(T currentView) {
        stateChanger.waiting();
        configuration.getWebServiceApiClient().readTransaction(new RequestCallback<Transaction>() {
            @Override
            public void onSuccess(Transaction object) {
                stateChanger.changeStateTo(FlowState.SUCCESS, object);
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                ErrorHandler.distributeVolleyError(error, configuration);
            }
        });
    }

    @Override
    public void onFailure(T currentView) {
        stateChanger.waiting();
        configuration.getWebServiceApiClient().readTransaction(new RequestCallback<Transaction>() {
            @Override
            public void onSuccess(Transaction object) {
                stateChanger.changeStateTo(FlowState.FAILURE, object);
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                ErrorHandler.distributeVolleyError(error, configuration);
            }
        });
    }

    @Override
    public void onAwaitingFinalState(T currentView) {

        stateChanger.waiting();
        configuration.getWebServiceApiClient().readTransaction(new RequestCallback<Transaction>() {
            @Override
            public void onSuccess(Transaction object) {
                if (object.isAwaitingFinalState()) {
                    stateChanger.changeStateTo(FlowState.AWAITING_FINAL_STATE, object);
                } else if (object.isFailed()) {
                    stateChanger.changeStateTo(FlowState.FAILURE, object);
                } else if (object.isSuccessful()) {
                    stateChanger.changeStateTo(FlowState.SUCCESS, object);
                }
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                ErrorHandler.distributeVolleyError(error, configuration);
            }
        });
    }

    @Override
    public View createView(final ViewGroup container) {
        return this.configuration.getPaymentFormViewFactory().build(container, this, selectedPaymentMethodConfiguration.getId(), new PaymentFormViewFactory.UrlFetcher() {
            @Override
            public void fetchMobileSdkUrl(final AsynchronousCallback<MobileSdkUrl> onReadyCallback) {
                configuration.getWebServiceApiClient().buildMobileSdkUrl(new RequestCallback<MobileSdkUrl>() {
                    @Override
                    public void onSuccess(MobileSdkUrl sdkUrl) {
                        onReadyCallback.process(sdkUrl);
                    }

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        ErrorHandler.distributeVolleyError(error, configuration);
                    }
                });
            }
        });
    }
}
