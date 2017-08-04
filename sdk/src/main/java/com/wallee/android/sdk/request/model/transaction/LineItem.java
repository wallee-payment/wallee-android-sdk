package com.wallee.android.sdk.request.model.transaction;

import android.os.Parcel;
import android.os.Parcelable;

import com.wallee.android.sdk.request.model.base.Tax;
import com.wallee.android.sdk.util.ParcelableHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hunziker on 7/24/17.
 */

public class LineItem implements Parcelable{
    private float aggregatedTaxRate;
    private float amountExcludingTax;
    private float amountIncludingTax;
    private Map<String, LineItemAttribute> attributes;
    private String name;
    private float quantity;
    private boolean shippingRequired;
    private String sku;
    private float taxAmount;
    private float taxAmountPerUnit;
    private List<Tax> taxes;
    private LineItemType type;
    private String uniqueId;
    private float unitPriceExcludingTax;
    private float unitPriceIncludingTax;

    protected LineItem(Parcel in) {
        aggregatedTaxRate = in.readFloat();
        amountExcludingTax = in.readFloat();
        amountIncludingTax = in.readFloat();
        attributes = new HashMap<>();
        ParcelableHelper.readSerializableMap(in, attributes, String.class, LineItemAttribute.class);
        name = in.readString();
        quantity = in.readFloat();
        shippingRequired = in.readByte() != 0;
        sku = in.readString();
        taxAmount = in.readFloat();
        taxAmountPerUnit = in.readFloat();
        taxes = in.createTypedArrayList(Tax.CREATOR);
        taxes = new ArrayList<>();
        in.readList(taxes, Tax.class.getClassLoader());
        type = in.readParcelable(LineItemType.class.getClassLoader());
        uniqueId = in.readString();
        unitPriceExcludingTax = in.readFloat();
        unitPriceIncludingTax = in.readFloat();
    }

    public static final Creator<LineItem> CREATOR = new Creator<LineItem>() {
        @Override
        public LineItem createFromParcel(Parcel in) {
            return new LineItem(in);
        }

        @Override
        public LineItem[] newArray(int size) {
            return new LineItem[size];
        }
    };

    public float getAggregatedTaxRate() {
        return aggregatedTaxRate;
    }

    public float getAmountExcludingTax() {
        return amountExcludingTax;
    }

    public float getAmountIncludingTax() {
        return amountIncludingTax;
    }

    public Map<String, LineItemAttribute> getAttributes() {
        return attributes;
    }

    public String getName() {
        return name;
    }

    public float getQuantity() {
        return quantity;
    }

    public boolean isShippingRequired() {
        return shippingRequired;
    }

    public String getSku() {
        return sku;
    }

    public float getTaxAmount() {
        return taxAmount;
    }

    public float getTaxAmountPerUnit() {
        return taxAmountPerUnit;
    }

    public LineItemType getType() {
        return type;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public float getUnitPriceExcludingTax() {
        return unitPriceExcludingTax;
    }

    public float getUnitPriceIncludingTax() {
        return unitPriceIncludingTax;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeFloat(aggregatedTaxRate);
        parcel.writeFloat(amountExcludingTax);
        parcel.writeFloat(amountIncludingTax);
        ParcelableHelper.writeSerializableMap(parcel, attributes);
        parcel.writeString(name);
        parcel.writeFloat(quantity);
        parcel.writeByte((byte) (shippingRequired ? 1 : 0));
        parcel.writeString(sku);
        parcel.writeFloat(taxAmount);
        parcel.writeFloat(taxAmountPerUnit);
        parcel.writeTypedList(taxes);
        parcel.writeParcelable(type, i);
        parcel.writeString(uniqueId);
        parcel.writeFloat(unitPriceExcludingTax);
        parcel.writeFloat(unitPriceIncludingTax);
    }
}
