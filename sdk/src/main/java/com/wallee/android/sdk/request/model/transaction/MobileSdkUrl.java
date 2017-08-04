package com.wallee.android.sdk.request.model.transaction;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import com.wallee.android.sdk.util.Check;

import java.io.Serializable;

/**
 * This mobile SDK URL represents the base URL for loading a {@link android.webkit.WebView} with the
 * payment form in it.
 *
 * <p>The implementation is immutable.</p>
 */
public final class MobileSdkUrl implements Serializable, Parcelable {

    /**
     * URL expiration time.
     */
    public static final long EXPIRY_TIME_IN_MM = 15 * 60 * 1000;

    /**
     * The threshold which is applied to make sure the URL is valid when it is used.
     */
    public static final long EXPIRY_THRESHOLD = 2 * 60 * 1000;

    private final static String PAYMENT_METHOD_QUERY_PARAM_NAME = "paymentMethodConfigurationId";

    private final String url;
    private final long expiryDate;

    /**
     * Constructor.
     *
     * @param url        the base URL.
     * @param expiryDate the date on which the URL will expire.
     */
    public MobileSdkUrl(String url, long expiryDate) {
        this.url = Check.requireNonEmpty(url, "The url is required.");
        this.expiryDate = expiryDate;
    }

    protected MobileSdkUrl(Parcel in) {
        url = in.readString();
        expiryDate = in.readLong();
    }

    public static final Creator<MobileSdkUrl> CREATOR = new Creator<MobileSdkUrl>() {
        @Override
        public MobileSdkUrl createFromParcel(Parcel in) {
            return new MobileSdkUrl(in);
        }

        @Override
        public MobileSdkUrl[] newArray(int size) {
            return new MobileSdkUrl[size];
        }
    };

    public String getUrl() {
        return url;
    }

    public long getExpiryDate() {
        return expiryDate;
    }

    public boolean isExpired() {
        return System.currentTimeMillis() > expiryDate - EXPIRY_THRESHOLD;
    }

    /**
     * This method constructs a new URL which is usable to load the payment form for the provided
     * {@code paymentMethodConfigurationId}.
     *
     * @param paymentMethodConfigurationId the payment method configuration id for which the URL
     *                                     should be created for.
     * @return the newly created Uri which can be used to load the {@link android.webkit.WebView}.
     */
    public Uri buildPaymentMethodUrl(long paymentMethodConfigurationId) {
        if (System.currentTimeMillis() > this.expiryDate) {
            throw new IllegalStateException("The URL is expired. It cannot be used anymore to create a payment method specific URL.");
        }
        return Uri.parse(this.url)
                .buildUpon()
                .appendQueryParameter(PAYMENT_METHOD_QUERY_PARAM_NAME, Long.toString(paymentMethodConfigurationId))
                .build();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(url);
        dest.writeLong(expiryDate);
    }
}
