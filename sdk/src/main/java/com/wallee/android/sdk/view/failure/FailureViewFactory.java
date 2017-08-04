package com.wallee.android.sdk.view.failure;

import android.view.View;
import android.view.ViewGroup;

import com.wallee.android.sdk.request.model.transaction.Transaction;

/**
 * This factory is responsible to create a view which is shown when the transaction is failing.
 *
 * <p>A transaction may fail during the authorization. This factory creates a view which is shown in
 * this situation. The view should explain to the user why it is failing.</p>
 *
 * <p>Implementing a dedicated factory allows to override the view shown to the user.</p>
 */
public interface FailureViewFactory {

    /**
     * This method is responsible to create the view which is shown to the customer when the
     * transaction is failing.
     *
     * <p>The implementor does not have to add the created view into the {@code parent}.</p>
     *
     * <p>The {@link Transaction#getUserFailureMessage()} describes in the user language what went
     * wrong.</p>
     *
     * @param parent      the parent view into which the created view is integrated into.
     * @param transaction the transaction which is failed.
     * @return the view which is shown to the user when the transaction is failing.
     */
    View build(ViewGroup parent, Transaction transaction);


}
