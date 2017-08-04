package com.wallee.android.sdk.request.model.transaction;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by simonwalter on 25.07.17.
 */

public enum  LineItemType implements Parcelable {
    SHIPPING,
    DISCOUNT,
    FEE,
    PRODUCT;

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<LineItemType> CREATOR = new Creator<LineItemType>() {
        @Override
        public LineItemType createFromParcel(Parcel in) {
            return LineItemType.valueOf(in.readString());
        }

        @Override
        public LineItemType[] newArray(int size) {
            return new LineItemType[size];
        }
    };
}
