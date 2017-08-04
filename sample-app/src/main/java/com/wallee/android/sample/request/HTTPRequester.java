package com.wallee.android.sample.request;

import android.os.AsyncTask;
import android.util.Base64;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by simonwalter on 27.07.17.
 */

public class HTTPRequester extends AsyncTask<HTTPRequestParameters, Void, String> {
    private HTTPResponseListener<String> listener;

    private final String customerId;

    public HTTPRequester(String customerId) {
        this.customerId = customerId;
    }

    @Override
    protected String doInBackground(HTTPRequestParameters... requestParameters) {
        HTTPRequestParameters params = requestParameters[0];
        this.listener = params.listener;
        return sendRequest(params.method, params.url, params.contentType, params.userId, params.hmacKey);
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        listener.onResponse(s);
    }

    private String transactionCreationString() {
        return "{\n" +
                "  \"currency\" : \"EUR\",\n" +
                "  \"customerId\": \"" + customerId + "\", \n" +
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
            connection.setRequestProperty("x-mac-userid", Long.toString(userId));
            connection.setRequestProperty("x-mac-timestamp", Long.toString(timestamp));


            String path = url.getPath();
            if (url.getQuery() != null) {
                path = path + "?" + url.getQuery();
            }

            String securedData = "1|" + userId + "|" + timestamp + "|" + method + "|" + path;

            byte[] decodedSecret = Base64.decode(macKey.getBytes(), Base64.NO_WRAP);

            Mac mac = Mac.getInstance("HmacSHA512");
            mac.init(new SecretKeySpec(decodedSecret, "HmacSHA512"));

            byte[] bytes = mac.doFinal(securedData.getBytes("UTF-8"));

            String macValue = new String(Base64.encode(bytes, Base64.NO_WRAP), Charset.forName("UTF-8"));
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

    private String sendRequest(String method, String url, String contentType, long userId, String hmacKey) {
        try {
            HttpURLConnection connection = createConnection(method, new URL(url), contentType, userId, hmacKey);
            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            writer.write(transactionCreationString());
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
}
