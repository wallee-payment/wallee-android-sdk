package com.wallee.android.sdk.request.model.base;

import android.os.Parcel;
import android.os.Parcelable;

import com.wallee.android.sdk.util.ParcelableHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by simonwalter on 26.07.17.
 */

public class LabelDescriptor implements Parcelable {

    private LabelDescriptorCategory category;
    private Map<String, String> description;
    private List<Long> features;
    private long group;
    private long id;
    private Map<String, String> name;
    private long type;
    private long weight;


    protected LabelDescriptor(Parcel in) {
        category = in.readParcelable(LabelDescriptorCategory.class.getClassLoader());
        description = new HashMap<>();
        ParcelableHelper.readSerializableMap(in, description, String.class, String.class);
        features = new ArrayList<>();
        in.readList(features, Long.class.getClassLoader());
        group = in.readLong();
        id = in.readLong();
        name = new HashMap<>();
        ParcelableHelper.readSerializableMap(in, name, String.class, String.class);
        type = in.readLong();
        weight = in.readLong();
    }

    public static final Creator<LabelDescriptor> CREATOR = new Creator<LabelDescriptor>() {
        @Override
        public LabelDescriptor createFromParcel(Parcel in) {
            return new LabelDescriptor(in);
        }

        @Override
        public LabelDescriptor[] newArray(int size) {
            return new LabelDescriptor[size];
        }
    };

    public LabelDescriptorCategory getCategory() {
        return category;
    }

    public Map<String, String> getDescription() {
        return description;
    }

    public List<Long> getFeatures() {
        return features;
    }

    public long getGroup() {
        return group;
    }

    public long getId() {
        return id;
    }

    public Map<String, String> getName() {
        return name;
    }

    public long getType() {
        return type;
    }

    public long getWeight() {
        return weight;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(category, i);
        ParcelableHelper.writeSerializableMap(parcel, description);
        parcel.writeList(features);
        parcel.writeLong(group);
        parcel.writeLong(id);
        ParcelableHelper.writeSerializableMap(parcel, name);
        parcel.writeLong(type);
        parcel.writeLong(weight);
    }
}
