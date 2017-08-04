package com.wallee.android.sdk.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.RequestQueue;
import com.wallee.android.sdk.R;
import com.wallee.android.sdk.credentials.CredentialsProvider;
import com.wallee.android.sdk.flow.FlowAction;
import com.wallee.android.sdk.flow.FlowCoordinator;
import com.wallee.android.sdk.flow.config.FlowConfiguration;
import com.wallee.android.sdk.flow.config.VolleyRequestQueueHolder;
import com.wallee.android.sdk.flow.listener.FlowListener;
import com.wallee.android.sdk.request.api.VolleyWebServiceApiClient;
import com.wallee.android.sdk.request.icon.IconCache;
import com.wallee.android.sdk.request.icon.InMemoryIconCache;
import com.wallee.android.sdk.request.icon.VolleyIconRequestManager;
import com.wallee.android.sdk.util.Check;

/**
 * This is an abstract fragment which helps to use {@link FlowCoordinator}.
 *
 * <p>This abstract fragment allows to process a whole payment flow. It manages every step of the
 * payment flow. Customization options:</p>
 *
 * <ul> <li>Inject a custom {@link FragmentFlowListener} which allows to modify the behaviour.</li>
 * <li>Override the {@link #createFlowConfigurationBuilder(FlowListener)} method to adjust the
 * coordinator configuration to change the view etc.</li>
 * <li>Override the layout files to adjust
 * the look and feel of the views.</li>
 * <li>Override the {@link #createPaymentContainerEnlarger(ViewGroup)}
 * method to inject a custom enlarger which provides more flexibility around how the form behaves
 * within the activity.</li>
 * </ul>
 */
public abstract class AbstractPaymentFragment extends Fragment {

    private FlowCoordinator flowCoordinator;

    /**
     * This method can be invoked to trigger going back within the payment fragment.
     *
     * @return {@code true} when the event was handled by the fragment.
     */
    public boolean onBackPressed() {
        if (flowCoordinator != null) {
            return flowCoordinator.triggerAction(FlowAction.GO_BACK);
        }
        return false;
    }

    /**
     * This method returns the resolver of {@link CredentialsProvider}. The implementor may create
     * an instance of it or fetch it from some other source.
     *
     * @return the resolver of the {@link CredentialsProvider}.
     */
    protected abstract CredentialsProviderResolver getCredentialsProviderResolver();

    /**
     * This method returns the listener which reacts up on the final states of the payment process.
     *
     * @return the listener which reacts up on the finale states of the transaction.
     */
    protected abstract FragmentTerminationListener getFragmentTerminationListener();

    @Nullable
    @Override
    public final View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        ViewGroup fragmentView = (ViewGroup) inflater.inflate(R.layout.wallee_fragment, container, false);
        ViewGroup paymentFormContainer = (ViewGroup) fragmentView.findViewById(R.id.wallee_payment_form_container);


        final FragmentTerminationListener fragmentTerminationListener = getFragmentTerminationListener();
        if (fragmentTerminationListener == null) {
            throw new IllegalStateException("The fragment termination listener cannot be null.");
        }

        final PaymentContainerEnlarger enlarger = createPaymentContainerEnlarger(paymentFormContainer);

        final FragmentFlowListener listener = this.createEventListener(enlarger, fragmentTerminationListener, fragmentView);
        if (listener == null) {
            throw new IllegalStateException("The listener cannot be null.");
        }


        FlowConfiguration configuration = createFlowConfigurationBuilder(listener).build();

        this.flowCoordinator = new FlowCoordinator(configuration);
        listener.setFlowCoordinator(flowCoordinator);
        View paymentPanel = this.flowCoordinator.onCreateView(inflater, paymentFormContainer, savedInstanceState);
        paymentFormContainer.removeAllViews();
        paymentFormContainer.addView(paymentPanel);

        return fragmentView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (this.flowCoordinator != null) {
            this.flowCoordinator.onSaveInstanceState(outState);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (this.flowCoordinator != null) {
            this.flowCoordinator.onDestroy();
        }
    }

    /**
     * This method constructs the coordinator configuration. Subclasses may override this method to
     * inject custom configuration values such as {@link FlowListener}. It allows also to provide
     * custom view factories to inject custom views for the different views within the coordinator.
     *
     * @param listener the fragment listener. This listener is responsible to react based upon the
     *                 events within the coordinator to adjust the fragment.
     * @return the configuration builder which may be modified by the subclasses.
     */
    protected FlowConfiguration.Builder createFlowConfigurationBuilder(FlowListener listener) {

        final CredentialsProvider credentialsProvider = getCredentialsProviderResolver().resolveCredentialsProvider();
        if (credentialsProvider == null) {
            throw new IllegalStateException("The credentials provider cannot be null.");
        }

        RequestQueue queue = VolleyRequestQueueHolder.getRequestQueue(getActivity().getApplication());
        return new FlowConfiguration.Builder()
                .setWebServiceApiClient(new VolleyWebServiceApiClient(queue, credentialsProvider))
                .setIconRequestManager(new VolleyIconRequestManager(queue))
                .setIconCache(new InMemoryIconCache(new VolleyIconRequestManager(queue),
                        IconCache.DEFAULT_ICON_LOADING_TIMEOUT))
                .addListeners(listener);
    }

    /**
     * This method creates the fragment listener for the flow. Essentially this listener is
     * responsible to map the flow events to the fragment functionality.
     *
     * @param enlarger                    the enlarge which is used. This is created through {@link
     *                                    #createPaymentContainerEnlarger(ViewGroup)},
     * @param fragmentTerminationListener the listener which coordinates the outcome of the payment
     *                                    process.
     * @param fragmentView                the fragment view holds all views for the fragment.
     * @return the listener which realizes the communication between the fragment and the
     * coordinator.
     */
    protected FragmentFlowListener createEventListener(PaymentContainerEnlarger enlarger, FragmentTerminationListener fragmentTerminationListener, ViewGroup fragmentView) {
        return new FragmentFlowListener(this, enlarger, fragmentTerminationListener, fragmentView);
    }

    /**
     * This method creates the view enlarger which is responsible to create more space for the
     * payment form within the activity.
     *
     * @param paymentFormContainer the container within which the coordinator places it's views.
     * @return the enlarger which is used to manage the resize of the fragment.
     */
    protected PaymentContainerEnlarger createPaymentContainerEnlarger(ViewGroup paymentFormContainer) {
        return new HierarchicalPaymentContainerEnlarger(paymentFormContainer, this.getActivity());
    }

}
