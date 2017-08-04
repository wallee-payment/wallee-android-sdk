package com.wallee.android.sdk.view.success;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wallee.android.sdk.R;
import com.wallee.android.sdk.request.model.transaction.Transaction;

/**
 * Default implementation of {@link SuccessViewFactory} which creates a static view.
 */
public class DefaultSuccessViewFactory implements SuccessViewFactory {
    @Override
    public View build(ViewGroup parent, Transaction transaction) {
        return LayoutInflater.from(parent.getContext()).inflate(R.layout.wallee_default_success_view, parent, false);
    }
}
