package com.wallee.android.sdk.request.model.method;

import android.os.Parcel;
import android.os.Parcelable;

import com.wallee.android.sdk.request.model.base.CreationEntityState;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by simonwalter on 26.07.17.
 */

public class ConnectorConfiguration implements Parcelable{
    private boolean applicableForTransactionProcessing;
    private List<Long> conditions;
    private long connector;
    private List<Long> enabledSpaceViews;
    private long id;
    private long linkedSpaceId;
    private String name;
    private PaymentMethodConfiguration paymentMethodConfiguration;
    private String plannedPurgeDate;
    private int priority;
    private ProcessorConfiguration processorConfiguration;
    private CreationEntityState state;
    private int version;

    protected ConnectorConfiguration(Parcel in) {
        applicableForTransactionProcessing = in.readByte() != 0;
        conditions = new ArrayList<>();
        in.readList(conditions, Long.class.getClassLoader());
        connector = in.readLong();
        enabledSpaceViews = new ArrayList<>();
        in.readList(enabledSpaceViews, Long.class.getClassLoader());
        id = in.readLong();
        linkedSpaceId = in.readLong();
        name = in.readString();
        paymentMethodConfiguration = in.readParcelable(PaymentMethodConfiguration.class.getClassLoader());
        plannedPurgeDate = in.readString();
        priority = in.readInt();
        processorConfiguration = in.readParcelable(ProcessorConfiguration.class.getClassLoader());
        state = in.readParcelable(CreationEntityState.class.getClassLoader());
        version = in.readInt();
    }

    public static final Creator<ConnectorConfiguration> CREATOR = new Creator<ConnectorConfiguration>() {
        @Override
        public ConnectorConfiguration createFromParcel(Parcel in) {
            return new ConnectorConfiguration(in);
        }

        @Override
        public ConnectorConfiguration[] newArray(int size) {
            return new ConnectorConfiguration[size];
        }
    };

    public boolean isApplicableForTransactionProcessing() {
        return applicableForTransactionProcessing;
    }

    public List<Long> getConditions() {
        return conditions;
    }

    public long getConnector() {
        return connector;
    }

    public List<Long> getEnabledSpaceViews() {
        return enabledSpaceViews;
    }

    public long getId() {
        return id;
    }

    public long getLinkedSpaceId() {
        return linkedSpaceId;
    }

    public String getName() {
        return name;
    }

    public PaymentMethodConfiguration getPaymentMethodConfiguration() {
        return paymentMethodConfiguration;
    }

    public String getPlannedPurgeDate() {
        return plannedPurgeDate;
    }

    public int getPriority() {
        return priority;
    }

    public ProcessorConfiguration getProcessorConfiguration() {
        return processorConfiguration;
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
        parcel.writeByte((byte) (applicableForTransactionProcessing ? 1 : 0));
        parcel.writeList(conditions);
        parcel.writeLong(connector);
        parcel.writeList(enabledSpaceViews);
        parcel.writeLong(id);
        parcel.writeLong(linkedSpaceId);
        parcel.writeString(name);
        parcel.writeParcelable(paymentMethodConfiguration, i);
        parcel.writeString(plannedPurgeDate);
        parcel.writeInt(priority);
        parcel.writeParcelable(processorConfiguration, i);
        parcel.writeParcelable(state, i);
        parcel.writeInt(version);
    }
}
