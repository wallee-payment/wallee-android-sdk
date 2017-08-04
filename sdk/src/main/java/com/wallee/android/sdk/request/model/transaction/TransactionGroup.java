package com.wallee.android.sdk.request.model.transaction;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by simonwalter on 28.07.17.
 */

public class TransactionGroup implements Parcelable{
    private String beginDate;
    private String customerId;
    private String endDate;
    private long id;
    private long linkedSpaceId;
    private String plannedPurgeDate;
    private TransactionGroupState state;
    private int version;

    protected TransactionGroup(Parcel in) {
        beginDate = in.readString();
        customerId = in.readString();
        endDate = in.readString();
        id = in.readLong();
        linkedSpaceId = in.readLong();
        plannedPurgeDate = in.readString();
        state = in.readParcelable(TransactionGroupState.class.getClassLoader());
        version = in.readInt();
    }

    public static final Creator<TransactionGroup> CREATOR = new Creator<TransactionGroup>() {
        @Override
        public TransactionGroup createFromParcel(Parcel in) {
            return new TransactionGroup(in);
        }

        @Override
        public TransactionGroup[] newArray(int size) {
            return new TransactionGroup[size];
        }
    };

    public String getBeginDate() {
        return beginDate;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getEndDate() {
        return endDate;
    }

    public long getId() {
        return id;
    }

    public long getLinkedSpaceId() {
        return linkedSpaceId;
    }

    public String getPlannedPurgeDate() {
        return plannedPurgeDate;
    }

    public TransactionGroupState getState() {
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
        parcel.writeString(beginDate);
        parcel.writeString(customerId);
        parcel.writeString(endDate);
        parcel.writeLong(id);
        parcel.writeLong(linkedSpaceId);
        parcel.writeString(plannedPurgeDate);
        parcel.writeParcelable(state, i);
        parcel.writeInt(version);
    }
}
