package com.wallee.android.sdk.request.model.method;

import android.os.Parcel;
import android.os.Parcelable;

import com.wallee.android.sdk.util.ParcelableHelper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by simonwalter on 28.07.17.
 */

public class PaymentMethodBrand implements Parcelable {
    private Map<String, String> description;
    private String grayImagePath;
    private long id;
    private String imagePath;
    private Map<String, String> name;
    private long paymentMethod;


    protected PaymentMethodBrand(Parcel in) {
        description = new HashMap<>();
        ParcelableHelper.readSerializableMap(in, description, String.class, String.class);
        grayImagePath = in.readString();
        id = in.readLong();
        imagePath = in.readString();
        name = new HashMap<>();
        ParcelableHelper.readSerializableMap(in, name, String.class, String.class);
        paymentMethod = in.readLong();
    }

    public static final Creator<PaymentMethodBrand> CREATOR = new Creator<PaymentMethodBrand>() {
        @Override
        public PaymentMethodBrand createFromParcel(Parcel in) {
            return new PaymentMethodBrand(in);
        }

        @Override
        public PaymentMethodBrand[] newArray(int size) {
            return new PaymentMethodBrand[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    public Map<String, String> getDescription() {
        return description;
    }

    public String getGrayImagePath() {
        return grayImagePath;
    }

    public String getImagePath() {
        return imagePath;
    }

    public long getPaymentMethod() {
        return paymentMethod;
    }

    public long getId() {
        return id;
    }

    public Map<String, String> getName() {
        return name;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        ParcelableHelper.writeSerializableMap(parcel, description);
        parcel.writeString(grayImagePath);
        parcel.writeLong(id);
        parcel.writeString(imagePath);
        ParcelableHelper.writeSerializableMap(parcel, name);
        parcel.writeLong(paymentMethod);
    }
}
