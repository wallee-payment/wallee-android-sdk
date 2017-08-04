package com.wallee.android.sdk.request.model.transaction;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by simonwalter on 28.07.17.
 */

public enum TransactionUserInterfaceType implements Parcelable {
    IFRAME,
    PAYMENT_PAGE,
    MOBILE_SDK;

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<TransactionUserInterfaceType> CREATOR = new Creator<TransactionUserInterfaceType>() {
        @Override
        public TransactionUserInterfaceType createFromParcel(Parcel in) {
            return TransactionUserInterfaceType.valueOf(in.readString());
        }

        @Override
        public TransactionUserInterfaceType[] newArray(int size) {
            return new TransactionUserInterfaceType[size];
        }
    };
}
