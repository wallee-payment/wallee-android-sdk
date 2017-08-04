package com.wallee.android.sdk.request.model.transaction;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * The possible state of a Transaction as defined in the web API.
 */

public enum TransactionState implements Parcelable{
    CREATE,
    PENDING,
    CONFIRMED,
    PROCESSING,
    FAILED,
    AUTHORIZED,
    COMPLETED,
    FULFILL,
    DECLINE,
    VOIDED;

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<TransactionState> CREATOR = new Creator<TransactionState>() {
        @Override
        public TransactionState createFromParcel(Parcel in) {
            return TransactionState.valueOf(in.readString());
        }

        @Override
        public TransactionState[] newArray(int size) {
            return new TransactionState[size];
        }
    };
}
