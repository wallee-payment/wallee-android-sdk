package com.wallee.android.sdk.request;


import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wallee.android.sdk.credentials.Credentials;
import com.wallee.android.sdk.credentials.CredentialsFetcher;
import com.wallee.android.sdk.credentials.CredentialsProvider;
import com.wallee.android.sdk.credentials.SimpleCredentialsStore;
import com.wallee.android.sdk.request.api.VolleyWebServiceApiClient;
import com.wallee.android.sdk.request.model.transaction.MobileSdkUrl;
import com.wallee.android.sdk.request.model.method.PaymentMethodConfiguration;
import com.wallee.android.sdk.request.model.token.Token;
import com.wallee.android.sdk.request.model.token.TokenVersion;
import com.wallee.android.sdk.request.model.transaction.Transaction;
import com.wallee.android.sdk.request.model.transaction.TransactionState;
import com.wallee.android.sdk.util.AsynchronousCallback;
import com.wallee.android.sdk.util.SingleThreadedRequestQueue;

import org.apache.commons.codec.binary.Base64;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * Tests {@link VolleyWebServiceApiClient}
 */
@Config(manifest=Config.NONE)
@RunWith(RobolectricTestRunner.class)
public class VolleyWebServiceApiClientTest {

    private Credentials credentials;
    private CredentialsProvider credentialsProvider;

    private final static long USER_ID = 526l;
    private final static String HMAC_KEY = "R1x818iST62GkGMgkm1zYKQ3N0Y7YiRRFdrycbs7KII=";
    private final static long SPACE_ID = 412;


    @Before
    public void onSetup() {
        this.credentials = this.createCredentials(USER_ID, HMAC_KEY, SPACE_ID);
        this.credentialsProvider = new CredentialsProvider(new SimpleCredentialsStore(), new CredentialsFetcher() {

            @Override
            public void fetchCredentials(AsynchronousCallback<Credentials> receiver) {
                receiver.process(credentials);
            }
        });
    }

    /**
     * Tests {@link VolleyWebServiceApiClient#buildMobileSdkUrl(RequestCallback)}
     */
    @Test
    public void testBuildMobileSdkUrl() {

        RequestQueue queue = new SingleThreadedRequestQueue();
        VolleyWebServiceApiClient manager = new VolleyWebServiceApiClient(queue, credentialsProvider, VolleyWebServiceApiClient.DEFAULT_BASE_URL);

        final AtomicReference<MobileSdkUrl> mobileSdkUrl = new AtomicReference<>();
        manager.buildMobileSdkUrl(new RequestCallback<MobileSdkUrl>() {
            @Override
            public void onSuccess(MobileSdkUrl object) {
                mobileSdkUrl.set(object);
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                throw new RuntimeException(error);
            }
        });

        if (mobileSdkUrl.get() == null) {
            Assert.fail("The mobile SDK URL has not been received.");
        }
        if (mobileSdkUrl.get().getUrl().isEmpty()) {
            Assert.fail("The mobile SDK URL is empty.");
        }
        Assert.assertTrue("The SDK URL does not start with the right prefix.", mobileSdkUrl.get().getUrl().startsWith("https://app-wallee.com/s/" + SPACE_ID + "/payment/transaction/mobile-sdk"));
    }

    /**
     * Tests {@link VolleyWebServiceApiClient#fetchTokenVersions(RequestCallback)}
     */
    @Test
    public void testFetchTokenVersions() {
        RequestQueue queue = new SingleThreadedRequestQueue();
        VolleyWebServiceApiClient manager = new VolleyWebServiceApiClient(queue, credentialsProvider, VolleyWebServiceApiClient.DEFAULT_BASE_URL);
        final AtomicReference<List<TokenVersion>> tokenVersions = new AtomicReference<>();
        manager.fetchTokenVersions(new RequestCallback<List<TokenVersion>>() {
            @Override
            public void onSuccess(List<TokenVersion> object) {
                tokenVersions.set(object);
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                throw new RuntimeException(error);
            }
        });

        if(tokenVersions.get() == null) {
            Assert.fail("The token versions could not be fetched.");
        }
    }

