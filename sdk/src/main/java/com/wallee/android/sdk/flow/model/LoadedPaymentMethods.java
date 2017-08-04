package com.wallee.android.sdk.flow.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.wallee.android.sdk.request.model.method.PaymentMethodConfiguration;
import com.wallee.android.sdk.util.ParcelableHelper;
import com.wallee.android.sdk.request.model.method.PaymentMethodIcon;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class holds the data which is passed from the payment method loading state to the payment
 * method listing state.
 */
public final class LoadedPaymentMethods implements Parcelable {
    private final List<PaymentMethodConfiguration> paymentMethods;
    private final Map<PaymentMethodConfiguration, PaymentMethodIcon> icons;

    public LoadedPaymentMethods(List<PaymentMethodConfiguration> paymentMethods, Map<PaymentMethodConfiguration, PaymentMethodIcon> icons) {
        this.paymentMethods = Collections.unmodifiableList(new ArrayList<>(paymentMethods));
        this.icons = Collections.unmodifiableMap(new HashMap<>(icons));
    }

    protected LoadedPaymentMethods(Parcel in) {
        paymentMethods = Collections.unmodifiableList(in.createTypedArrayList(PaymentMethodConfiguration.CREATOR));
        Map<PaymentMethodConfiguration, PaymentMethodIcon> map = new HashMap<>();
        ParcelableHelper.readParcelableMap(in, map, PaymentMethodConfiguration.class, PaymentMethodIcon.class);
        this.icons = Collections.unmodifiableMap(map);
    }

    public static final Creator<LoadedPaymentMethods> CREATOR = new Creator<LoadedPaymentMethods>() {
        @Override
        public LoadedPaymentMethods createFromParcel(Parcel in) {
            return new LoadedPaymentMethods(in);
        }

        @Override
        public LoadedPaymentMethods[] newArray(int size) {
            return new LoadedPaymentMethods[size];
        }
    };

    public List<PaymentMethodConfiguration> getPaymentMethods() {
        return paymentMethods;
    }

    public Map<PaymentMethodConfiguration, PaymentMethodIcon> getIcons() {
        return icons;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeTypedList(paymentMethods);
        ParcelableHelper.writeParcelableMap(parcel, icons, i);
    }
}
