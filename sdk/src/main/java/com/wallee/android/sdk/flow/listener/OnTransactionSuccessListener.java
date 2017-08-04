package com.wallee.android.sdk.flow.listener;

import com.wallee.android.sdk.request.model.transaction.Transaction;

/**
 * This listener is called when the {@link Transaction}
 * succeeds.
 */
public interface OnTransactionSuccessListener extends FlowListener {

    /**
     * This method is called when the {@code transaction} is considered as successful.
     *
     * <p>Be aware if someone is decompile the app this method can be also invoked. There is no
     * guarantee that the invocation of this method is secured. Means someone can trigger it even
     * the transaction is not successful. As such any action taken within this method it should only
     * affect the user interface. To enable something or to grant something a webhook should be used
     * to inform the app backend. So with other words: The implementor of this listener should not
     * trigger something in the app backend. This should be done through a <a
     * href="https://app-wallee.com/doc/webhooks">webhook</a>.</p>
     *
     * @param transaction the transaction which succeeds.
     */
    void onTransactionSuccess(Transaction transaction);
}
