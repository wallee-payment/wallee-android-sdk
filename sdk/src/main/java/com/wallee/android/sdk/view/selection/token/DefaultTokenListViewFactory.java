package com.wallee.android.sdk.view.selection.token;

import android.support.v7.widget.LinearLayoutManager;
import android.view.ViewGroup;

import com.wallee.android.sdk.request.model.method.PaymentMethodConfiguration;
import com.wallee.android.sdk.request.model.token.TokenVersion;
import com.wallee.android.sdk.request.model.method.PaymentMethodIcon;

import java.util.List;
import java.util.Map;


/**
 * This implementation of {@link TokenListViewFactory} provides a default list view of the token
 * list in a recycler view.
 */
public class DefaultTokenListViewFactory implements TokenListViewFactory {

    @Override
    public DefaultTokenListView build(ViewGroup parent, TokenViewListener listener, List<TokenVersion> tokens, Map<PaymentMethodConfiguration, PaymentMethodIcon> icons) {
        DefaultTokenListView recyclerView = new DefaultTokenListView(parent.getContext());
        DefaultTokenListViewAdapter adapter = new DefaultTokenListViewAdapter(tokens, listener, icons);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager llm = new LinearLayoutManager(parent.getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        return recyclerView;
    }
}
