package com.wallee.android.sdk.view.selection.token;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * This is the default view used to render the token selection list.
 *
 * <p>We use here a recycler view to make the loading more efficient in case there are a lot of
 * options to select from.</p>
 */
public class DefaultTokenListView extends RecyclerView {

    public DefaultTokenListView(Context context) {
        super(context);
    }

    public DefaultTokenListView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DefaultTokenListView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

}
