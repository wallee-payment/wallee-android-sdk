package com.wallee.android.sdk.flow.listener;


import java.util.List;

/**
 * This listener is invoked when the payment form validation completes.
 */
public interface OnPaymentFormValidationListener extends FlowListener {

    /**
     * This method is invoked when the validation fails.
     *
     * @param errors a list of error messages which may give more details about the failure.
     */
    void onValidationFailure(List<String> errors);

    /**
     * This method is invoked when the validation completed without errors.
     */
    void onValidationSuccess();

}
