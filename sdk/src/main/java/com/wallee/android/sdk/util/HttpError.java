package com.wallee.android.sdk.util;

/**
 * This error is thrown when there is a HTTP error in loading a web page.
 */
public final class HttpError extends Exception{

    private final String url;
    private final String details;
    private final int httpStatusCode;

    public HttpError(String url, String details, int httpStatusCode) {
        super("The invocation of the URL " + url + " leads to a HTTP error " + httpStatusCode + ": " + details);
        this.url = Check.requireNonEmpty(url, "The url is required.");
        this.details = Check.requireNonEmpty(details, "The details is required.");
        this.httpStatusCode = httpStatusCode;
    }

    public String getUrl() {
        return url;
    }

    public String getDetails() {
        return this.details;
    }

    public int getHttpStatusCode() {
        return httpStatusCode;
    }


    @Override
    public String toString() {
        return "HttpError{" +
                "url='" + url + '\'' +
                ", details='" + details + '\'' +
                ", httpStatusCode=" + httpStatusCode +
                '}';
    }
}
