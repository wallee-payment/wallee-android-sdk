package com.wallee.android.sdk.flow.config;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.wallee.android.sdk.credentials.CredentialsProvider;
import com.wallee.android.sdk.flow.listener.FlowListener;
import com.wallee.android.sdk.request.icon.IconRequestManager;
import com.wallee.android.sdk.request.icon.InMemoryIconCache;
import com.wallee.android.sdk.request.icon.VolleyIconRequestManager;
import com.wallee.android.sdk.request.api.VolleyWebServiceApiClient;
import com.wallee.android.sdk.request.api.WebServiceApiClient;
import com.wallee.android.sdk.util.Check;
import com.wallee.android.sdk.request.icon.IconCache;
import com.wallee.android.sdk.view.awaiting.AwaitingFinalStateViewFactory;
import com.wallee.android.sdk.view.awaiting.DefaultAwaitingFinalStateViewFactory;
import com.wallee.android.sdk.view.failure.DefaultFailureViewFactory;
import com.wallee.android.sdk.view.failure.FailureViewFactory;
import com.wallee.android.sdk.view.form.DefaultPaymentFormViewFactory;
import com.wallee.android.sdk.view.form.PaymentFormViewFactory;
import com.wallee.android.sdk.view.selection.method.PaymentMethodListViewFactory;
import com.wallee.android.sdk.view.selection.method.DefaultPaymentMethodListViewFactory;
import com.wallee.android.sdk.view.selection.token.TokenListViewFactory;
import com.wallee.android.sdk.view.selection.token.DefaultTokenListViewFactory;
import com.wallee.android.sdk.view.success.DefaultSuccessViewFactory;
import com.wallee.android.sdk.view.success.SuccessViewFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * The flow configuration holds all the configuration which influence the way how the {@link
 * FlowConfiguration} is working.
 *
 * <p>The configuration is immutable and as such it can be passed along different threads.</p>
 */
public final class FlowConfiguration {

    private final PaymentFormViewFactory paymentFormViewFactory;
    private final TokenListViewFactory tokenListViewFactory;
    private final PaymentMethodListViewFactory paymentMethodListViewFactory;
    private final SuccessViewFactory successViewFactory;
    private final FailureViewFactory failureViewFactory;
    private final AwaitingFinalStateViewFactory awaitingFinalStateViewFactory;
    private final IconCache iconCache;
    private final List<? extends FlowListener> listeners;
    private final IconRequestManager iconRequestManager;
    private final WebServiceApiClient webServiceApiClient;

    public FlowConfiguration(PaymentFormViewFactory paymentFormViewFactory,
                             TokenListViewFactory tokenListViewFactory,
                             PaymentMethodListViewFactory paymentMethodListViewFactory,
                             SuccessViewFactory successViewFactory,
                             AwaitingFinalStateViewFactory awaitingFinalStateViewFactory,
                             FailureViewFactory failureViewFactory,
                             IconCache iconCache, List<? extends FlowListener> listeners,
                             IconRequestManager iconRequestManager,
                             WebServiceApiClient webServiceApiClient) {
        this.paymentFormViewFactory = Check.requireNonNull(paymentFormViewFactory, "The paymentFormViewFactory is required.");
        this.tokenListViewFactory = Check.requireNonNull(tokenListViewFactory, "The tokenListViewFactory is required.");
        this.paymentMethodListViewFactory = Check.requireNonNull(paymentMethodListViewFactory, "The paymentMethodListViewFactory is required.");
        this.successViewFactory = Check.requireNonNull(successViewFactory, "The successViewFactory is required.");
        this.iconCache = Check.requireNonNull(iconCache, "The iconCache is required.");
        this.listeners = Collections.unmodifiableList(new ArrayList<>(Check.requireNonNull(listeners, "The listeners is required.")));
        this.failureViewFactory = Check.requireNonNull(failureViewFactory, "The failureViewFactory is required.");
        this.awaitingFinalStateViewFactory = Check.requireNonNull(awaitingFinalStateViewFactory, "The awaitingFinalStateViewFactory is required.");
        this.iconRequestManager = Check.requireNonNull(iconRequestManager, "The iconRequestManager is required.");
        this.webServiceApiClient = Check.requireNonNull(webServiceApiClient, "The webServiceApiClient is required.");

    }

