package com.wallee.android.sdk.util;

import android.net.TrafficStats;
import android.os.Build;
import android.os.Handler;
import android.os.Message;

import com.android.volley.Cache;
import com.android.volley.ExecutorDelivery;
import com.android.volley.Network;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ResponseDelivery;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.NoCache;

import java.lang.reflect.Method;

/**
 * Extension of {@link RequestQueue} which sends the request in the same thread as the caller to
 * make is more simple to test remote calls.
 *
 * <p>To really test if an integration into remote service is working it helps when the real
 * integration can be tested. This request queue allows to do so. However it requires a working API
 * and as such it can be seen as not the best test strategy, but it provides certainty about the
 * functionality of a particular integration on a level which a mocked response will never
 * provide.</p>
 */
public class SingleThreadedRequestQueue extends RequestQueue {


    private final ResponseDelivery delivery;
    private final Network network;
    private final Method parseNetworkResponseMethod;

    public SingleThreadedRequestQueue() {
        this(new NoCache(), new BasicNetwork(new HurlStack()), new ExecutorDelivery(new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                return true;
            }
        })));
    }

    public SingleThreadedRequestQueue(Cache cache, Network network,
                                      ResponseDelivery delivery) {
        super(cache, network, 1, delivery);
        this.delivery = delivery;
        this.network = network;

        // We need to access a protected method. This is not nice but the easies way to bypass the
        // protection. This class is only intended to be used within unit tests and as such this is
        // okay.
        try {
            this.parseNetworkResponseMethod = Request.class.getDeclaredMethod("parseNetworkResponse", NetworkResponse.class);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        parseNetworkResponseMethod.setAccessible(true);

    }


    @Override
    public <T> Request<T> add(Request<T> request) {
        this.executeRequest(request);
        return request;
    }

    private void addTrafficStatsTag(Request<?> request) {
        // Tag the request (if API >= 14)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            TrafficStats.setThreadStatsTag(request.getTrafficStatsTag());
        }
    }

    private void parseAndDeliverNetworkError(Request<?> request, VolleyError error) {
        this.delivery.postError(request, error);
    }

    private void executeRequest(Request<?> request) {
        // This method is mostly copied from the NetworkDispatcher but modified so it is executed
        // in the same thread and without blocking.
        try {
            request.addMarker("network-queue-take");

            // If the request was cancelled already, do not perform the
            // network request.
            if (request.isCanceled()) {
                return;
            }

            addTrafficStatsTag(request);

            // Perform the network request.
            NetworkResponse networkResponse = this.network.performRequest(request);
            request.addMarker("network-http-complete");

            // If the server returned 304 AND we delivered a response already,
            // we're done -- don't deliver a second identical response.
            if (networkResponse.notModified && request.hasHadResponseDelivered()) {
                return;
            }

            // Parse the response here on the worker thread.
            Response<?> response = (Response<?>) this.parseNetworkResponseMethod.invoke(request, networkResponse);
            request.addMarker("network-parse-complete");

            // Write to cache if applicable.
            if (request.shouldCache() && response.cacheEntry != null) {
                request.addMarker("network-cache-written");
            }

            // Post the response back.
            request.markDelivered();
            this.delivery.postResponse(request, response);
        } catch (VolleyError volleyError) {
            parseAndDeliverNetworkError(request, volleyError);
        } catch (Exception e) {
            VolleyLog.e(e, "Unhandled exception %s", e.toString());
            VolleyError volleyError = new VolleyError(e);
            this.delivery.postError(request, volleyError);
        }
    }

}