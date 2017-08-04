package com.wallee.android.sdk.request.model.base;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by simonwalter on 26.07.17.
 */

public class DatabaseTranslatedString implements Parcelable {
    private List<String> availableLanguages;
    private String displayName;
    private List<DatabaseTranslatedStringItem> items;

    protected DatabaseTranslatedString(Parcel in) {
        availableLanguages = in.createStringArrayList();
        displayName = in.readString();
        items = in.createTypedArrayList(DatabaseTranslatedStringItem.CREATOR);
    }

    public static final Creator<DatabaseTranslatedString> CREATOR = new Creator<DatabaseTranslatedString>() {
        @Override
        public DatabaseTranslatedString createFromParcel(Parcel in) {
            return new DatabaseTranslatedString(in);
        }

        @Override
        public DatabaseTranslatedString[] newArray(int size) {
            return new DatabaseTranslatedString[size];
        }
    };

    public List<String> getAvailableLanguages() {
        return availableLanguages;
    }

    public String getDisplayName() {
        return displayName;
    }

    public List<DatabaseTranslatedStringItem> getItems() {
        return items;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeStringList(availableLanguages);
        parcel.writeString(displayName);
        parcel.writeTypedList(items);
    }
}
