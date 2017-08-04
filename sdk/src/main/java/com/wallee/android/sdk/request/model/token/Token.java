package com.wallee.android.sdk.request.model.token;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * The token represents stored payment information which can be used to trigger a payment. The
 * stored data is securely stored on a remote server.
 */
public final class Token implements Serializable, Parcelable {

    private String createdOn;
    private String customerEmailAddress;
    private String customerId;
    private boolean enabledForOneClickPayment;
    private String externalId;
    private long id;
    private String language;
    private long linkedSpaceId;
    private String plannedPurgeDate;
    private String state;
    private String tokenReference;
    private int version;

    private Token(Parcel in) {
        createdOn = in.readString();
        customerEmailAddress = in.readString();
        customerId = in.readString();
        enabledForOneClickPayment = in.readByte() != 0;
        externalId = in.readString();
        id = in.readLong();
        language = in.readString();
        linkedSpaceId = in.readLong();
        plannedPurgeDate = in.readString();
        state = in.readString();
        tokenReference = in.readString();
        version = in.readInt();
    }

    public static final Creator<Token> CREATOR = new Creator<Token>() {
        @Override
        public Token createFromParcel(Parcel in) {
            return new Token(in);
        }

        @Override
        public Token[] newArray(int size) {
            return new Token[size];
        }
    };

    public String getCreatedOn() {
        return createdOn;
    }

    public String getCustomerEmailAddress() {
        return customerEmailAddress;
    }

    public String getCustomerId() {
        return customerId;
    }

    public boolean isEnabledForOneClickPayment() {
        return enabledForOneClickPayment;
    }

    public String getExternalId() {
        return externalId;
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

    public String getPlannedPurgeDate() {
        return plannedPurgeDate;
    }

    public String getState() {
        return state;
    }

    public String getTokenReference() {
        return tokenReference;
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
        parcel.writeString(createdOn);
        parcel.writeString(customerEmailAddress);
        parcel.writeString(customerId);
        parcel.writeByte((byte) (enabledForOneClickPayment ? 1 : 0));
        parcel.writeString(externalId);
        parcel.writeLong(id);
        parcel.writeString(language);
        parcel.writeLong(linkedSpaceId);
        parcel.writeString(plannedPurgeDate);
        parcel.writeString(state);
        parcel.writeString(tokenReference);
        parcel.writeInt(version);
    }
}
