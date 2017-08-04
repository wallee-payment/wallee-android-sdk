package com.wallee.android.sdk.request.model.token;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by simonwalter on 26.07.17.
 */

public enum TokenVersionState implements Parcelable {
    UNINITIALIZED,
    ACTIVE,
    OBSOLETE;

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<TokenVersionState> CREATOR = new Creator<TokenVersionState>() {
        @Override
        public TokenVersionState createFromParcel(Parcel in) {
            return TokenVersionState.valueOf(in.readString());
        }

        @Override
        public TokenVersionState[] newArray(int size) {
            return new TokenVersionState[size];
        }
    };
}
