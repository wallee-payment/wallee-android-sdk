package com.wallee.android.sdk.request.model.base;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by simonwalter on 26.07.17.
 */

public class LocalizedString implements Parcelable {
    private String displayName;
    private String language;
    private String string;

    protected LocalizedString(Parcel in) {
        displayName = in.readString();
        language = in.readString();
        string = in.readString();
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getLanguage() {
        return language;
    }

    public String getString() {
        return string;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(displayName);
        parcel.writeString(language);
        parcel.writeString(string);
    }

    public static final Creator<LocalizedString> CREATOR = new Creator<LocalizedString>() {
        @Override
        public LocalizedString createFromParcel(Parcel in) {
            return new LocalizedString(in);
        }

        @Override
        public LocalizedString[] newArray(int size) {
            return new LocalizedString[size];
        }
    };
}
