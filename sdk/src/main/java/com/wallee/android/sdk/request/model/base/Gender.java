package com.wallee.android.sdk.request.model.base;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by simonwalter on 26.07.17.
 */

public enum  Gender implements Parcelable{
    MALE,
    FEMALE;

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Gender> CREATOR = new Creator<Gender>() {
        @Override
        public Gender createFromParcel(Parcel in) {
            return Gender.valueOf(in.readString());
        }

        @Override
        public Gender[] newArray(int size) {
            return new Gender[size];
        }
    };
}
