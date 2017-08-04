package com.wallee.android.sdk.flow.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * This model is passed from the payment method selection view to the payment form view. It contains
 * the selected payment method configuration ID.
 */
public final class SelectedPaymentMethodConfiguration implements Parcelable {

    private final long id;

    public SelectedPaymentMethodConfiguration(long id) {
        this.id = id;
    }

    protected SelectedPaymentMethodConfiguration(Parcel in) {
        id = in.readLong();
    }

    public static final Creator<SelectedPaymentMethodConfiguration> CREATOR = new Creator<SelectedPaymentMethodConfiguration>() {
        @Override
        public SelectedPaymentMethodConfiguration createFromParcel(Parcel in) {
            return new SelectedPaymentMethodConfiguration(in);
        }

        @Override
        public SelectedPaymentMethodConfiguration[] newArray(int size) {
            return new SelectedPaymentMethodConfiguration[size];
        }
    };

    public long getId() {
        return id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
    }
}
