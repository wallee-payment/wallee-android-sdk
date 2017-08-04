package com.wallee.android.sdk.view.selection.token;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wallee.android.sdk.R;
import com.wallee.android.sdk.request.model.method.PaymentMethodConfiguration;
import com.wallee.android.sdk.request.model.token.TokenVersion;
import com.wallee.android.sdk.view.selection.PaymentMethodImageView;
import com.wallee.android.sdk.request.model.method.PaymentMethodIcon;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This is the adapter of the {@link DefaultTokenListView} which inserts the data into the recycler
 * view.
 */
public class DefaultTokenListViewAdapter extends RecyclerView.Adapter<DefaultTokenListViewAdapter.ViewHolder> {
    private final List<TokenVersion> tokenVersions;
    private final TokenListViewFactory.TokenViewListener tokenClickHandler;
    private final Map<PaymentMethodConfiguration, PaymentMethodIcon> icons;

    /**
     * This method constructs a new adapter. The list of tokens and icons is fixed and cannot be
     * changed.
     *
     * @param tokens   the tokens which should be shown.
     * @param listener the listener which should act upon the events triggered within the view.
     * @param icons    the icons which should be used for the payment methods.
     */
    public DefaultTokenListViewAdapter(List<TokenVersion> tokens, TokenListViewFactory.TokenViewListener listener,
                                       Map<PaymentMethodConfiguration, PaymentMethodIcon> icons) {
        List<TokenVersion> list = new ArrayList<>(tokens);
        Collections.sort(list);
        this.tokenVersions = Collections.unmodifiableList(list);
        this.tokenClickHandler = listener;
        this.icons = Collections.unmodifiableMap(new HashMap<>(icons));
    }

    /**
     * The view holder which holds the view with the tokens.
     */
    public final class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView paymentMethodNameTextView;
        private final TextView tokenTextView;
        private final PaymentMethodImageView iconView;


        protected ViewHolder(View v) {
            super(v);
            paymentMethodNameTextView = (TextView) v.findViewById(R.id.payment_method_name_view);
            tokenTextView = (TextView) v.findViewById(R.id.token_name_view);
            iconView = (PaymentMethodImageView) v.findViewById(R.id.token_payment_method_icon_view);
            v.setOnClickListener(this);
            iconView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            triggerClickEvent(this.getAdapterPosition());
        }
    }

    @Override
    public DefaultTokenListViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.wallee_token_version_item_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        TokenVersion version = tokenVersions.get(position);
        String paymentMethodName = version
                .getPaymentConnectorConfiguration()
                .getPaymentMethodConfiguration()
                .getResolvedTitle(holder.iconView.getContext().getResources().getConfiguration().locale);
        holder.paymentMethodNameTextView.setText(paymentMethodName);
        holder.tokenTextView.setText(version.getName());
        holder.iconView.setIcon(icons.get(version.getPaymentConnectorConfiguration().getPaymentMethodConfiguration()));
    }

    /**
     * This method triggers click event based up on the position of the entry which has been
     * clicked.
     *
     * @param position the position of the entry which has been clicked.
     */
    protected final void triggerClickEvent(int position) {
        if (position < tokenVersions.size()) {
            tokenClickHandler.onTokenClicked(tokenVersions.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return tokenVersions.size();
    }
}

