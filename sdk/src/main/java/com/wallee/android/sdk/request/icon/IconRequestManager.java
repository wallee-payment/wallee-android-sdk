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
import com.wallee.android.sdk.util.AsynchronousCallback;
import com.wallee.android.sdk.util.Check;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * The icon request manager fetches the icons of the payment methods from the remote server.
 *
 * <p>The payment method icons can be changed through the backend of wallee by the merchant and as
 * such we have to load them dynamically. This request manager helps to fetch them. However it is
 * not responsible to cache the icons in any way.</p>
 *
 * <p>Fetching the icons do not require any permissions or credentials.</p>
 */
public interface IconRequestManager {

    /**
     * This method fetches the payment method icon from the remote server.
     *
     * @param paymentMethodConfiguration the payment method for which icon should be fetched for.
     * @param callback                   the callback which is invoked once the icon has been
     *                                   loaded.
     */
    void fetchIcon(PaymentMethodConfiguration paymentMethodConfiguration, RequestCallback<PaymentMethodIcon> callback);

}