    /**
     * The payment form view factory creates the view which collects the payment information.
     *
     * @return the factory which is used to create the payment form view.
     */
    public PaymentFormViewFactory getPaymentFormViewFactory() {
        return paymentFormViewFactory;
    }

    /**
     * The token selection view lets the user select a token from a list of tokens. The selected
     * token will be used to process the transaction.
     *
     * @return the factory which is responsible to create teh token selection view.
     */
    public TokenListViewFactory getTokenListViewFactory() {
        return tokenListViewFactory;
    }

    /**
     * The payment method selection view allows the user to select from a list of payment methods
     * the one which should be used to process the transaction with.
     *
     * @return the factory which is responsible for creating the payment method selection view.
     */
    public PaymentMethodListViewFactory getPaymentMethodListViewFactory() {
        return paymentMethodListViewFactory;
    }

    /**
     * The success view is displayed once the transaction has been processed successfully.
     *
     * @return the factory which is used to create the success view.
     */
    public SuccessViewFactory getSuccessViewFactory() {
        return successViewFactory;
    }

    /**
     * The icon cache is required to cache the downloaded icons locally on the device to avoid
     * downloading them over and over again.
     *
     * @return the icon cache in which the payment method icons are cached in.
     */
    public IconCache getIconCache() {
        return iconCache;
    }

    /**
     * A listener can implement any subinterface of {@link FlowListener}. The listener will be
     * informed about all of those events.
     *
     * @return the list of listeners which listing on the payment flow.
     */
    public List<? extends FlowListener> getListeners() {
        return listeners;
    }

    /**
     * When the transaction fails the {@link com.wallee.android.sdk.flow.FlowCoordinator} will show
     * a view which explains to the user why the transaction failed. This factory is responsible for
     * this view.
     *
     * @return the factory which is responsible for creating the view for failed transactions.
     */
    public FailureViewFactory getFailureViewFactory() {
        return failureViewFactory;
    }

    /**
     * The transaction may not reach a final state immediately. In this case the awaiting final
     * state view will be displayed. The returned factory is used to create this view.
     *
     * @return the factory which creates the view for the awaiting final state view.
     */
    public AwaitingFinalStateViewFactory getAwaitingFinalStateViewFactory() {
        return awaitingFinalStateViewFactory;
    }

    /**
     * Each payment method has an icon. The icon is configured through the backend of wallee.
     * Therefore the icon is downloaded dynamically. The icon request manager is responsible to
     * manage this download process.
     *
     * @return the icon request manager is used to download the payment method icons from the remote
     * server.
     */
    public IconRequestManager getIconRequestManager() {
        return iconRequestManager;
    }

    /**
     * The web service API client allows to access directly the web service API of wallee. This
     * client wraps the API to make it more easy to use with Java.
     *
     * @return the web service API client which is used to connect to the wallee web service.
     */
    public WebServiceApiClient getWebServiceApiClient() {
        return webServiceApiClient;
    }

    /**
     * This method returns the listeners which implements the {@code type} interface.
     *
     * <p>The method can be used to filter the listeners to a particular listener interface.</p>
     *
     * @param type the listener interface to which the list of listeners should be reduced to.
     * @param <L>  the type of the listener interface.
     * @return the list of listeners which implement the {@code type} interface.
     */
    public <L extends FlowListener> List<L> getListenersByType(Class<L> type) {
        List<L> result = new ArrayList<>();
        for (FlowListener listener : this.getListeners()) {
            if (type.isInstance(listener)) {
                result.add(type.cast(listener));
            }
        }
        return Collections.unmodifiableList(result);
    }

    /**
     * The builder allows to construct a flow configuration.
     *
     * <p>When there is no specific behaviour required the {@link #minimal(CredentialsProvider,
     * Context)} method provides the simplest possible configuration which works out of the
     * box.</p>
     */
    public static class Builder {

        private PaymentFormViewFactory paymentFormViewFactory = new DefaultPaymentFormViewFactory();
        private TokenListViewFactory tokenListViewFactory = new DefaultTokenListViewFactory();
        private PaymentMethodListViewFactory paymentMethodListViewFactory = new DefaultPaymentMethodListViewFactory();
        private SuccessViewFactory successViewFactory = new DefaultSuccessViewFactory();
        private FailureViewFactory failureViewFactory = new DefaultFailureViewFactory();
        private AwaitingFinalStateViewFactory awaitingFinalStateViewFactory = new DefaultAwaitingFinalStateViewFactory();
        private final List<FlowListener> listeners = new ArrayList<>();
        private IconCache iconCache;
        private IconRequestManager iconRequestManager;
        private WebServiceApiClient webServiceApiClient;

