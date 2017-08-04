package com.wallee.android.sdk.request.model.base;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by simonwalter on 26.07.17.
 */

public class DatabaseTranslatedStringItem implements Parcelable{
    private String language;
    private String languageCode;
    private String translation;

    protected DatabaseTranslatedStringItem(Parcel in) {
        language = in.readString();
        languageCode = in.readString();
        translation = in.readString();
    }

    public static final Creator<DatabaseTranslatedStringItem> CREATOR = new Creator<DatabaseTranslatedStringItem>() {
        @Override
        public DatabaseTranslatedStringItem createFromParcel(Parcel in) {
            return new DatabaseTranslatedStringItem(in);
        }

        @Override
        public DatabaseTranslatedStringItem[] newArray(int size) {
            return new DatabaseTranslatedStringItem[size];
        }
    };

    public String getLanguage() {
        return language;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public String getTranslation() {
        return translation;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(language);
        parcel.writeString(languageCode);
        parcel.writeString(translation);
    }
}
