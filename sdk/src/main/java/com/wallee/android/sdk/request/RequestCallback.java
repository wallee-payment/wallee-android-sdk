package com.wallee.android.sdk.request;

import com.android.volley.Response;
import com.wallee.android.sdk.request.api.VolleyWebServiceApiClient;

/**
 * This callback is invoked as a result handler for {@link VolleyWebServiceApiClient}. The implementor will be
 * called once the result is present and the processing can continue.
 *
 * @param <T> the response object type.
 */
public interface RequestCallback<T> extends Response.ErrorListener {

    /**
     * This method is invoked when the call was successful. The passed {@code object} contains the
     * parsed response object from the wallee web service.
     *
     * @param object the object which is result of the remote invocation.
     */
    void onSuccess(T object);

}
