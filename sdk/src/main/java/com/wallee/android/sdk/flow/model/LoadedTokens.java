package com.wallee.android.sdk.flow.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.wallee.android.sdk.request.model.method.PaymentMethodConfiguration;
import com.wallee.android.sdk.request.model.token.TokenVersion;
import com.wallee.android.sdk.util.ParcelableHelper;
import com.wallee.android.sdk.request.model.method.PaymentMethodIcon;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The loaded tokens holds the loaded {@link TokenVersion} and {@link PaymentMethodIcon}. We use
 * this class to make them serializable. This way we can pass them along the state change.
 */
public final class LoadedTokens implements Parcelable {
    private final List<TokenVersion> tokenVersions;
    private final Map<PaymentMethodConfiguration, PaymentMethodIcon> icons;


    public LoadedTokens(List<TokenVersion> tokenVersions, Map<PaymentMethodConfiguration, PaymentMethodIcon> icons) {
        this.tokenVersions = Collections.unmodifiableList(new ArrayList<>(tokenVersions));
        this.icons = Collections.unmodifiableMap(new HashMap<>(icons));
    }

    private LoadedTokens(Parcel in) {
        tokenVersions = Collections.unmodifiableList(in.createTypedArrayList(TokenVersion.CREATOR));
        Map<PaymentMethodConfiguration, PaymentMethodIcon> map = new HashMap<>();
        ParcelableHelper.readParcelableMap(in, map, PaymentMethodConfiguration.class, PaymentMethodIcon.class);
        this.icons = Collections.unmodifiableMap(map);
    }

    public static final Creator<LoadedTokens> CREATOR = new Creator<LoadedTokens>() {
        @Override
        public LoadedTokens createFromParcel(Parcel in) {
            return new LoadedTokens(in);
        }

        @Override
        public LoadedTokens[] newArray(int size) {
            return new LoadedTokens[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeTypedList(tokenVersions);
        ParcelableHelper.writeParcelableMap(parcel, icons, i);
    }

    public List<TokenVersion> getTokenVersions() {
        return tokenVersions;
    }

    public Map<PaymentMethodConfiguration, PaymentMethodIcon> getIcons() {
        return icons;
    }
}
