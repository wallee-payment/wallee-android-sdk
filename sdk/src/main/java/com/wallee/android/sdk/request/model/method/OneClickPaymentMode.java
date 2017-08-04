package com.wallee.android.sdk.request.model.method;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by simonwalter on 26.07.17.
 */

public enum OneClickPaymentMode implements Parcelable {
    DISABLED,
    ALLOW,
    FORCE;

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<OneClickPaymentMode> CREATOR = new Creator<OneClickPaymentMode>() {
        @Override
        public OneClickPaymentMode createFromParcel(Parcel in) {
            return OneClickPaymentMode.valueOf(in.readString());
        }

        @Override
        public OneClickPaymentMode[] newArray(int size) {
            return new OneClickPaymentMode[size];
        }
    };
}
