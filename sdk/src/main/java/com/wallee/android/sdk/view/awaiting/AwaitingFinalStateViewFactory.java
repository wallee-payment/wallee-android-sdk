package com.wallee.android.sdk.view.awaiting;

import android.view.View;
import android.view.ViewGroup;

import com.wallee.android.sdk.request.model.transaction.Transaction;

/**
 * This view factory allows to create the view shown when there is no final transaction state.
 *
 * <p>A transaction may not switch into a final state immediately. This may take some time. This
 * factory is responsible to create a view which indicates to the user that a final result will
 * arrive within serveral minutes.</p>
 *
 * <p>Implementing a dedicated factory allows to override the view shown to the user.</p>
 */
public interface AwaitingFinalStateViewFactory {

    /**
     * This method is responsible to create a corresponding view. The implementor may return an
     * arbitrary view which is explaining the situation to the user.
     *
     * <p>The implementor does not need to add the view to the parent. This is done by the
     * caller.</p>
     *
     * @param parent      the parent view into which the provided view will be integrated into.
     * @param transaction the transaction for which no final result exists yet.
     * @return the view which is shown to the user.
     */
    View build(ViewGroup parent, Transaction transaction);

}
