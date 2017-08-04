package com.wallee.android.sdk.flow;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.wallee.android.sdk.flow.config.FlowConfiguration;
import com.wallee.android.sdk.flow.listener.OnApiErrorListener;
import com.wallee.android.sdk.flow.listener.OnNetworkErrorListener;
import com.wallee.android.sdk.request.model.base.ClientError;
import com.wallee.android.sdk.request.model.base.ServerError;

import java.nio.charset.Charset;

/**
 * This class helps to handle errors.
 */
final class ErrorHandler {

    /**
     * This method is responsible to notify the flow listeners about a {@link VolleyError}.
     *
     * @param error         the error which was thrown.
     * @param configuration the configuration which is used to resolve the listeners.
     */
    static void distributeVolleyError(VolleyError error, FlowConfiguration configuration) {

        if (error.networkResponse != null && error.networkResponse.statusCode == 442) {
            String body = new String(error.networkResponse.data, Charset.forName("UTF-8"));
            Gson gson = new Gson();
            ClientError clientError = gson.fromJson(body, ClientError.class);
            for (OnApiErrorListener listener :
                    configuration.getListenersByType(OnApiErrorListener.class)) {
                listener.onClientError(clientError);
            }
        } else if (error.networkResponse != null && error.networkResponse.statusCode == 542) {
            String body = new String(error.networkResponse.data, Charset.forName("UTF-8"));
            Gson gson = new Gson();
            ServerError serverError = gson.fromJson(body, ServerError.class);
            for (OnApiErrorListener listener :
                    configuration.getListenersByType(OnApiErrorListener.class)) {
                listener.onServerError(serverError);
            }
        } else {
            for (OnNetworkErrorListener listener :
                    configuration.getListenersByType(OnNetworkErrorListener.class)) {
                listener.onNetworkError(error);
            }
        }
    }

    private ErrorHandler() {
        throw new IllegalAccessError();
    }

}
