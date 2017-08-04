package com.wallee.android.sdk.view.success;

import android.view.View;
import android.view.ViewGroup;

import com.wallee.android.sdk.request.model.transaction.Transaction;

/**
 * This factory is responsible to create a view which is shown when the transaction is succeeding.
 *
 * <p>A transaction succeeds when at least the authorization of the transaction is successful. This
 * factory creates a view which is shown in this situation.</p>
 *
 * <p>Implementing a dedicated factory allows to override the view shown to the user.</p>
 */
public interface SuccessViewFactory {

    /**
     * This method is responsible to create the view which is shown to the customer when the
     * transaction is succeeding.
     *
     * <p>The implementor does not have to add the created view into the {@code parent}.</p>
     *
     * @param parent      the parent view into which the created view is integrated into.
     * @param transaction the transaction which is succeeding.
     * @return the view which is shown to the user when the transaction is succeeding.
     */
    View build(ViewGroup parent, Transaction transaction);
}
