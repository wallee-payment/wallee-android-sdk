package com.wallee.android.sdk.request.model.base;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by simonwalter on 26.07.17.
 */

public enum CreationEntityState implements Parcelable {
    CREATE,
    ACTIVE,
    INACTIVE,
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

    public static final Creator<CreationEntityState> CREATOR = new Creator<CreationEntityState>() {
        @Override
        public CreationEntityState createFromParcel(Parcel in) {
            return CreationEntityState.valueOf(in.readString());
        }

        @Override
        public CreationEntityState[] newArray(int size) {
            return new CreationEntityState[size];
        }
    };
}
