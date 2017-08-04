package com.wallee.android.sample;

import android.content.Context;
import android.provider.Settings;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wallee.android.sample.request.HTTPRequestParameters;
import com.wallee.android.sample.request.HTTPRequester;
import com.wallee.android.sample.request.HTTPResponseListener;
import com.wallee.android.sdk.credentials.Credentials;
import com.wallee.android.sdk.credentials.CredentialsFetcher;
import com.wallee.android.sdk.util.AsynchronousCallback;

import java.lang.reflect.Type;
import java.util.Map;


/**
 * This is a implementation of {@link CredentialsFetcher} which fetches the credentials directly
 * from the server. In a production environment such a implementation is not secure and should be
 * avoided. In a production environment the fetching of the credentials should be provided by the
 * merchant backend system.
 */
public final class TestCredentialsFetcher implements CredentialsFetcher {

    private final String baseUrl;
    private final long userId;
    private final String hmacKey;
    private final long spaceId;
    private final String customerId;

    public TestCredentialsFetcher(Context context, String baseUrl, long userId, String hmacKey, long spaceId) {
        this.baseUrl = baseUrl;
        this.userId = userId;
        this.hmacKey = hmacKey;
        this.spaceId = spaceId;
        this.customerId = Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        ;
    }

    @Override
    public void fetchCredentials(AsynchronousCallback<Credentials> receiver) {
        new Handler(customerId, baseUrl, userId, hmacKey, spaceId, receiver).createTransaction();
    }

    private static class Handler {

        private final AsynchronousCallback<Credentials> receiver;
        private final String baseUrl;
        private final long userId;
        private final String hmacKey;
        private final long spaceId;
        private final String customerId;

        private Handler(String customerId, String baseUrl, long userId, String hmacKey, long spaceId, AsynchronousCallback<Credentials> receiver) {
            this.customerId = customerId;
            this.receiver = receiver;
            this.baseUrl = baseUrl;
            this.userId = userId;
            this.hmacKey = hmacKey;
            this.spaceId = spaceId;
        }

        private void createTransaction() {
            HTTPRequestParameters params = new HTTPRequestParameters("POST",
                    baseUrl + "transaction/create?spaceId=" + spaceId, "application/json",
                    userId,
                    hmacKey,
                    new TransactionResponse());
            HTTPRequester request = new HTTPRequester(customerId);
            request.execute(params);
        }


        private class TransactionResponse implements HTTPResponseListener<String> {

            @Override
            public void onResponse(String transactionAsJson) {
                Gson gson = new Gson();
                Type type = new TypeToken<Map<String, Object>>() {
                }.getType();
                Map<String, Object> myMap = gson.fromJson(transactionAsJson, type);
                long transactionId = ((Double) myMap.get("id")).longValue();
                HTTPRequestParameters params = new HTTPRequestParameters("POST",
                        baseUrl + "/transaction/createTransactionCredentials?spaceId=" + spaceId + "&id=" + transactionId,
                        "application/json",
                        userId, hmacKey,
                        new CredentialsResponse());
                HTTPRequester request = new HTTPRequester(customerId);
                request.execute(params);
            }
        }

        private class CredentialsResponse implements HTTPResponseListener<String> {

            @Override
            public void onResponse(String response) {
                receiver.process(new Credentials((response)));
            }
        }
    }


}
