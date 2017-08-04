package com.wallee.android.sdk.request.model.transaction;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by simonwalter on 28.07.17.
 */

public enum  TransactionGroupState implements Parcelable {
    PENDING,
    FAILED,
    SUCCESSFUL;

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<TransactionGroupState> CREATOR = new Creator<TransactionGroupState>() {
        @Override
        public TransactionGroupState createFromParcel(Parcel in) {
            return TransactionGroupState.valueOf(in.readString());
        }

        @Override
        public TransactionGroupState[] newArray(int size) {
            return new TransactionGroupState[size];
        }
    };
}
