package com.wallee.android.sdk.request.model.method;

import android.os.Parcel;
import android.os.Parcelable;

import com.wallee.android.sdk.request.model.base.CreationEntityState;

/**
 * Created by simonwalter on 26.07.17.
 */

public class ProcessorConfiguration implements Parcelable {
    private long id;
    private long linkedSpaceId;
    private String name;
    private String plannedPurgeDate;
    private long processor;
    private CreationEntityState state;
    private int version;

    public long getId() {
        return id;
    }

    public long getLinkedSpaceId() {
        return linkedSpaceId;
    }

    public String getName() {
        return name;
    }

    public String getPlannedPurgeDate() {
        return plannedPurgeDate;
    }

    public long getProcessor() {
        return processor;
    }

    public CreationEntityState getState() {
        return state;
    }

    public int getVersion() {
        return version;
    }

    protected ProcessorConfiguration(Parcel in) {
        id = in.readLong();
        linkedSpaceId = in.readLong();
        name = in.readString();
        plannedPurgeDate = in.readString();
        processor = in.readLong();
        state = in.readParcelable(CreationEntityState.class.getClassLoader());
        version = in.readInt();
    }

    public static final Creator<ProcessorConfiguration> CREATOR = new Creator<ProcessorConfiguration>() {
        @Override
        public ProcessorConfiguration createFromParcel(Parcel in) {
            return new ProcessorConfiguration(in);
        }

        @Override
        public ProcessorConfiguration[] newArray(int size) {
            return new ProcessorConfiguration[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeLong(linkedSpaceId);
        parcel.writeString(name);
        parcel.writeString(plannedPurgeDate);
        parcel.writeLong(processor);
        parcel.writeParcelable(state, i);
        parcel.writeInt(version);
    }
}
