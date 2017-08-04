package com.wallee.android.sdk.request.api;

import com.wallee.android.sdk.credentials.Credentials;
import com.wallee.android.sdk.request.icon.IconRequestManager;
import com.wallee.android.sdk.request.RequestCallback;
import com.wallee.android.sdk.request.model.method.PaymentMethodConfiguration;
import com.wallee.android.sdk.request.model.token.Token;
import com.wallee.android.sdk.request.model.token.TokenVersion;
import com.wallee.android.sdk.request.model.transaction.MobileSdkUrl;
import com.wallee.android.sdk.request.model.transaction.Transaction;

import java.util.List;

/**
 * The request manager is responsible for the communication with the wallee web service.
 *
 * <p>The request manager provides an asynchronous way to communicate with the web service API. This
 * implies to call the remote service a callback as to be provided which is invoked once the request
 * has completed.</p>
 *
 * <p>The request manager is not responsible to cache the returned result.</p>
 */
public interface WebServiceApiClient {

    /**
     * This method fetches the {@link TokenVersion} which may be used by the app user. The returned
     * tokens are intended to be uses as one-click tokens. The method will only return valid tokens.
     *
     * @param callback the callback which is invoked once the token versions have been loaded.
     * @see <a href="https://app-wallee.com/doc/api/web-service#transaction-service--process-one-click-token-with-credentials">Service
     * Definition</a>
     */
    void fetchTokenVersions(final RequestCallback<List<TokenVersion>> callback);


    /**
     * This method constructs the URL which can be used to render the payment form within a {@link
     * android.webkit.WebView}. The URL is used for example in {@link com.wallee.android.sdk.view.form.DefaultPaymentFormView}.
     *
     * @param callback the callback which is invoked once the URL has been loaded.
     * @see <a href="https://app-wallee.com/doc/api/web-service#transaction-service--build-mobile-sdk-url-with-credentials">Service
     * Definition</a>
     */
    void buildMobileSdkUrl(final RequestCallback<MobileSdkUrl> callback);


    /**
     * This method fetches the possible payment methods from the remote server. The returned methods
     * depend on the transaction which is linked with the {@link Credentials}. As such the method
     * may return less payment methods as configured in the wallee backend.
     *
     * <p>The payment method configuration is linked with an icon. This icon can be loaded by {@link
     * IconRequestManager#fetchIcon(PaymentMethodConfiguration, RequestCallback)}.</p>
     *
     * @param callback the callback which is invoked once all the payment methods has been loaded.
     * @see <a href="https://app-wallee.com/doc/api/web-service#transaction-service--fetch-possible-payment-methods-with-credentials">Service
     * Definition</a>
     */
    void fetchPaymentMethodConfigurations(final RequestCallback<List<PaymentMethodConfiguration>> callback);

    /**
     * This method fetches the transaction object. This is useful to determine the current state of
     * the transaction or to fetch the details of the transaction.
     *
     * @param callback the callback which is invoked once the transaction has been loaded.
     * @see <a href="https://app-wallee.com/doc/api/web-service#transaction-service--read-with-credentials">Service
     * Definition</a>
     */
    void readTransaction(final RequestCallback<Transaction> callback);

    /**
     * This method processes the transaction with the given {@code token}. Processing means the
     * remote server is invoked to authorize the transaction.
     *
     * @param token    the token which should be used to process the transaction.
     * @param callback the callback which is invoked once the remote server gives a response.
     * @see <a href="https://app-wallee.com/doc/api/web-service#transaction-service--process-one-click-token-with-credentials">Service
     * Definition</a>
     */
    void processOneClickToken(final Token token, final RequestCallback<Transaction> callback);
}
