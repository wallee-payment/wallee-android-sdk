package com.wallee.android.sdk.request.model.method;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.wallee.android.sdk.request.model.base.DatabaseTranslatedString;
import com.wallee.android.sdk.request.model.base.ImageResourcePath;
import com.wallee.android.sdk.util.CompareUtil;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by simonwalter on 26.07.17.
 */

public class PaymentMethodConfiguration implements Comparable<PaymentMethodConfiguration>, Parcelable {
    private DataCollectionType dataCollectionType;
    private DatabaseTranslatedString description;
    private long id;
    private ImageResourcePath imageResourcePath;
    private long linkedSpaceId;
    private String name;
    private OneClickPaymentMode oneClickPaymentMode;
    private long paymentMethod;
    private String plannedPurgeDate;
    private Map<String, String> resolvedDescription = new HashMap<>();
    private String resolvedImageUrl;
    private Map<String, String> resolvedTitle = new HashMap<>();
    private int sortOrder;
    private long spaceId;
    private DatabaseTranslatedString title;
    private int version;


    protected PaymentMethodConfiguration(Parcel in) {
        dataCollectionType = in.readParcelable(DataCollectionType.class.getClassLoader());
        description = in.readParcelable(DatabaseTranslatedString.class.getClassLoader());
        id = in.readLong();
        imageResourcePath = in.readParcelable(ImageResourcePath.class.getClassLoader());
        linkedSpaceId = in.readLong();
        name = in.readString();
        oneClickPaymentMode = in.readParcelable(OneClickPaymentMode.class.getClassLoader());
        paymentMethod = in.readLong();
        plannedPurgeDate = in.readString();
        sortOrder = in.readInt();
        spaceId = in.readLong();
        title = in.readParcelable(DatabaseTranslatedString.class.getClassLoader());
        version = in.readInt();

        int resolvedDescription = in.readInt();
        for (int i = 0; i < resolvedDescription; i++) {
            String key = in.readString();
            String value = in.readString();
            this.resolvedDescription.put(key, value);
        }

        int resolveTitleSize = in.readInt();
        for (int i = 0; i < resolveTitleSize; i++) {
            String key = in.readString();
            String value = in.readString();
            this.resolvedTitle.put(key, value);
        }
        this.resolvedImageUrl = in.readString();
    }


    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(dataCollectionType, i);
        parcel.writeParcelable(description, i);
        parcel.writeLong(id);
        parcel.writeParcelable(imageResourcePath, i);
        parcel.writeLong(linkedSpaceId);
        parcel.writeString(name);
        parcel.writeParcelable(oneClickPaymentMode, i);
        parcel.writeLong(paymentMethod);
        parcel.writeString(plannedPurgeDate);
        parcel.writeInt(sortOrder);
        parcel.writeLong(spaceId);
        parcel.writeParcelable(title, i);
        parcel.writeInt(version);

        parcel.writeInt(resolvedDescription.size());
        for (Map.Entry<String, String> entry : resolvedDescription.entrySet()) {
            parcel.writeString(entry.getKey());
            parcel.writeString(entry.getValue());
        }

        parcel.writeInt(resolvedTitle.size());
        for (Map.Entry<String, String> entry : resolvedTitle.entrySet()) {
            parcel.writeString(entry.getKey());
            parcel.writeString(entry.getValue());
        }

        parcel.writeString(this.resolvedImageUrl);
    }

    public static final Creator<PaymentMethodConfiguration> CREATOR = new Creator<PaymentMethodConfiguration>() {
        @Override
        public PaymentMethodConfiguration createFromParcel(Parcel in) {
            return new PaymentMethodConfiguration(in);
        }

        @Override
        public PaymentMethodConfiguration[] newArray(int size) {
            return new PaymentMethodConfiguration[size];
        }
    };

    public String getResolvedImageUrl() {
        return this.resolvedImageUrl;
    }

    @Override
    public int compareTo(@NonNull PaymentMethodConfiguration paymentMethodConfiguration) {
        int result = CompareUtil.compare(sortOrder, paymentMethodConfiguration.getSortOrder());
        if (result == 0) {
            return CompareUtil.compare(this.getId(), paymentMethodConfiguration.getId());
        } else {
            return result;
        }
    }


    public DataCollectionType getDataCollectionType() {
        return dataCollectionType;
    }

    public DatabaseTranslatedString getDescription() {
        return description;
    }

    public long getId() {
        return id;
    }

    public ImageResourcePath getImageResourcePath() {
        return imageResourcePath;
    }

    public long getLinkedSpaceId() {
        return linkedSpaceId;
    }

    public String getName() {
        return name;
    }

    public OneClickPaymentMode getOneClickPaymentMode() {
        return oneClickPaymentMode;
    }

    public long getPaymentMethod() {
        return paymentMethod;
    }

    public String getPlannedPurgeDate() {
        return plannedPurgeDate;
    }

    public Map<String, String> getResolvedDescription() {
        return resolvedDescription;
    }

    public Map<String, String> getResolvedTitle() {
        return resolvedTitle;
    }

    public String getResolvedTitle(Locale locale) {
        String language = locale.getLanguage();
        String key = language + "-" + locale.getCountry();
        String rs = resolvedTitle.get(key);
        if (rs != null) {
            return rs;
        }
        else {
            for (Map.Entry<String, String> entry : resolvedTitle.entrySet()) {
                if (entry.getKey().toLowerCase().startsWith(language)) {
                    return entry.getValue();
                }
            }
            return resolvedTitle.entrySet().iterator().next().getValue();
        }
    }

    public int getSortOrder() {
        return sortOrder;
    }

    public long getSpaceId() {
        return spaceId;
    }

    public DatabaseTranslatedString getTitle() {
        return title;
    }

    public int getVersion() {
        return version;
    }

    @Override
    public int describeContents() {
        return 0;
    }



}
