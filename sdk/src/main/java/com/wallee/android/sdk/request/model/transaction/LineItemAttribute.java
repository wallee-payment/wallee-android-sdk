package com.wallee.android.sdk.request.model.transaction;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by simonwalter on 25.07.17.
 */

public class LineItemAttribute implements Parcelable, Serializable {
    private String label;
    private String value;

    protected LineItemAttribute(Parcel in) {
        label = in.readString();
        value = in.readString();
    }

    public static final Creator<LineItemAttribute> CREATOR = new Creator<LineItemAttribute>() {
        @Override
        public LineItemAttribute createFromParcel(Parcel in) {
            return new LineItemAttribute(in);
        }

        @Override
        public LineItemAttribute[] newArray(int size) {
            return new LineItemAttribute[size];
        }
    };

    public String getLabel() {
        return label;
    }

    public String getValue() {
        return value;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(label);
        parcel.writeString(value);
    }
}
