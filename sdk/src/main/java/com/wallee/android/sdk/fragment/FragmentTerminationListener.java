package com.wallee.android.sdk.fragment;

import com.wallee.android.sdk.request.model.transaction.Transaction;

/**
 * This interface informs about the final outcomes of the {@link AbstractPaymentFragment}.
 *
 * <p>The implementor has essentially the option to react on basic outcomes of the payment process.
 * Either it was successful or it failed. These events allows the implementor the option to show a
 * corresponding error message and switch into a different activity or return to the starting point
 * in case of a failure.</p>
 */
public interface FragmentTerminationListener {

    /**
     * This method is invoked when the {@code transaction} is successfully completed.
     *
     * @param transaction the transaction which is completed successfully.
     */
    void onSuccessfulTermination(Transaction transaction);

    /**
     * This method is invoked when something went wrong. This can be before the transaction was
     * processed or during the processing.
     *
     * <p>When this method is invoked normally restarting of the whole flow make sense including
     * creating a new transaction.</p>
     *
     * @param userMessage this gives a suitable message which can be shown to the user.
     * @param transaction the transaction object. This value can be {@code null} when the
     *                    transaction was not created or used until the failure.
     */
    void onFailureTermination(String userMessage, Transaction transaction);

}
