package com.wallee.android.sdk.request.api;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wallee.android.sdk.credentials.Credentials;
import com.wallee.android.sdk.credentials.CredentialsProvider;
import com.wallee.android.sdk.request.RequestCallback;
import com.wallee.android.sdk.request.model.method.PaymentMethodConfiguration;
import com.wallee.android.sdk.request.model.token.Token;
import com.wallee.android.sdk.request.model.token.TokenVersion;
import com.wallee.android.sdk.request.model.transaction.MobileSdkUrl;
import com.wallee.android.sdk.request.model.transaction.Transaction;
import com.wallee.android.sdk.util.AsynchronousCallback;
import com.wallee.android.sdk.util.Check;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;

/**
 * The request manager is responsible for the communication with the wallee web service.
 *
 * <p>The request manager is implemented in an asynchronous way. This implies to call the remote
 * service a callback as to be provided which is invoked once the request has completed.</p>
 *
 * <p>The request manager uses the {@link CredentialsProvider} to fetch the {@link
 * com.wallee.android.sdk.credentials.Credentials} to access the web service.</p>
 *
 * <p>The request manager is not responsible to cache the returned result.</p>
 */
public final class VolleyWebServiceApiClient implements WebServiceApiClient {

    /**
     * The default URL to which the requests will be sent to.
     */
    public static final String DEFAULT_BASE_URL = "https://app-wallee.com/api/";

    private final RequestQueue requestQueue;
    private final CredentialsProvider credentialsProvider;
    private final String baseUrl;

    /**
     * Constructs a new request manager. The created instance will use the {@link #DEFAULT_BASE_URL}
     * URL.
     *
     * @param requestQueue        the request queue which should be used. The request queue pools
     *                            the requests which the request manager sends to the remote server.
     *                            The request queue may be shared with the rest of the app.
     * @param credentialsProvider the credential provider which should be used. The credential
     *                            provider gives access to the credentials which the request manager
     *                            requires to access the remote server.
     */
    public VolleyWebServiceApiClient(RequestQueue requestQueue, CredentialsProvider credentialsProvider) {
        this(requestQueue, credentialsProvider, DEFAULT_BASE_URL);
    }

    /**
     * Constructs a new request manager.
     *
     * @param requestQueue        the request queue which should be used. The request queue pools
     *                            the requests which the request manager sends to the remote server.
     *                            The request queue may be shared with the rest of the app.
     * @param credentialsProvider the credential provider which should be used. The credential
     *                            provider gives access to the credentials which the request manager
     *                            requires to access the remote server.
     * @param baseUrl             the base URL to which the requests should be sent to. This may
     *                            differ when using another environment. This is mainly here for
     *                            testing purposes.
     */
    public VolleyWebServiceApiClient(RequestQueue requestQueue, CredentialsProvider credentialsProvider, String baseUrl) {
        Check.requireNonEmpty(baseUrl, "The base url is required.");
        baseUrl = baseUrl.trim();
        if (baseUrl.endsWith("/")) {
            baseUrl = baseUrl.substring(0, baseUrl.length() - 1);
        }
        baseUrl = baseUrl + "/";

        this.requestQueue = Check.requireNonNull(requestQueue, "The request queue is required.");
        this.baseUrl = baseUrl;
        this.credentialsProvider = Check.requireNonNull(credentialsProvider, "The credentialsProvider is required.");
    }

