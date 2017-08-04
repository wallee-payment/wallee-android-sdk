package com.wallee.android.sdk.request.model.base;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by simonwalter on 27.07.17.
 */

public class ImageResourcePath implements Parcelable {
    private long id;
    private long linkedSpaceId;
    private String path;
    private String plannedPurgeDate;
    private long spaceId;
    private CreationEntityState state;
    private int version;

    protected ImageResourcePath(Parcel in) {
        id = in.readLong();
        linkedSpaceId = in.readLong();
        path = in.readString();
        plannedPurgeDate = in.readString();
        spaceId = in.readLong();
        state = in.readParcelable(CreationEntityState.class.getClassLoader());
        version = in.readInt();
    }

    public static final Creator<ImageResourcePath> CREATOR = new Creator<ImageResourcePath>() {
        @Override
        public ImageResourcePath createFromParcel(Parcel in) {
            return new ImageResourcePath(in);
        }

        @Override
        public ImageResourcePath[] newArray(int size) {
            return new ImageResourcePath[size];
        }
    };

    public long getId() {
        return id;
    }

    public long getLinkedSpaceId() {
        return linkedSpaceId;
    }

    public String getPath() {
        return path;
    }

    public String getPlannedPurgeDate() {
        return plannedPurgeDate;
    }

    public long getSpaceId() {
        return spaceId;
    }

    public CreationEntityState getState() {
        return state;
    }

    public int getVersion() {
        return version;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeLong(linkedSpaceId);
        parcel.writeString(path);
        parcel.writeString(plannedPurgeDate);
        parcel.writeLong(spaceId);
        parcel.writeParcelable(state, i);
        parcel.writeInt(version);
    }
}
