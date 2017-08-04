package com.wallee.android.sdk.view.selection.method;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wallee.android.sdk.R;
import com.wallee.android.sdk.request.model.method.PaymentMethodConfiguration;
import com.wallee.android.sdk.view.selection.PaymentMethodImageView;
import com.wallee.android.sdk.request.model.method.PaymentMethodIcon;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This is the adapter which handles the {@link DefaultPaymentMethodListView}.
 */
public class DefaultPaymentMethodListViewAdapter extends RecyclerView.Adapter<DefaultPaymentMethodListViewAdapter.ViewHolder> {
    private List<PaymentMethodConfiguration> paymentMethods;
    private PaymentMethodListViewFactory.PaymentMethodListViewListener paymentMethodListViewListener;
    private Map<PaymentMethodConfiguration, PaymentMethodIcon> icons;

    /**
     * Hols the payment list item views.
     */
    public final class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final PaymentMethodImageView iconView;
        private final TextView nameTextView;

        protected ViewHolder(View v) {
            super(v);
            nameTextView = (TextView) v.findViewById(R.id.wallee_payment_method_name);
            iconView = (PaymentMethodImageView) v.findViewById(R.id.wallee_payment_method_icon);
            v.setOnClickListener(this);
            iconView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onItemClicked(this.getAdapterPosition());
        }
    }

    public DefaultPaymentMethodListViewAdapter(List<PaymentMethodConfiguration> paymentMethods, PaymentMethodListViewFactory.PaymentMethodListViewListener paymentMethodListViewListener, Map<PaymentMethodConfiguration, PaymentMethodIcon> icons) {
        this.paymentMethodListViewListener = paymentMethodListViewListener;

        List<PaymentMethodConfiguration> list = new ArrayList<>(paymentMethods);
        Collections.sort(list);
        this.paymentMethods = Collections.unmodifiableList(list);
        this.icons = Collections.unmodifiableMap(new HashMap<>(icons));
    }

    @Override
    public DefaultPaymentMethodListViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.wallee_payment_method_item_view, parent, false);
        return new DefaultPaymentMethodListViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DefaultPaymentMethodListViewAdapter.ViewHolder holder, int position) {
        holder.nameTextView.setText(paymentMethods.get(position).getName());
        holder.iconView.setIcon(icons.get(paymentMethods.get(position)));
    }

    private void onItemClicked(int position) {
        if (position < paymentMethods.size()) {
            paymentMethodListViewListener.onPaymentMethodClicked(paymentMethods.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return paymentMethods.size();
    }
}
