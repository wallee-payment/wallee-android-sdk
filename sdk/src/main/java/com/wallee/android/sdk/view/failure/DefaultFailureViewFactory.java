package com.wallee.android.sdk.view.failure;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wallee.android.sdk.R;
import com.wallee.android.sdk.request.model.transaction.Transaction;

/**
 * Default implementation of {@link FailureViewFactory} which provides a static view with the error
 * message.
 */
public class DefaultFailureViewFactory implements FailureViewFactory {

    @Override
    public View build(ViewGroup parent, Transaction transaction) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.wallee_default_failure_view, parent, false);

        TextView textView = (TextView) view.findViewById(R.id.wallee_user_failure_message);
        textView.setText(transaction.getUserFailureMessage());

        return view;
    }
}
