package com.wallee.android.sdk.credentials;

/**
 * The credentials store is responsible to store the {@link Credentials} in a way that a destruction
 * of the {@link CredentialsProvider} can be filled with the {@link Credentials} when it is
 * restored. This can happen for example when the user rotate the device.
 *
 * <p>The implementor may uses the bundle of an {@link android.app.Activity} or {@link
 * android.app.Fragment} to store them.</p>
 */
public interface CredentialsStore {

    /**
     * @return the current stored credentials or {@code null} when there are not credentials present
     * in the store.
     */
    Credentials getCredentials();

    /**
     * This method stores the credentials into a more permanent location. For example in the bundle
     * of {@link android.app.Activity} or {@link android.app.Fragment}.
     *
     * <p>The implementor may delay the storage until a destruction event occurs. The storage does
     * not have to be reliable (e.g. an app crash has not to be survived).</p>
     *
     * @param credentials the credentials which should be stored.
     */
    void updateCredentials(Credentials credentials);

}
