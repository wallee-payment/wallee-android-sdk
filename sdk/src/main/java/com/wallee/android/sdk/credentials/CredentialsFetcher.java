package com.wallee.android.sdk.credentials;

import android.content.Context;

import com.wallee.android.sdk.util.AsynchronousCallback;


/**
 * The credential fetcher is responsible to retrieve the {@link Credentials} from the remote server.
 * It is responsible to connect to the server which is actually able to store the API username and
 * shared secret in a secure way.
 *
 * <p>Normally the implementor will use some authentication to authenticate against the secure
 * remote server and fetch the credentials from the {@code wallee} web service. This way the remote
 * server controls who can access which transaction.</p>
 */
public interface CredentialsFetcher {

    /**
     * This method is called whenever a new {@link Credentials} are required. The implementor can
     * invoke the remote server in asynchronous way for example by using a {@link
     * com.android.volley.toolbox.Volley#newRequestQueue(Context)}.
     *
     * <p>This method is only called when it is required. Means the credentials are expired or do
     * not work anymore. As such the implementor does not need to worry about caching those
     * credentials. The caller is responsible to cache the result in an appropriated way.</p>
     *
     * <p>Once the credentials has been fetched they have to be provided to the {@code
     * receiver}.</p>
     *
     * <p>The method is only called when the credentials are required. Means the method will be
     * called shortly before the credentials are required by an operation or task. As such the first
     * call can be quit delayed.</p>
     *
     * @param receiver the receiver which has to be called to update the credentials. They will be
     *                 cached until a refresh is required.
     */
    void fetchCredentials(AsynchronousCallback<Credentials> receiver);

}
