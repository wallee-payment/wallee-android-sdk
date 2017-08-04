package com.wallee.android.sdk.request.model.method;

import android.os.Parcel;
import android.os.Parcelable;

import com.wallee.android.sdk.request.model.base.LocalizedString;

import java.util.List;

/**
 * Created by simonwalter on 28.07.17.
 */

public class PaymentMethodBrand implements Parcelable {
    private List<LocalizedString> description;
    private String grayImagePath;
    private long id;
    private String imagePath;
    private List<LocalizedString> name;
    private long paymentMethod;


    protected PaymentMethodBrand(Parcel in) {
        description = in.createTypedArrayList(LocalizedString.CREATOR);
        grayImagePath = in.readString();
        id = in.readLong();
        imagePath = in.readString();
        name = in.createTypedArrayList(LocalizedString.CREATOR);
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

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeTypedList(description);
        parcel.writeString(grayImagePath);
        parcel.writeLong(id);
        parcel.writeString(imagePath);
        parcel.writeTypedList(name);
        parcel.writeLong(paymentMethod);
    }
}
