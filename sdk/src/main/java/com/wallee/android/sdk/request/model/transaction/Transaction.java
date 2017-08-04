package com.wallee.android.sdk.request.model.transaction;

import android.os.Parcel;
import android.os.Parcelable;

import com.wallee.android.sdk.request.model.method.ConnectorConfiguration;
import com.wallee.android.sdk.request.model.method.PaymentMethodBrand;
import com.wallee.android.sdk.request.model.base.FailureReason;
import com.wallee.android.sdk.request.model.token.Token;
import com.wallee.android.sdk.util.ParcelableHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class Transaction implements Parcelable {
    private String acceptHeader;
    private List<PaymentMethodBrand> allowedPaymentMethodBrands;
    private List<Long> allowedPaymentMethodConfigurations;
    private float authorizationAmount;
    private String authorizedOn;
    private Address billingAddress;
    private boolean chargeRetryEnabled;
    private String completedOn;
    private String completionTimeoutOn;
    private long confirmedBy;
    private String confirmedOn;
    private long createdBy;
    private String createdOn;
    private String currency;
    private String customerEmailAddress;
    private String customerId;
    private CustomersPresence customersPresence;
    private String endOfLife;
    private String failedOn;
    private String failedUrl;
    private FailureReason failureReason;
    private TransactionGroup group;
    private long id;
    private String internetProtocolAddress;
    private String internetProtocolAddressCountry;
    private String invoiceMerchantReference;
    private String language;
    private List<LineItem> lineItems;
    private long linkedSpaceId;
    private String merchantReference;
    private Map<String, String> metaData;
    private ConnectorConfiguration paymentConnectorConfiguration;
    private String plannedPurgeDate;
    private String processingOn;
    private float refundedAmount;
    private Address shippingAddress;
    private String shippingMethod;
    private long spaceViewId;
    private TransactionState state;
    private String successUrl;
    private Token token;
    private String userAgentHeader;
    private String userFailureMessage;
    private TransactionUserInterfaceType userInterfaceType;
    private int version;

    protected Transaction(Parcel in) {
        acceptHeader = in.readString();
        allowedPaymentMethodBrands = in.createTypedArrayList(PaymentMethodBrand.CREATOR);
        allowedPaymentMethodConfigurations = new ArrayList<>();
        in.readList(allowedPaymentMethodConfigurations, Long.class.getClassLoader());
        authorizationAmount = in.readFloat();
        authorizedOn = in.readString();
        billingAddress = in.readParcelable(Address.class.getClassLoader());
        chargeRetryEnabled = in.readByte() != 0;
        completedOn = in.readString();
        completionTimeoutOn = in.readString();
        confirmedBy = in.readLong();
        confirmedOn = in.readString();
        createdBy = in.readLong();
        createdOn = in.readString();
        currency = in.readString();
        customerEmailAddress = in.readString();
        customerId = in.readString();
        customersPresence = in.readParcelable(CustomersPresence.class.getClassLoader());
        endOfLife = in.readString();
        failedOn = in.readString();
        failedUrl = in.readString();
        group = in.readParcelable(TransactionGroup.class.getClassLoader());
        id = in.readLong();
        internetProtocolAddress = in.readString();
        internetProtocolAddressCountry = in.readString();
        invoiceMerchantReference = in.readString();
        language = in.readString();
        lineItems = in.createTypedArrayList(LineItem.CREATOR);
        linkedSpaceId = in.readLong();
        merchantReference = in.readString();
        metaData = new HashMap<>();
        ParcelableHelper.readSerializableMap(in, metaData, String.class, String.class);
        paymentConnectorConfiguration = in.readParcelable(ConnectorConfiguration.class.getClassLoader());
        plannedPurgeDate = in.readString();
        processingOn = in.readString();
        refundedAmount = in.readFloat();
        shippingAddress = in.readParcelable(Address.class.getClassLoader());
        shippingMethod = in.readString();
        spaceViewId = in.readLong();
        state = in.readParcelable(TransactionState.class.getClassLoader());
        successUrl = in.readString();
        token = in.readParcelable(Token.class.getClassLoader());
        userAgentHeader = in.readString();
        userInterfaceType = in.readParcelable(TransactionUserInterfaceType.class.getClassLoader());
        version = in.readInt();
    }

    public static final Creator<Transaction> CREATOR = new Creator<Transaction>() {
        @Override
        public Transaction createFromParcel(Parcel in) {
            return new Transaction(in);
        }

        @Override
        public Transaction[] newArray(int size) {
            return new Transaction[size];
        }
    };

    public boolean isAwaitingFinalState() {
        return this.state == TransactionState.PENDING || this.state == TransactionState.PROCESSING
                || this.state == TransactionState.CONFIRMED;
    }

    public boolean isFailed() {
        return this.state == TransactionState.FAILED || this.state == TransactionState.DECLINE;
    }

    public boolean isSuccessful() {
        return this.state == TransactionState.AUTHORIZED || this.state == TransactionState.COMPLETED
                || this.state == TransactionState.FULFILL;
    }

    public String getAcceptHeader() {
        return acceptHeader;
    }

    public float getAuthorizationAmount() {
        return authorizationAmount;
    }

    public String getAuthorizedOn() {
        return authorizedOn;
    }

    public Address getBillingAddress() {
        return billingAddress;
    }

    /**
     * When the charging of the customer fails we can retry the charging.
     * This implies that we redirect the user back to the payment page which
     * allows the customer to retry. By default we will retry.
     *
     * @return chargeRetryEnabled
     */
    public boolean isChargeRetryEnabled() {
        return chargeRetryEnabled;
    }

    public String getCompletedOn() {
        return completedOn;
    }

    public String getCompletionTimeoutOn() {
        return completionTimeoutOn;
    }

    public long getConfirmedBy() {
        return confirmedBy;
    }

    public String getConfirmedOn() {
        return confirmedOn;
    }

    public long getCreatedBy() {
        return createdBy;
    }

    /**
     * The created on date indicates the date on which the
     * entity was stored into the database.
     *
     * @return createdOn, String in DateTime Format
     */
    public String getCreatedOn() {
        return createdOn;
    }

    public String getCurrency() {
        return currency;
    }

    /**
     * The customer email address is the email address of the customer.
     * If no email address is used provided on the shipping or billing
     * address this address is used.
     *
     * @return customerEmailAddress, String in the format of an email address.
     */
    public String getCustomerEmailAddress() {
        return customerEmailAddress;
    }

    public String getCustomerId() {
        return customerId;
    }

    public CustomersPresence getCustomersPresence() {
        return customersPresence;
    }

    /**
     * The transaction's end of life indicates the date from which on
     * no operation can be carried out anymore.
     *
     * @return String in DateTime Format
     */
    public String getEndOfLife() {
        return endOfLife;
    }

    public String getFailedOn() {
        return failedOn;
    }

    /**
     * The user will be redirected to failed URL when the transaction could
     * not be authorized or completed. In case no failed URL is specified a
     * default failed page will be displayed.
     *
     * @return String
     */
    public String getFailedUrl() {
        return failedUrl;
    }

    /**
     * The ID is the primary key of the entity. The ID identifies the entity uniquely.
     *
     * @return long id, long
     */
    public long getId() {
        return id;
    }

    public String getInternetProtocolAddress() {
        return internetProtocolAddress;
    }

    public String getInternetProtocolAddressCountry() {
        return internetProtocolAddressCountry;
    }

    public String getInvoiceMerchantReference() {
        return invoiceMerchantReference;
    }

    public String getLanguage() {
        return language;
    }

    public List<LineItem> getLineItems() {
        return lineItems;
    }

    /**
     * The linked space id holds the ID of the space to which the entity belongs to.
     *
     * @return spaceId, long
     */
    public long getLinkedSpaceId() {
        return linkedSpaceId;
    }

    public String getMerchantReference() {
        return merchantReference;
    }

    /**
     * Meta data allow to store additional data along the object.
     *
     * @return MetaData, Map<String, String>
     */
    public Map<String, String> getMetaData() {
        return metaData;
    }

    /**
     * The planned purge date indicates when the entity is permanently removed.
     * When the date is null the entity is not planned to be removed.
     *
     * @return plannedPurgeDate, String in DateTime format
     */
    public String getPlannedPurgeDate() {
        return plannedPurgeDate;
    }

    public String getProcessingOn() {
        return processingOn;
    }

    /**
     * The refunded amount is the total amount which has been refunded so far.
     *
     * @return refundedAmount, float
     */
    public float getRefundedAmount() {
        return refundedAmount;
    }

    public Address getShippingAddress() {
        return shippingAddress;
    }

    public String getShippingMethod() {
        return shippingMethod;
    }

    public long getSpaceViewId() {
        return spaceViewId;
    }

    public TransactionState getState() {
        return state;
    }

    /**
     * The user will be redirected to success URL when the transaction could
     * be authorized or completed. In case no success URL is specified a
     * default success page will be displayed.
     *
     * @return successUrl, String
     */
    public String getSuccessUrl() {
        return successUrl;
    }

    public Token getToken() {
        return token;
    }

    public String getUserAgentHeader() {
        return userAgentHeader;
    }

    public List<PaymentMethodBrand> getAllowedPaymentMethodBrands() {
        return allowedPaymentMethodBrands;
    }

    public List<Long> getAllowedPaymentMethodConfigurations() {
        return allowedPaymentMethodConfigurations;
    }

    public FailureReason getFailureReason() {
        return failureReason;
    }

    public TransactionGroup getGroup() {
        return group;
    }

    public ConnectorConfiguration getPaymentConnectorConfiguration() {
        return paymentConnectorConfiguration;
    }

    public String getUserFailureMessage() {
        return userFailureMessage;
    }

    public TransactionUserInterfaceType getUserInterfaceType() {
        return userInterfaceType;
    }

    /**
     * The version number indicates the version of the entity. The version is
     * incremented whenever the entity is changed.
     *
     * @return version, int
     */
    public int getVersion() {
        return version;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(acceptHeader);
        parcel.writeTypedList(allowedPaymentMethodBrands);
        parcel.writeList(allowedPaymentMethodConfigurations);
        parcel.writeFloat(authorizationAmount);
        parcel.writeString(authorizedOn);
        parcel.writeParcelable(billingAddress, i);
        parcel.writeByte((byte) (chargeRetryEnabled ? 1 : 0));
        parcel.writeString(completedOn);
        parcel.writeString(completionTimeoutOn);
        parcel.writeLong(confirmedBy);
        parcel.writeString(confirmedOn);
        parcel.writeLong(createdBy);
        parcel.writeString(createdOn);
        parcel.writeString(currency);
        parcel.writeString(customerEmailAddress);
        parcel.writeString(customerId);
        parcel.writeParcelable(customersPresence, i);
        parcel.writeString(endOfLife);
        parcel.writeString(failedOn);
        parcel.writeString(failedUrl);
        parcel.writeParcelable(group, i);
        parcel.writeLong(id);
        parcel.writeString(internetProtocolAddress);
        parcel.writeString(internetProtocolAddressCountry);
        parcel.writeString(invoiceMerchantReference);
        parcel.writeString(language);
        parcel.writeTypedList(lineItems);
        parcel.writeLong(linkedSpaceId);
        parcel.writeString(merchantReference);
        ParcelableHelper.writeSerializableMap(parcel, metaData);
        parcel.writeParcelable(paymentConnectorConfiguration, i);
        parcel.writeString(plannedPurgeDate);
        parcel.writeString(processingOn);
        parcel.writeFloat(refundedAmount);
        parcel.writeParcelable(shippingAddress, i);
        parcel.writeString(shippingMethod);
        parcel.writeLong(spaceViewId);
        parcel.writeParcelable(state, i);
        parcel.writeString(successUrl);
        parcel.writeParcelable(token, i);
        parcel.writeString(userAgentHeader);
        parcel.writeParcelable(userInterfaceType, i);
        parcel.writeInt(version);
    }
}