        /**
         * This method provides a minimal configuration which works for most cases. It is suitable
         * when there are not customization required.
         *
         * <p>The provided configuration will use an in-memory cache (see {@link InMemoryIconCache}
         * for the payment icons and it will use a default {@link RequestQueue} which is stored in a
         * static field (see {@link VolleyRequestQueueHolder}.</p>
         *
         * @param credentialsProvider the credential provider which should be used.
         * @param context             the context which should be used to construct the request
         *                            queue.
         * @return a builder instance which can be either directly used to construct a configuration
         * or eventually additional customization may be applied.
         */
        public static Builder minimal(CredentialsProvider credentialsProvider, Context context) {
            RequestQueue queue = VolleyRequestQueueHolder.getRequestQueue(context);
            return new Builder()
                    .setWebServiceApiClient(new VolleyWebServiceApiClient(queue, credentialsProvider))
                    .setIconRequestManager(new VolleyIconRequestManager(queue))
                    .setIconCache(new InMemoryIconCache(new VolleyIconRequestManager(queue), IconCache.DEFAULT_ICON_LOADING_TIMEOUT));
        }


        /**
         * This method adds a listener to the configuration builder.
         *
         * <p>The listener can implement any number of sub interfaces of {@link FlowListener}. All
         * listener interfaces implemented by the provided {@code listeners} will be picked up.</p>
         *
         * @param listeners the listeners which should be registered.
         * @return this builder
         */
        public Builder addListeners(FlowListener... listeners) {
            return this.addListeners(Arrays.asList(listeners));
        }

        /**
         * This method adds a listener to the configuration builder.
         *
         * <p>The listener can implement any number of sub interfaces of {@link FlowListener}. All
         * listener interfaces implemented by the provided {@code listeners} will be picked up.</p>
         *
         * @param listeners the listeners which should be registered.
         * @return this builder
         */
        public Builder addListeners(Collection<FlowListener> listeners) {
            for (FlowListener listener : listeners) {
                this.listeners.add(listener);
            }
            return this;
        }

        /**
         * This method sets a listener to the configuration builder.
         *
         * <p>The listener can implement any number of sub interfaces of {@link FlowListener}. All
         * listener interfaces implemented by the provided {@code listeners} will be picked up.</p>
         *
         * @param listeners the listeners which should be registered.
         * @return this builder
         */
        public Builder setListeners(Collection<? extends FlowListener> listeners) {
            this.listeners.clear();
            for (FlowListener listener : listeners) {
                this.listeners.add(listener);
            }
            return this;
        }


        /**
         * This method sets the cache which stores the downloaded images from the remote server.
         *
         * <p>The cache is required to improve the performance of the loading of the payment method
         * selection screen and token selection screen. Depending on the app purpose the cache may
         * be in-memory, transient or a file based.</p>
         *
         * @param iconCache the cache which should be used for the downloaded icons.
         * @return this builder
         * @see IconCache
         */
        public Builder setIconCache(IconCache iconCache) {
            this.iconCache = iconCache;
            return this;
        }

        /**
         * This method sets the icon request manager which should be used.
         *
         * <p>The icon request manager is responsible for downloading the icons from the remote
         * server.</p>
         *
         * <p>You may consider the {@link VolleyIconRequestManager} which is an implementation which
         * uses {@link com.android.volley.toolbox.Volley}.</p>
         *
         * @param iconRequestManager the request manager for downloading the icons from the remote
         *                           server.
         * @return this builder
         * @see IconRequestManager
         */
        public Builder setIconRequestManager(IconRequestManager iconRequestManager) {
            this.iconRequestManager = iconRequestManager;
            return this;
        }

