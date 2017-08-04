package com.wallee.android.sdk.request.model.token;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.wallee.android.sdk.request.model.transaction.Address;
import com.wallee.android.sdk.request.model.transaction.ChargeAttemptEnvironment;
import com.wallee.android.sdk.request.model.method.ConnectorConfiguration;
import com.wallee.android.sdk.request.model.base.Label;

import java.io.Serializable;
import java.util.List;

/**
 * Token Version class as defined by the web API.
 */

public class TokenVersion implements Serializable, Comparable<TokenVersion>, Parcelable {
    private String activatedOn;
    private Address billingAddress;
    private String createdOn;
    private ChargeAttemptEnvironment environment;
    private long id;
    private List<Label> labels;
    private String language;
    private long linkedSpaceId;
    private String name;
    private String obsoletedOn;
    private ConnectorConfiguration paymentConnectorConfiguration;
    private String plannedPurgeDate;
    private String processorToken;
    private Address shippingAddress;
    private TokenVersionState state;
    private int version;
    private Token token;

    protected TokenVersion(Parcel in) {
        activatedOn = in.readString();
        billingAddress = in.readParcelable(Address.class.getClassLoader());
        createdOn = in.readString();
        environment = in.readParcelable(ChargeAttemptEnvironment.class.getClassLoader());
        id = in.readLong();
        labels = in.createTypedArrayList(Label.CREATOR);
        language = in.readString();
        linkedSpaceId = in.readLong();
        name = in.readString();
        obsoletedOn = in.readString();
        paymentConnectorConfiguration = in.readParcelable(ConnectorConfiguration.class.getClassLoader());
        plannedPurgeDate = in.readString();
        processorToken = in.readString();
        shippingAddress = in.readParcelable(Address.class.getClassLoader());
        state = in.readParcelable(TokenVersionState.class.getClassLoader());
        version = in.readInt();
        token = in.readParcelable(Token.class.getClassLoader());
    }

    public static final Creator<TokenVersion> CREATOR = new Creator<TokenVersion>() {
        @Override
        public TokenVersion createFromParcel(Parcel in) {
            return new TokenVersion(in);
        }

        @Override
        public TokenVersion[] newArray(int size) {
            return new TokenVersion[size];
        }
    };

    public List<Label> getLabels() {
        return labels;
    }

    public ConnectorConfiguration getPaymentConnectorConfiguration() {
        return paymentConnectorConfiguration;
    }

    public String getActivatedOn() {
        return activatedOn;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public ChargeAttemptEnvironment getEnvironment() {
        return environment;
    }

    public long getId() {
        return id;
    }

    public String getLanguage() {
        return language;
    }

    public long getLinkedSpaceId() {
        return linkedSpaceId;
    }

    public String getName() {
        return name;
    }

    public String getObsoletedOn() {
        return obsoletedOn;
    }

    public String getPlannedPurgeDate() {
        return plannedPurgeDate;
    }

    public String getProcessorToken() {
        return processorToken;
    }

    public TokenVersionState getState() {
        return state;
    }

    public int getVersion() {
        return version;
    }

    public Token getToken() {
        return token;
    }

    public Address getBillingAddress() {
        return billingAddress;
    }

    public Address getShippingAddress() {
        return shippingAddress;
    }

    @Override
    public int compareTo(@NonNull TokenVersion tokenVersion) {
        if(id < tokenVersion.getId()) {
            return -1;
        } else if(id > tokenVersion.getId()) {
            return 1;
        }
        return  0;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(activatedOn);
        parcel.writeParcelable(billingAddress, i);
        parcel.writeString(createdOn);
        parcel.writeParcelable(environment, i);
        parcel.writeLong(id);
        parcel.writeTypedList(labels);
        parcel.writeString(language);
        parcel.writeLong(linkedSpaceId);
        parcel.writeString(name);
        parcel.writeString(obsoletedOn);
        parcel.writeParcelable(paymentConnectorConfiguration, i);
        parcel.writeString(plannedPurgeDate);
        parcel.writeString(processorToken);
        parcel.writeParcelable(shippingAddress, i);
        parcel.writeParcelable(state, i);
        parcel.writeInt(version);
        parcel.writeParcelable(token, i);
    }
}
