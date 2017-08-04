package com.wallee.android.sdk.view.selection.method;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * Default view for listing the payment methods.
 */
public class DefaultPaymentMethodListView extends RecyclerView {
    public DefaultPaymentMethodListView(Context context) {
        super(context);
    }

    public DefaultPaymentMethodListView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DefaultPaymentMethodListView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

}
