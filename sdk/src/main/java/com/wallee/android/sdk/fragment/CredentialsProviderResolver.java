package com.wallee.android.sdk.fragment;

import com.wallee.android.sdk.credentials.CredentialsProvider;

/**
 * This interface is used to lookup the {@link CredentialsProvider}.
 */
public interface CredentialsProviderResolver {

    /**
     * This method should return an instance of {@link CredentialsProvider}. This method is invoked
     * when the {@link com.wallee.android.sdk.flow.FlowCoordinator} is created.
     *
     * @return the credentials provider which should be used.
     */
    CredentialsProvider resolveCredentialsProvider();

}
