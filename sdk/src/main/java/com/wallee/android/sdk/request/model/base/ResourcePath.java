package com.wallee.android.sdk.request.model.base;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by simonwalter on 26.07.17.
 */

public class ResourcePath implements Parcelable {
    private long id;
    private long linkedSpaceId;
    private String path;
    private String plannedPurgeDate;
    private long spaceId;
    private ResourceState state;
    private int version;

    protected ResourcePath(Parcel in) {
        id = in.readLong();
        linkedSpaceId = in.readLong();
        path = in.readString();
        plannedPurgeDate = in.readString();
        spaceId = in.readLong();
        state = in.readParcelable(ResourceState.class.getClassLoader());
        version = in.readInt();
    }

    public static final Creator<ResourcePath> CREATOR = new Creator<ResourcePath>() {
        @Override
        public ResourcePath createFromParcel(Parcel in) {
            return new ResourcePath(in);
        }

        @Override
        public ResourcePath[] newArray(int size) {
            return new ResourcePath[size];
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

    public ResourceState getState() {
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