        /**
         * This method sets the web service API client.
         *
         * <p>The web service API client is used to communicate with the wallee web service API.
         * This includes the fetching of the payment methods etc.</p>
         *
         * <p>You may consider the {@link VolleyWebServiceApiClient} which is an implementation
         * which uses internally {@link com.android.volley.toolbox.Volley}.</p>
         *
         * @param webServiceApiClient the web service client to use for sending API requests to the
         *                            remote web service.
         * @return this builder.
         */
        public Builder setWebServiceApiClient(WebServiceApiClient webServiceApiClient) {
            this.webServiceApiClient = webServiceApiClient;
            return this;
        }

        /**
         * This method defines the factory which creates the form for the payment information
         * collection.
         *
         * <p>The payment form is a {@link android.webkit.WebView} which is responsible to collect
         * all payment details.</p>
         *
         * @param paymentFormViewFactory the factory which should be used.
         * @return this builder.
         * @see PaymentFormViewFactory
         * @see DefaultPaymentFormViewFactory
         */
        public Builder setPaymentFormViewFactory(PaymentFormViewFactory paymentFormViewFactory) {
            this.paymentFormViewFactory = paymentFormViewFactory;
            return this;
        }

        /**
         * This method sets the token selection view. This view is responsible to let the user
         * decide which token should be used for processing.
         *
         * @param tokenListViewFactory the factory which should be used to create the token selection
         *                         screen.
         * @return this builder.
         * @see TokenListViewFactory
         * @see DefaultTokenListViewFactory
         */
        public Builder setTokenListViewFactory(TokenListViewFactory tokenListViewFactory) {
            this.tokenListViewFactory = tokenListViewFactory;
            return this;
        }

        /**
         * This method sets the factory which is responsible for creating the payment method
         * selection screen.
         *
         * @param paymentMethodListViewFactory the factory which should be used to create the payment
         *                                 method selection view.
         * @return this builder.
         * @see PaymentMethodListViewFactory
         * @see DefaultPaymentMethodListViewFactory
         */
        public Builder setPaymentMethodListViewFactory(PaymentMethodListViewFactory paymentMethodListViewFactory) {
            this.paymentMethodListViewFactory = paymentMethodListViewFactory;
            return this;
        }

        /**
         * This method sets the factory which is responsible for creating the success view.
         *
         * <p>When the transaction is successful the coordinator shows a view with a success
         * message. This method controls which factory is used to create this view.</p>
         *
         * @param successViewFactory the factory which should be used to create the success view.
         * @return this builder
         * @see SuccessViewFactory
         * @see DefaultSuccessViewFactory
         */
        public Builder setSuccessViewFactory(SuccessViewFactory successViewFactory) {
            this.successViewFactory = successViewFactory;
            return this;
        }

        /**
         * This method sets the factory which is responsible for creating the failure view.
         *
         * <p>When the transaction fails the coordinator shows a view with a failure
         * message. This method controls which factory is used to create this view.</p>
         *
         * @param failureViewFactory the factory which should be used to create the failure view.
         * @return this builder.
         * @see FailureViewFactory
         * @see DefaultFailureViewFactory
         */
        public Builder setFailureViewFactory(FailureViewFactory failureViewFactory) {
            this.failureViewFactory = failureViewFactory;
            return this;
        }

        /**
         * This method sets the factory which is responsible for creating the awaiting final state
         * view.
         *
         * <p>When the transaction is processed by the coordinator it can happen that the
         * transaction is not immediately in a final state. This factory is responsible to create
         * the view which is shown in this situation. </p>
         *
         * @param awaitingFinalStateViewFactory the factory which should be used to create the
         *                                      awaiting final state view.
         * @return this builder.
         * @see AwaitingFinalStateViewFactory
         * @see DefaultAwaitingFinalStateViewFactory
         */

        public Builder setAwaitingFinalStateViewFactory(AwaitingFinalStateViewFactory awaitingFinalStateViewFactory) {
            this.awaitingFinalStateViewFactory = awaitingFinalStateViewFactory;
            return this;
        }

        /**
         * This method builds the configuration based up on the properties set on the builder.
         *
         * @return the newly created configuration.
         */
        public FlowConfiguration build() {
            return new FlowConfiguration(
                    paymentFormViewFactory, tokenListViewFactory, paymentMethodListViewFactory,
                    successViewFactory, awaitingFinalStateViewFactory, failureViewFactory,
                    iconCache, listeners, iconRequestManager, webServiceApiClient);
        }
    }

}
