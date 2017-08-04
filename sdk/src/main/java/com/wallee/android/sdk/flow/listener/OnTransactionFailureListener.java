package com.wallee.android.sdk.flow.listener;

import com.wallee.android.sdk.request.model.base.FailureReason;
import com.wallee.android.sdk.request.model.transaction.Transaction;

/**
 * This listeners is invoked when the {@link Transaction} fails.
 *
 * <p>The transaction may fails due to invalid input provided by the user or because the payment was
 * refused (e.g. no credit left). It is also possible that the payment fails due to some
 * configuration issues on the wallee platform.</p>
 */
public interface OnTransactionFailureListener extends FlowListener {

    /**
     * This method is invoked when the transaction is considered as failed. This can happen for
     * various reasons. The {@code userFailureMessage} provides in any case a message localized to
     * the language of the user which explains to the app user what is going on.
     *
     * <p>The {@code failureReason} gives an explanation to the merchant resp. app developer. This
     * message may be logged. It can be also seen in the back office of wallee.</p>
     *
     * <p>The implementor of this method may trigger a specific behavior in case of a failure. For
     * example switch to a dedicated view which explains it.</p>
     *
     * @param transaction        the transaction which failed.
     * @param failureReason      the reason which explains to the merchant what went wrong. This
     *                           message may also help the app developer to understand what is going
     *                           wrong.
     * @param userFailureMessage this message is localized in the app user's language. It gives a
     *                           user friendly explanation what is going wrong.
     */
    void onTransactionFailure(Transaction transaction, FailureReason failureReason, String userFailureMessage);
}
