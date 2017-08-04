package com.wallee.android.sdk.request.model.method;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * The payment method icon represents the data associated with an icon of a payment method.
 *
 * <p>We fetch the icon data from the remote server. To display and to cache the data we need a data
 * container. This class is able to store all the relevant image data.</p>
 *
 * <p>We store the data as base64 since we are going to use it as base64 string and to avoid
 * multiple times encoding the data we directly store it as base64.</p>
 */
final public class PaymentMethodIcon implements Serializable, Parcelable {
    private final String mimeType;
    private final String base64Data;
    private final String url;

    /**
     * The default constructor.
     *
     * @param url        the URL from where the image has been downloaded from. We use it to
     *                   identify the image. It acts as the icon's identifier.
     * @param base64Data the binary image data as base64 encoded string.
     * @param mimeType   the mime type indicates what kind of image the icon is. Example: image/png,
     *                   image/svg+xml
     */
    public PaymentMethodIcon(String url, String base64Data, String mimeType) {
        this.base64Data = base64Data;
        this.url = url;
        this.mimeType = mimeType;
    }

    protected PaymentMethodIcon(Parcel in) {
        mimeType = in.readString();
        base64Data = in.readString();
        url = in.readString();
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mimeType);
        parcel.writeString(base64Data);
        parcel.writeString(url);
    }
    public static final Creator<PaymentMethodIcon> CREATOR = new Creator<PaymentMethodIcon>() {
        @Override
        public PaymentMethodIcon createFromParcel(Parcel in) {
            return new PaymentMethodIcon(in);
        }

        @Override
        public PaymentMethodIcon[] newArray(int size) {
            return new PaymentMethodIcon[size];
        }
    };

    /**
     * @return the mime type of the icon. Example: image/png, image/svg+xml
     */
    public String getMimeType() {
        return mimeType;
    }

    /**
     * @return the binary data of the icon encoded as base64.
     */
    public String getBase64Data() {
        return base64Data;
    }

    /**
     * We want to cache the icon. Therefore we need an identifier which is enables us to uniquely
     * identify the icon.
     *
     * @return the URL of the icon. The URL acts as the identifier of the icon.
     */
    public String getUrl() {
        return url;
    }

    @Override
    public int describeContents() {
        return 0;
    }

}
