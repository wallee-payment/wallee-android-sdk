package com.wallee.android.sdk.credentials;

/**
 * A minimal implementation of {@link CredentialsStore} which does store the credentials in a member
 * variable. This may be suitable when the destruction of the {@link CredentialsProvider} is
 * announced and the credentials can be stored whenever this event occurs. Within this even the
 * {@link #getCredentials()} method can be called to store the credentials.
 */
public final class SimpleCredentialsStore implements CredentialsStore {

    private volatile Credentials credentials;

    @Override
    public Credentials getCredentials() {
        return this.credentials;
    }

    @Override
    public void updateCredentials(Credentials credentials) {
        this.credentials = credentials;
    }
}
