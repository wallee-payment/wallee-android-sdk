package com.wallee.android.sdk.flow;

import com.wallee.android.sdk.request.model.token.Token;

/**
 * The {@link FlowCoordinator} allows the execution of certain actions on the flow. Those actions
 * depends on the current state of the coordinator.
 *
 * <p>This enum holds all possible actions which can be triggered from the outside. If a particular
 * action can be executed depends on the inner state of the coordinator. To check if a particular
 * action can be executed the {@link FlowCoordinator#dryTriggerAction(FlowAction)} can be
 * invoked.</p>
 *
 * <p>To actually execute a particular action the method {@link FlowCoordinator#triggerAction(FlowAction)}
 * can be used. See also the documentation of the actions to see when an execution is generally
 * possible.</p>
 */
public enum FlowAction {

    /**
     * <p>This action triggers the validation of the payment information entered in the {@link
     * com.wallee.android.sdk.view.form.PaymentFormView}. The validation does not send the data
     * however it checks if everything entered is in the correct format and if the required input
     * has been provided.</p>
     *
     * <p>As a result the {@link com.wallee.android.sdk.flow.listener.OnPaymentFormValidationListener}
     * is triggered. To listen to the result the mentioned listener may be implemented.</p>
     */
    VALIDATE_PAYMENT_FORM,

    /**
     * <p>This action triggers actually the sending of the payment information to the remote server
     * within the {@link com.wallee.android.sdk.view.form.PaymentFormView}. The submit will try to
     * process the data and eventually authorize the transaction.</p>
     *
     * <p>As a result either {@link com.wallee.android.sdk.flow.listener.OnTransactionSuccessListener}
     * or {@link com.wallee.android.sdk.flow.listener.OnTransactionFailureListener}.</p>
     */
    SUBMIT_PAYMENT_FORM,

    /**
     * <p>When the {@link Token}s are listed the user may want
     * to register a new token. This action allows to trigger the switch to the payment method
     * selection which allows to register a new token.</p>
     */
    SWITCH_TO_PAYMENT_METHOD_SELECTION,

    /**
     * The go back action allows to trigger a change back to the previous screen. Not in all states
     * a change back to a previous screen is easily possible and as such not for all state a go back
     * action will be executed.
     */
    GO_BACK,

}
