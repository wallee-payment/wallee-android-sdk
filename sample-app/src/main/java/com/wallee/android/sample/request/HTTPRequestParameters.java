package com.wallee.android.sample.request;

/**
 * Created by simonwalter on 27.07.17.
 */

public class HTTPRequestParameters {
    public String method;
    public String url;
    public String contentType;
    public long userId;
    public String hmacKey;
    public HTTPResponseListener listener;

    public HTTPRequestParameters(String method, String url, String contentType, long userId, String hmacKey, HTTPResponseListener listener) {
        this.method = method;
        this.url = url;
        this.contentType = contentType;
        this.userId = userId;
        this.hmacKey = hmacKey;
        this.listener = listener;
    }
}