    /**
     * Tests {@link VolleyWebServiceApiClient#fetchPaymentMethodConfigurations(RequestCallback)}
     */
    @Test
    public void testFetchPaymentMethods() {
        RequestQueue queue = new SingleThreadedRequestQueue();
        VolleyWebServiceApiClient manager = new VolleyWebServiceApiClient(queue, credentialsProvider, VolleyWebServiceApiClient.DEFAULT_BASE_URL);
        final AtomicReference<List<PaymentMethodConfiguration>> paymentMethods = new AtomicReference<>();
        manager.fetchPaymentMethodConfigurations(new RequestCallback<List<PaymentMethodConfiguration>>() {
            @Override
            public void onSuccess(List<PaymentMethodConfiguration> object) {
                paymentMethods.set(object);
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                throw new RuntimeException(error);
            }
        });

        if(paymentMethods.get() == null) {
            Assert.fail("The payment methods could not be fetched.");
        }
        if(paymentMethods.get().isEmpty()) {
            Assert.fail("The payment methods list is empty.");
        }
    }

    /**
     * Tests {@link VolleyWebServiceApiClient#readTransaction(RequestCallback)}
     */
    @Test
    public void testReadTransaction() {
        RequestQueue queue = new SingleThreadedRequestQueue();
        VolleyWebServiceApiClient manager = new VolleyWebServiceApiClient(queue, credentialsProvider, VolleyWebServiceApiClient.DEFAULT_BASE_URL);
        final AtomicReference<Transaction> transaction = new AtomicReference<>();
        manager.readTransaction(new RequestCallback<Transaction>() {
            @Override
            public void onSuccess(Transaction object) {
                transaction.set(object);
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                throw new RuntimeException(error);
            }
        });

        if(transaction.get() == null) {
            Assert.fail("The transaction could not be read.");
        }
    }

    /**
     * Tests {@link VolleyWebServiceApiClient#processOneClickToken(Token, RequestCallback)}
     */
    @Test
    public void testProcessOneClickToken() {
        final AtomicReference<Transaction> transactionBefore = new AtomicReference<>();
        final AtomicReference<Transaction> transactionAfter = new AtomicReference<>();
        final AtomicReference<List<TokenVersion>> tokens = new AtomicReference<>();

        RequestQueue queue = new SingleThreadedRequestQueue();
        VolleyWebServiceApiClient manager = new VolleyWebServiceApiClient(queue, credentialsProvider, VolleyWebServiceApiClient.DEFAULT_BASE_URL);

        manager.readTransaction(new RequestCallback<Transaction>() {
            @Override
            public void onSuccess(Transaction object) {
                transactionBefore.set(object);
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                throw new RuntimeException(error);
            }
        });

        if(transactionBefore.get() == null) {
            Assert.fail("The transaction could not be read.");
        }
        if(transactionBefore.get().getState() != TransactionState.PENDING) {
            Assert.fail("The present transaction is not pending and therefore the OneClickToken cannot be used.");
        }

        manager.fetchTokenVersions(new RequestCallback<List<TokenVersion>>() {
            @Override
            public void onSuccess(List<TokenVersion> object) {
                tokens.set(object);
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                throw new RuntimeException(error);
            }
        });

        if (tokens.get() == null) {
            Assert.fail("Could not fetch token versions.");
        }
        if (tokens.get().isEmpty()) {
            Assert.fail("No tokens available for this transaction.");
        }

        RequestCallback<Transaction> transactionResultCallback = new RequestCallback<Transaction>() {
            @Override
            public void onSuccess(Transaction object) {
                transactionAfter.set(object);
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                throw new RuntimeException(error);
            }
        };

        manager.processOneClickToken(tokens.get().get(0).getToken(), transactionResultCallback);

        if (transactionAfter.get() == null) {
            Assert.fail("Could not process OneClickToken.");
        }

        Assert.assertNotEquals("Unexpected state of the transaction with ID: " + transactionAfter.get().getId(), TransactionState.FAILED, transactionAfter.get().getState());
        Assert.assertNotEquals("Unexpected state of the transaction with ID: " + transactionAfter.get().getId(), TransactionState.PENDING, transactionAfter.get().getState());
        Assert.assertNotEquals("Unexpected state of the transaction with ID: " + transactionAfter.get().getId(), TransactionState.PROCESSING, transactionAfter.get().getState());
        Assert.assertNotEquals("Unexpected state of the transaction with ID: " + transactionAfter.get().getId(), TransactionState.CONFIRMED, transactionAfter.get().getState());
    }


