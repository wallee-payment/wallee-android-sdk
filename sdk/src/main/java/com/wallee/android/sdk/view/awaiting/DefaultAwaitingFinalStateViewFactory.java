package com.wallee.android.sdk.view.awaiting;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wallee.android.sdk.R;
import com.wallee.android.sdk.request.model.transaction.Transaction;

/**
 * This is a default implementation of {@link AwaitingFinalStateViewFactory} which shows a static
 * text which explains the situation.
 */
public class DefaultAwaitingFinalStateViewFactory implements AwaitingFinalStateViewFactory {
    @Override
    public View build(ViewGroup parent, Transaction transaction) {
        return LayoutInflater.from(parent.getContext()).inflate(R.layout.wallee_default_awaiting_final_state_view, parent, false);
    }
}
