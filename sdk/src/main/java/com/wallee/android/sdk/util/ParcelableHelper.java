package com.wallee.android.sdk.util;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.Map;

/**
 * This class helps to convert {@link Serializable} into {@link Parcelable}.
 */
public final class ParcelableHelper {

    /**
     * Writes a map of {@link Serializable} into a {@link Parcelable}.
     *
     * @param parcel the parceable which should be written into.
     * @param map    the map which should be converted.
     * @param <K>    the key type.
     * @param <V>    the value type of the map.
     */
    public static <K extends Serializable, V extends Serializable> void writeSerializableMap(
            Parcel parcel, Map<K, V> map) {
        parcel.writeInt(map.size());
        for (Map.Entry<K, V> e : map.entrySet()) {
            parcel.writeSerializable(e.getKey());
            parcel.writeSerializable(e.getValue());
        }
    }

    /**
     * Reads the map from the parcel. This is the counter part to {@link #writeSerializableMap(Parcel,
     * Map)}.
     *
     * @param parcel the parceable from which the data should be read from.
     * @param map    the map into which the values should be written into.
     * @param <K>    the key type.
     * @param <V>    the value type.
     */
    public static <K extends Serializable, V extends Serializable> void readSerializableMap(
            Parcel parcel, Map<K, V> map, Class<K> keyType, Class<V> valueType) {
        int size = parcel.readInt();
        for (int i = 0; i < size; i++) {
            map.put(keyType.cast(parcel.readSerializable()), valueType.cast(parcel.readSerializable()));
        }
    }


    /**
     * Writes a map of {@link Parcelable}s into a {@link Parcelable}.
     *
     * @param parcel the parceable which should be written into.
     * @param map    the map which should be converted.
     * @param flags  the flags which should be passed to the parceable.
     * @param <K>    the key type.
     * @param <V>    the value type of the map.
     */
    public static <K extends Parcelable, V extends Parcelable> void writeParcelableMap(
            Parcel parcel, Map<K, V> map, int flags) {
        parcel.writeInt(map.size());
        for (Map.Entry<K, V> e : map.entrySet()) {
            parcel.writeParcelable(e.getKey(), flags);
            parcel.writeParcelable(e.getValue(), flags);
        }
    }

    /**
     * Reads the map from the parcel. This is the counter part to {@link #writeParcelableMap(Parcel,
     * Map, int)}.
     *
     * @param parcel the parceable from which the data should be read from.
     * @param map    the map into which the values should be written into.
     * @param <K>    the key type.
     * @param <V>    the value type.
     */
    public static <K extends Parcelable, V extends Parcelable> void readParcelableMap(
            Parcel parcel, Map<K, V> map, Class<K> kClass, Class<V> vClass) {
        int size = parcel.readInt();
        for (int i = 0; i < size; i++) {
            map.put(kClass.cast(parcel.readParcelable(kClass.getClassLoader())),
                    vClass.cast(parcel.readParcelable(vClass.getClassLoader())));
        }
    }

    private ParcelableHelper() {
        throw new IllegalAccessError();
    }

}
