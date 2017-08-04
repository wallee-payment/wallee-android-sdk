package com.wallee.android.sdk.flow.listener;

/**
 * This listener is invoked before the token list is loaded from the remote server.
 */
public interface OnBeforeLoadingTokenListener extends FlowListener {

    /**
     * This method is invoked before the token list is loaded.
     */
    void onBeforeLoadingToken();

}
