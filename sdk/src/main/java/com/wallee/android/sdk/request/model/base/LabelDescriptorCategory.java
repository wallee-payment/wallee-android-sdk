package com.wallee.android.sdk.request.model.base;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by simonwalter on 26.07.17.
 */

public enum LabelDescriptorCategory implements Parcelable {
    HUMAN,
    APPLICATION;

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<LabelDescriptorCategory> CREATOR = new Creator<LabelDescriptorCategory>() {
        @Override
        public LabelDescriptorCategory createFromParcel(Parcel in) {
            return LabelDescriptorCategory.valueOf(in.readString());
        }

        @Override
        public LabelDescriptorCategory[] newArray(int size) {
            return new LabelDescriptorCategory[size];
        }
    };
}
