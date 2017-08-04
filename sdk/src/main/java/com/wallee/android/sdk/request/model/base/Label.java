package com.wallee.android.sdk.request.model.base;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by simonwalter on 26.07.17.
 */

public class Label implements Parcelable {
    private String contentAsString;
    private LabelDescriptor descriptor;
    private long id;
    private int version;

    protected Label(Parcel in) {
        contentAsString = in.readString();
        id = in.readLong();
        version = in.readInt();
    }

    public static final Creator<Label> CREATOR = new Creator<Label>() {
        @Override
        public Label createFromParcel(Parcel in) {
            return new Label(in);
        }

        @Override
        public Label[] newArray(int size) {
            return new Label[size];
        }
    };

    public String getContentAsString() {
        return contentAsString;
    }

    public long getId() {
        return id;
    }

    public int getVersion() {
        return version;
    }

    public LabelDescriptor getDescriptor() {
        return descriptor;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(contentAsString);
        parcel.writeLong(id);
        parcel.writeInt(version);
    }
}
