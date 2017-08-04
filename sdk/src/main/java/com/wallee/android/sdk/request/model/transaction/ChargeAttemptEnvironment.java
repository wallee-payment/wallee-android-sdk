package com.wallee.android.sdk.request.model.transaction;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by simonwalter on 26.07.17.
 */

public enum ChargeAttemptEnvironment implements Parcelable {
    PRODUCTION,
    TEST;

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ChargeAttemptEnvironment> CREATOR = new Creator<ChargeAttemptEnvironment>() {
        @Override
        public ChargeAttemptEnvironment createFromParcel(Parcel in) {
            return ChargeAttemptEnvironment.valueOf(in.readString());
        }

        @Override
        public ChargeAttemptEnvironment[] newArray(int size) {
            return new ChargeAttemptEnvironment[size];
        }
    };
}