    private Credentials createCredentials(long userId, String macKey, long spaceId) {
        String transactionObject = sendRequest("POST", "https://app-wallee.com/api/transaction/create?spaceId=" + spaceId, "application/json", userId, macKey);
        Gson gson = new Gson();
        Type type = new TypeToken<Map<String, Object>>() {
        }.getType();
        Map<String, Object> myMap = gson.fromJson(transactionObject, type);

        long transactionId = ((Double) myMap.get("id")).longValue();
        String credentials = sendRequest("POST", "https://app-wallee.com/api/transaction/createTransactionCredentials?spaceId=" + spaceId + "&id=" + transactionId, "application/json", userId, macKey);
        return new Credentials(credentials);
    }

    private String sendRequest(String method, String url, String contentType, long userId, String hmacKey) {
        try {
            HttpURLConnection connection = this.createConnection(method, new URL(url), contentType, userId, hmacKey);
            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            writer.write(this.transactionCreationString());
            writer.close();

            if (Integer.toString(connection.getResponseCode()).startsWith("2")) {
                BufferedReader r = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder transactionJson = new StringBuilder();
                String line;
                while ((line = r.readLine()) != null) {
                    transactionJson.append(line).append('\n');
                }
                return transactionJson.toString();
            } else {
                BufferedReader r = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                StringBuilder error = new StringBuilder();
                String line;
                while ((line = r.readLine()) != null) {
                    error.append(line).append('\n');
                }
                throw new RuntimeException(error.toString());
            }
        } catch (IOException e) {
            throw new RuntimeException("Unable to execute call.", e);
        }
    }

    private String transactionCreationString() {
        return "{\n" +
                "  \"currency\" : \"EUR\",\n" +
                "  \"customerId\": \"test-customer\", \n" +
                "  \"lineItems\" : [ {\n" +
                "    \"amountIncludingTax\" : \"11.87\",\n" +
                "    \"name\" : \"Barbell Pull Up Bar\",\n" +
                "    \"quantity\" : \"1\",\n" +
                "    \"shippingRequired\" : \"true\",\n" +
                "    \"sku\" : \"barbell-pullup\",\n" +
                "    \"type\" : \"PRODUCT\",\n" +
                "    \"uniqueId\" : \"barbell-pullup\"\n" +
                "  } ],\n" +
                "  \"merchantReference\" : \"DEV-2630\"\n" +
                "}";
    }

    private HttpURLConnection createConnection(String method, URL url, String contentType, long userId, String macKey) {
        try {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            long timestamp = (long) (System.currentTimeMillis() / 1000l);
            connection.setRequestProperty("x-mac-version", "1");
            connection.setRequestProperty("x-mac-userid", Long.toString(USER_ID));
            connection.setRequestProperty("x-mac-timestamp", Long.toString(timestamp));


            String path = url.getPath();
            if (url.getQuery() != null) {
                path = path + "?" + url.getQuery();
            }

            String securedData = "1|" + userId + "|" + timestamp + "|" + method + "|" + path;

            byte[] decodedSecret = Base64.decodeBase64(macKey.getBytes("UTF-8"));

            Mac mac = Mac.getInstance("HmacSHA512");
            mac.init(new SecretKeySpec(decodedSecret, "HmacSHA512"));

            byte[] bytes = mac.doFinal(securedData.getBytes("UTF-8"));

            String macValue = new String(Base64.encodeBase64(bytes), "UTF-8");
            connection.setRequestProperty("x-mac-value", macValue);
            connection.setRequestMethod(method);
            connection.setRequestProperty("Content-Type", contentType);
            if (method.equalsIgnoreCase("POST")) {
                connection.setDoOutput(true);
            }

            return connection;
        } catch (IOException | NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException("Unable to execute call.", e);
        }
    }

}
