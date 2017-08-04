package com.wallee.android.sdk.flow.listener;

import com.wallee.android.sdk.request.model.token.TokenVersion;

/**
 * This listener is invoked when the token has been selected in the token view list.
 */
public interface OnTokenSelectedListener extends FlowListener {

    /**
     * This method is invoked when the user has chosen a token with which the payment should be
     * completed with.
     *
     * @param selectedTokenVersion the token which has been selected by the user to complete the
     *                             transaction with.
     */
    void onTokenSelected(TokenVersion selectedTokenVersion);

}
