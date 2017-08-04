package com.wallee.android.sdk.view;

import android.view.View;

/**
 * This interface defines a view to expire automatically after awhile. The listener allows to act on
 * the fact when the view is expired.
 *
 * @param <V> the type of the view.
 */
public interface ExpiringViewListener<V extends View> {

    /**
     * This method is invoked when the view has been expired. The implementor should take some
     * action to solve this issue. For example reload the view or remove it and cancel the payment
     * process.
     *
     * @param view the view which has been expired.
     */
    void onExpired(V view);

}
