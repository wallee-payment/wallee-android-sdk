package com.wallee.android.sdk.request.icon;

import android.util.Base64;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.wallee.android.sdk.request.RequestCallback;
import com.wallee.android.sdk.request.model.method.PaymentMethodConfiguration;
import com.wallee.android.sdk.request.model.method.PaymentMethodIcon;
import com.wallee.android.sdk.util.Check;

import java.util.Timer;

/**
 * The icon request manager fetches the icons of the payment methods from the remote server.
 *
 * <p>The payment method icons can be changed through the backend of wallee by the merchant and as
 * such we have to load them dynamically. This request manager helps to fetch them. However it is
 * not responsible to cache the icons in any way.</p>
 *
 * <p>Fetching the icons do not require any permissions or credentials. The icons can be accessed by
 * anymore and as such we do not need anything else beside the {@link RequestQueue} to fetch the
 * icons.</p>
 */
public final class VolleyIconRequestManager implements IconRequestManager{

    private final RequestQueue requestQueue;

    /**
     * Constructs a request manager with a request queue.
     *
     * @param requestQueue the request queue which should be used. The request queue should be
     *                     shared with the rest of the app.
     */
    public VolleyIconRequestManager(RequestQueue requestQueue) {
        this.requestQueue = Check.requireNonNull(requestQueue);
    }

    /**
     * This method fetches the payment method icon from the remote server.
     *
     * @param paymentMethodConfiguration the payment method for which icon should be fetched for.
     * @param callback                   the callback which is invoked once the icon has been
     *                                   loaded.
     */
    public void fetchIcon(final PaymentMethodConfiguration paymentMethodConfiguration, final RequestCallback<PaymentMethodIcon> callback) {
        this.fetchIcon(paymentMethodConfiguration.getResolvedImageUrl(), callback);
    }


    /**
     * This method fetches the payment method icon from the remote server.
     *
     * @param url      the url from which the icons should be fetched from.
     * @param callback the callback which is invoked once the icon has been loaded.
     */
    private void fetchIcon(final String url, final RequestCallback<PaymentMethodIcon> callback) {
        Response.Listener<PaymentMethodIcon> imageListener = new Response.Listener<PaymentMethodIcon>() {
            @Override
            public void onResponse(PaymentMethodIcon response) {
                callback.onSuccess(response);
            }
        };
        PaymentMethodIconRequest iconRequest = new PaymentMethodIconRequest(Request.Method.GET, url, imageListener, callback);
        requestQueue.add(iconRequest);
    }

    /**
     * This request manages the fetching of the payment method icons from the remote server.
     */
    private static class PaymentMethodIconRequest extends Request<PaymentMethodIcon> {

        private final String CONTENT_TYPE = "content-type";

        private final Response.Listener<PaymentMethodIcon> listener;
        private final String url;

        private PaymentMethodIconRequest(int method, String url, Response.Listener<PaymentMethodIcon> responseListener, Response.ErrorListener errorListener) {
            super(method, url, errorListener);
            this.listener = responseListener;
            this.url = url;
        }

        @Override
        protected Response<PaymentMethodIcon> parseNetworkResponse(NetworkResponse response) {
            try {
                String data = Base64.encodeToString(response.data, Base64.NO_WRAP);
                PaymentMethodIcon paymentMethodIcon = new PaymentMethodIcon(url, data, extractMimeType(response));
                return Response.success(paymentMethodIcon, HttpHeaderParser.parseCacheHeaders(response));
            } catch (Throwable e) {
                return Response.error(new ParseError(e));
            }
        }

        private String extractMimeType(NetworkResponse response) {
            for (String key : response.headers.keySet()) {
                if (CONTENT_TYPE.equalsIgnoreCase(key)) {
                    String mimeType = response.headers.get(key);
                    if (mimeType != null && !mimeType.isEmpty()) {
                        return mimeType;
                    } else {
                        throw new RuntimeException("Icon response does contain an invalid content type header.");
                    }
                }
            }
            throw new RuntimeException("Icon response does not contain a content type header.");
        }

        @Override
        protected void deliverResponse(PaymentMethodIcon response) {
            listener.onResponse(response);
        }
    }
}
