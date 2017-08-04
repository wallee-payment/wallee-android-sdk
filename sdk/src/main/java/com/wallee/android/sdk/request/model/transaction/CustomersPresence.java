package com.wallee.android.sdk.request.model.transaction;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by simonwalter on 28.07.17.
 */

public enum CustomersPresence implements Parcelable {
    NOT_PRESENT,
    VIRTUAL_PRESENT,
    PHYSICAL_PRESENT;

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CustomersPresence> CREATOR = new Creator<CustomersPresence>() {
        @Override
        public CustomersPresence createFromParcel(Parcel in) {
            return CustomersPresence.valueOf(in.readString());
        }

        @Override
        public CustomersPresence[] newArray(int size) {
            return new CustomersPresence[size];
        }
    };
}