    @Override
    public void fetchTokenVersions(final RequestCallback<List<TokenVersion>> callback) {
        AsynchronousCallback<Credentials> credentialsRequestCallback = new AsynchronousCallback<Credentials>() {
            @Override
            public void process(Credentials parameter) {
                String url = baseUrl + "transaction/fetchOneClickTokensWithCredentials?credentials=" + parameter.getCredentials();
                Response.Listener<JSONArray> handler = new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Gson gson = new Gson();
                        Type listType = new TypeToken<List<TokenVersion>>() {
                        }.getType();
                        List<TokenVersion> tokenVersions = gson.fromJson(response.toString(), listType);
                        callback.onSuccess(tokenVersions);
                    }
                };
                JsonRequest request = new JsonArrayRequest(Request.Method.GET, url, null, handler, callback);
                requestQueue.add(request);
            }
        };

        credentialsProvider.getCredentials(credentialsRequestCallback);
    }

    @Override
    public void buildMobileSdkUrl(final RequestCallback<MobileSdkUrl> callback) {

        AsynchronousCallback<Credentials> credentialsRequestCallback = new AsynchronousCallback<Credentials>() {
            @Override
            public void process(Credentials parameter) {
                String url = baseUrl + "transaction/buildMobileSdkUrlWithCredentials?credentials=" + parameter.getCredentials();
                Response.Listener<String> handler = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        long expiryDate = System.currentTimeMillis() + MobileSdkUrl.EXPIRY_TIME_IN_MM;
                        MobileSdkUrl mobileSdkUrl = new MobileSdkUrl(response, expiryDate);
                        callback.onSuccess(mobileSdkUrl);
                    }
                };

                StringRequest request = new StringRequest(url, handler, callback);
                requestQueue.add(request);
            }
        };

        credentialsProvider.getCredentials(credentialsRequestCallback);
    }

    @Override
    public void fetchPaymentMethodConfigurations(final RequestCallback<List<PaymentMethodConfiguration>> callback) {

        AsynchronousCallback<Credentials> credentialsRequestCallback = new AsynchronousCallback<Credentials>() {
            @Override
            public void process(Credentials parameter) {
                String url = baseUrl + "transaction/fetchPossiblePaymentMethodsWithCredentials?credentials=" + parameter.getCredentials();
                Response.Listener<JSONArray> handler = new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Gson gson = new Gson();
                        Type listType = new TypeToken<List<PaymentMethodConfiguration>>() {
                        }.getType();
                        List<PaymentMethodConfiguration> paymentMethods = gson.fromJson(response.toString(), listType);
                        callback.onSuccess(paymentMethods);
                    }
                };

                JsonRequest request = new JsonArrayRequest(Request.Method.GET, url, null, handler, callback);
                requestQueue.add(request);
            }
        };

        credentialsProvider.getCredentials(credentialsRequestCallback);
    }

    @Override
    public void readTransaction(final RequestCallback<Transaction> callback) {

        AsynchronousCallback<Credentials> credentialsRequestCallback = new AsynchronousCallback<Credentials>() {
            @Override
            public void process(Credentials parameter) {
                String url = baseUrl + "transaction/readWithCredentials?credentials=" + parameter.getCredentials();
                Response.Listener<JSONObject> handler = new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Gson gson = new Gson();
                        Transaction transaction = gson.fromJson(response.toString(), Transaction.class);
                        callback.onSuccess(transaction);
                    }
                };

                JsonRequest request = new JsonObjectRequest(Request.Method.GET, url, null, handler, callback);
                requestQueue.add(request);
            }
        };

        credentialsProvider.getCredentials(credentialsRequestCallback);
    }

    @Override
    public void processOneClickToken(final Token token, final RequestCallback<Transaction> callback) {

        AsynchronousCallback<Credentials> credentialsRequestCallback = new AsynchronousCallback<Credentials>() {
            @Override
            public void process(Credentials parameter) {
                String url = baseUrl + "transaction/processOneClickTokenWithCredentials?credentials=" + parameter.getCredentials() + "&tokenId=" + token.getId();
                System.out.print(url);
                Response.Listener<JSONObject> handler = new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Gson gson = new Gson();
                        Transaction transaction = gson.fromJson(response.toString(), Transaction.class);
                        callback.onSuccess(transaction);
                    }
                };

                JsonRequest request = new JsonObjectRequest(Request.Method.POST, url, null, handler, callback);
                requestQueue.add(request);
            }
        };

        credentialsProvider.getCredentials(credentialsRequestCallback);
    }

}
