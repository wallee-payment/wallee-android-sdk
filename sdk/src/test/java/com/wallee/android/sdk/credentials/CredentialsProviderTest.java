package com.wallee.android.sdk.credentials;

import com.wallee.android.sdk.credentials.Credentials;
import com.wallee.android.sdk.credentials.CredentialsFetcher;
import com.wallee.android.sdk.credentials.CredentialsProvider;
import com.wallee.android.sdk.credentials.CredentialsStore;
import com.wallee.android.sdk.credentials.SimpleCredentialsStore;
import com.wallee.android.sdk.util.AsynchronousCallback;

import junit.framework.Assert;

import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Tests {@link com.wallee.android.sdk.credentials.CredentialsProvider}
 */
public class CredentialsProviderTest {


    /**
     * Tests if the provider is working with a valid credentials provider.
     */
    @Test
    public void testGetCredentialsValid() {

        long validTimestamp = System.currentTimeMillis() + 5 * 60 * 1000;
        final Credentials credentials = new Credentials("316-16005-" + Long.toString(validTimestamp) +
                "-c4LUhOqIiFrwEcNU3YAJl4_28x3_b2iQAeqJI7V6yP8-grantedUser419");

        SimpleCredentialsStore store = new SimpleCredentialsStore();
        final AtomicInteger numberOfCalls = new AtomicInteger(0);
        final CredentialsFetcher fetcher = new CredentialsFetcher() {
            @Override
            public void fetchCredentials(AsynchronousCallback<Credentials> receiver) {
                receiver.process(credentials);
                numberOfCalls.getAndIncrement();
            }
        };
        CredentialsProvider provider = new CredentialsProvider(store, fetcher);

        Assert.assertEquals(0, numberOfCalls.get());

        AsynchronousCallback<Credentials> checker = new AsynchronousCallback<Credentials>() {
            @Override
            public void process(Credentials parameter) {
                Assert.assertEquals(credentials, parameter);
            }
        };

        provider.getCredentials(checker);
        Assert.assertEquals(1, numberOfCalls.get());

        // We invoke the provider a second time to make sure the result is really cached.
        provider.getCredentials(checker);
        Assert.assertEquals(1, numberOfCalls.get());

        // Now we create a new provider with the same store and see that the fetcher is not invoked
        // again.
        CredentialsProvider provider2 = new CredentialsProvider(store, fetcher);

        provider.getCredentials(checker);
        Assert.assertEquals(1, numberOfCalls.get());

    }

    /**
     * Tests if the provider is working with a valid credentials provider.
     */
    @Test(expected = IllegalStateException.class)
    public void testGetCredentialsInvalid() {

        long validTimestamp = System.currentTimeMillis() - 60 * 1000;
        final Credentials credentials = new Credentials("316-16005-" + Long.toString(validTimestamp) +
                "-c4LUhOqIiFrwEcNU3YAJl4_28x3_b2iQAeqJI7V6yP8-grantedUser419");

        SimpleCredentialsStore store = new SimpleCredentialsStore();
        final AtomicInteger numberOfCalls = new AtomicInteger(0);
        final CredentialsFetcher fetcher = new CredentialsFetcher() {
            @Override
            public void fetchCredentials(AsynchronousCallback<Credentials> receiver) {
                receiver.process(credentials);
                numberOfCalls.getAndIncrement();
            }
        };
        CredentialsProvider provider = new CredentialsProvider(store, fetcher);

        Assert.assertEquals(0, numberOfCalls.get());

        AsynchronousCallback<Credentials> checker = new AsynchronousCallback<Credentials>() {
            @Override
            public void process(Credentials parameter) {
                // nothing to do since we expect that the test fails before.
            }
        };
        provider.getCredentials(checker);
    }


}
