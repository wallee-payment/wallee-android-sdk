package com.wallee.android.sdk.request.model.base;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by simonwalter on 26.07.17.
 */

public class LegalOrganizationForm implements Parcelable{
    private String country;
    private List<LocalizedString> description;
    private String englishDescription;
    private long id;
    private List<LocalizedString> shortcut;

    protected LegalOrganizationForm(Parcel in) {
        country = in.readString();
        description = new ArrayList<>();
        in.readTypedList(description, LocalizedString.CREATOR);
        englishDescription = in.readString();
        id = in.readLong();
        shortcut = new ArrayList<>();
        in.readTypedList(shortcut, LocalizedString.CREATOR);
    }

    public static final Creator<LegalOrganizationForm> CREATOR = new Creator<LegalOrganizationForm>() {
        @Override
        public LegalOrganizationForm createFromParcel(Parcel in) {
            return new LegalOrganizationForm(in);
        }

        @Override
        public LegalOrganizationForm[] newArray(int size) {
            return new LegalOrganizationForm[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(country);
        parcel.writeTypedList(description);
        parcel.writeString(englishDescription);
        parcel.writeLong(id);
        parcel.writeTypedList(shortcut);
    }
}
