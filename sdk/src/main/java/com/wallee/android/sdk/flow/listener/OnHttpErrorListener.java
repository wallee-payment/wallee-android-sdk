package com.wallee.android.sdk.flow.listener;

import com.wallee.android.sdk.flow.FlowAction;
import com.wallee.android.sdk.util.HttpError;

/**
 * This listener is invoked when there is a problem loading some HTTP page within in a
 */
public interface OnHttpErrorListener extends FlowListener {

    /**
     * This method is invoked when there occurred a HTTP error. This happens typically within a
     * {@link android.webkit.WebView}.
     *
     * @param error the error which was thrown.
     */
    void onHttpError(HttpError error);

}
