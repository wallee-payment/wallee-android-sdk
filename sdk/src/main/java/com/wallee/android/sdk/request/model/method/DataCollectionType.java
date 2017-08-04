package com.wallee.android.sdk.request.model.method;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by simonwalter on 26.07.17.
 */

public enum DataCollectionType implements Parcelable{
    ONSITE,
    OFFSITE;

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<DataCollectionType> CREATOR = new Creator<DataCollectionType>() {
        @Override
        public DataCollectionType createFromParcel(Parcel in) {
            return DataCollectionType.valueOf(in.readString());
        }

        @Override
        public DataCollectionType[] newArray(int size) {
            return new DataCollectionType[size];
        }
    };
}
