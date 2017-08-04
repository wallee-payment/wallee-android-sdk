package com.wallee.android.sdk.flow.listener;

import com.wallee.android.sdk.request.model.base.ClientError;
import com.wallee.android.sdk.request.model.base.ServerError;

/**
 * This listener is invoked when there occurred an error during invoking the wallee web service API.
 */
public interface OnApiErrorListener extends FlowListener {

    /**
     * This method is invoked when there was a client error. For example the user has provided an
     * invalid card number or the invocation fails because somehow the service was used in a wrong
     * way.
     *
     * <p>So client error means that either the app user, the developer which invokes the web
     * service API or the user which configured the space has done something wrong.</p>
     *
     * @param error the error which may gives more details about the issue.
     */
    void onClientError(ClientError error);

    /**
     * This method is invoked when there was an error which was caused by a bug within the wallee
     * platform itself. The error references a UUID which can be provided to the support to fix the
     * issue. This method may be invoked also when the user has done something wrong, but the
     * handling inside the platform of the misbehavior is not handled properly (which is also a
     * bug).
     *
     * @param error the error which gives more details about the failure.
     */
    void onServerError(ServerError error);
}
