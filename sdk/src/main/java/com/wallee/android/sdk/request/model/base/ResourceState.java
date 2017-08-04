package com.wallee.android.sdk.request.model.base;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by simonwalter on 26.07.17.
 */

public enum ResourceState implements Parcelable {
    ACTIVE,
    DELETING,
    DELETED;

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ResourceState> CREATOR = new Creator<ResourceState>() {
        @Override
        public ResourceState createFromParcel(Parcel in) {
            return ResourceState.valueOf(in.readString());
        }

        @Override
        public ResourceState[] newArray(int size) {
            return new ResourceState[size];
        }
    };
}
