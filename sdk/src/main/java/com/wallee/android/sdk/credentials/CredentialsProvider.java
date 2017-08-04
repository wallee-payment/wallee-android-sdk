package com.wallee.android.sdk.credentials;

import com.wallee.android.sdk.util.Check;
import com.wallee.android.sdk.util.AsynchronousCallback;

/**
 * The credentials provider is responsible to manage and store the {@link Credentials}.
 *
 * <p>{@link Credentials} need to be provided by the app backend server. The backend server is
 * responsible to fetch the credentials from the wallee web service. This way the backend server can
 * bind the credentials to the session resp. the app user. This ensures that the backend server
 * controls at any point what the user is allowed to do.</p>
 *
 * <p>The provider is ensures that the credentials are properly cached and refreshed whenever they
 * are required. For a more persistent storage the {@link CredentialsStore} is used.</p>
 *
 * <p>The implementation is thread-safe and as such the provider can be called from multiple
 * threads.</p>
 */
public final class CredentialsProvider {

    private final CredentialsStore store;
    private final CredentialsFetcher fetcher;

    private final Object lock = new Object();
    private volatile Credentials credentials = null;

    public CredentialsProvider(CredentialsStore store, CredentialsFetcher fetcher) {
        this.store = Check.requireNonNull(store, "The store is required.");
        this.fetcher = Check.requireNonNull(fetcher, "The fetcher is required.");
    }

    /**
     * This method can be called to execute a command which requires a current set of {@link
     * Credentials}. The method is executed asynchronously when required. When the credentials are
     * already present and valid the {@code executor} is called immediately.
     *
     * @param executor the executor which should be invoked once the credentials are onTokenSelectionViewReady to be
     *                 used.
     */
    public void getCredentials(final AsynchronousCallback<Credentials> executor) {
        if (this.credentials == null) {
            this.credentials = this.store.getCredentials();
        }

        if (this.credentials == null || !this.credentials.isValid()) {
            // We do not lock here since the fetcher will be most likely also executed asynchronous
            // and as such a lock will not prevent the fetching of the credentials in parallel. We
            // cannot prevent that the fetcher to be called multiple times.
            this.fetcher.fetchCredentials(new AsynchronousCallback<Credentials>() {
                @Override
                public void process(Credentials parameter) {
                    Check.requireNonNull(parameter, "The fetcher " + fetcher.getClass() + " provides an invalid credential object. It is null.");
                    if (!parameter.isValid()) {
                        throw new IllegalStateException("The fetcher " + fetcher.getClass() + " provides an invalid credential object. It is already expired.");
                    }
                    checkAndSet(parameter);
                    executor.process(credentials);
                }
            });
        } else {
            executor.process(this.credentials);
        }
    }

    private void checkAndSet(Credentials parameter) {
        synchronized (lock) {
            if (this.credentials != null) {
                this.credentials.checkCredentials(parameter);
            }
            // We only update the credentials when we are sure that the newly provided
            // credentials are a longer time valid.
            if (this.credentials == null || parameter.getTimestamp() > this.credentials.getTimestamp()) {
                this.credentials = parameter;

                // We need to propagate the credentials to the store, so when we get destructed we
                // can rebuild it from the store.
                this.store.updateCredentials(this.credentials);
            }
        }
    }


}
