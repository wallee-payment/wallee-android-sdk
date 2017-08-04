package com.wallee.android.sdk.flow.listener;

import com.android.volley.VolleyError;

/**
 * This listener is invoked when there was a network error.
 */
public interface OnNetworkErrorListener extends FlowListener {

    /**
     * This methdo is invoked when the communication between the mobile device and the wallee API
     * fails.
     *
     * @param error the error which has been thrown. It may gives more details about what went
     *              wrong.
     */
    void onNetworkError(VolleyError error);
}
