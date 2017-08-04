package com.wallee.android.sdk.flow.config;


import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.util.concurrent.atomic.AtomicReference;

/**
 * This class holds a reference of the request queue in a static way. This means we will use always
 * the same queue. This is a solution to make the usage of the {@link
 * com.wallee.android.sdk.flow.FlowCoordinator} more minimal. However from a performance perspective
 * the queue has to be provided by the {@link android.app.Activity} so that other app requests get
 * pooled.
 */
public final class VolleyRequestQueueHolder {

    private final static AtomicReference<RequestQueue> requestQueue = new AtomicReference<>();

    /**
     * Returns the current request queue.
     *
     * @param context the context which should be used to create a request queue when there is not
     *                already a request queue created.
     * @return the request queue.
     */
    public static RequestQueue getRequestQueue(Context context) {
        if (requestQueue.get() == null) {
            requestQueue.compareAndSet(null, Volley.newRequestQueue(context));
        }
        return requestQueue.get();
    }

}
