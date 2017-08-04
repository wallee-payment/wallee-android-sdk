package com.wallee.android.sdk.credentials;

import android.os.Parcel;
import android.os.Parcelable;

import com.wallee.android.sdk.util.Check;

import java.io.Serializable;

/**
 * The credentials represents the authentication details which allows to invoke the wallee API. The
 * credentials are valid for a limit time and only for a particular transaction.
 *
 * <p>The credentials are unmodifiable.</p>
 */
public final class Credentials implements Serializable, Parcelable {

    /**
     * The threshold which is used to check if the credentials are still valid. The threshold
     * defines the minimum time in milliseconds which the credentials need to be valid.
     */
    public final static long THRESHOLD = 2 * 60 * 1000;

    private final String credentials;
    private final long transactionId;
    private final long spaceId;
    private final long timestamp;

    /**
     * Constructor which takes as input the formatted string as provided by the wallee API. The
     * constructor parses the credentials into a structured form.
     *
     * @param credentials the credential string which should be parsed.
     * @throws InvalidCredentialsException thrown when the provided credential string is not valid.
     */
    public Credentials(String credentials) throws InvalidCredentialsException {
        Check.requireNonEmpty(credentials, "The credentials are required to create a new credentials object.");
        String[] props = credentials.trim().split("-");

        if (props.length < 3 || props[0].equals("") || props[1].equals("") || props[2].equals("")) {
            throw new InvalidCredentialsException("Wrong credentials format.");
        }

        this.spaceId = Long.parseLong(props[0]);
        this.transactionId = Long.parseLong(props[1]);
        if (this.spaceId < 0 || this.transactionId < 0) {
            throw new InvalidCredentialsException("IDs can not be negative.");
        }

        this.timestamp = Long.parseLong(props[2]);
        if (this.timestamp < 0) {
            throw new InvalidCredentialsException("Timestamps can not be negative.");
        }
        this.credentials = credentials.trim();

        // We do intentionally not check here directly if the provided credentials are still valid
        // to avoid any issue when the credentials are loaded from a storage. We need to check them
        // before we use them.
    }

    @Override
    public String toString() {
        return this.credentials;
    }

    private Credentials(Parcel in) {
        credentials = in.readString();
        transactionId = in.readLong();
        spaceId = in.readLong();
        timestamp = in.readLong();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(credentials);
        dest.writeLong(transactionId);
        dest.writeLong(spaceId);
        dest.writeLong(timestamp);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Credentials> CREATOR = new Creator<Credentials>() {
        @Override
        public Credentials createFromParcel(Parcel in) {
            return new Credentials(in);
        }

        @Override
        public Credentials[] newArray(int size) {
            return new Credentials[size];
        }
    };

    /**
     * @return {@code true} when the credentials are still valid otherwise the method returns {@code
     * false}.
     */
    public boolean isValid() {
        long now = System.currentTimeMillis();
        return now + THRESHOLD < this.timestamp;

    }

    /**
     * This method checks if the provided {@code other} credential is a valid replacement of this
     * credentials. Means the method checks whether the {@code other} credentials belongs to the
     * same transaction as this.
     *
     * @param other the other credential pair which should be checked.
     * @throws IllegalStateException thrown when the provided {@code other} is not a valid
     *                               replacement.
     */
    public void checkCredentials(Credentials other) throws IllegalStateException {
        if (this.getTransactionId() != other.getTransactionId()) {
            throw new IllegalStateException("The provided credentials do not have the same transaction transactionId.");
        } else if (this.getSpaceId() != other.getSpaceId()) {
            throw new IllegalStateException("The provided credentials do not have the same space transactionId.");
        }
    }

    /**
     * @return the credentials as they are provided through the wallee web service
     */
    public String getCredentials() {
        return credentials;
    }

    /**
     * @return the transaction ID for which the credentials are build for. This is the transaction
     * id to which the credentials give access to.
     */
    public long getTransactionId() {
        return transactionId;
    }

    /**
     * @return the ID of the space to which the transaction belongs to.
     */
    public long getSpaceId() {
        return spaceId;
    }

    /**
     * @return the timestamp (milliseconds since 1970-01-01) when the credentials will expire on.
     */
    public long getTimestamp() {
        return timestamp;
    }

}
