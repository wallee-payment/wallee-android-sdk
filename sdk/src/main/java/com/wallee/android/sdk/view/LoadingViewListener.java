package com.wallee.android.sdk.view;

import android.view.View;

/**
 * A loading view is a {@link View} which can indicate whether it is in a loading state or in a
 * onTokenSelectionViewReady state. This means that the view will not be usable in certain situations. In this case we
 * have to communicate to the user that the view is not in a usable state resp. we can show a
 * loading spinner. The view should automatically return to a usable state automatically. Normally
 * the view is triggering some fetching of information from a remote server.
 *
 * @param <V> the view type.
 */
public interface LoadingViewListener<V extends View> {

    /**
     * This method is invoked when the {@code view} is not onTokenSelectionViewReady and requires to load something.
     *
     * @param view the view which is not onTokenSelectionViewReady and requires to load something.
     */
    void onLoading(V view);

    /**
     * This method is invoked when the {@code view} is onTokenSelectionViewReady and the content can be shown to the
     * user.
     *
     * @param view the view which is onTokenSelectionViewReady and can be shown.
     */
    void onReady(V view);


}
