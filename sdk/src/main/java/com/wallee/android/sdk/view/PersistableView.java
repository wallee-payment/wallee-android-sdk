package com.wallee.android.sdk.view;

import android.os.Bundle;

/**
 * When the view implements this interface the {@link com.wallee.android.sdk.flow.FlowCoordinator}
 * will save resp. restore the view state by calling the respective method.
 *
 * <p>{@link android.webkit.WebView} and other views work differently when it comes to store the
 * state. As such the view can decide by itself how the storage should occur.</p>
 */
public interface PersistableView {

    /**
     * This method is called when the state of the view should be persisted.
     *
     * <p>The implementor may store within this method the data which should a destruction of the
     * view should survive.</p>
     *
     * @param outState the bundle into which the view data should be written into.
     */
    void saveViewState(Bundle outState);

    /**
     * This method is called to restore the state from {@code inState} bundle.
     *
     * <p>The implementor may read the data which are relevant from the bundle and restore the
     * view's inner state.</p>
     *
     * @param inState the bundle from which the state should be reconstructed.
     */
    void restoreViewState(Bundle inState);

}
