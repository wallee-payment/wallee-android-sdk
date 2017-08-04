package com.wallee.android.sdk.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.wallee.android.sdk.flow.config.FlowConfiguration;
import com.wallee.android.sdk.flow.listener.FlowListener;

/**
 * This is a concrete implementation of {@link AbstractPaymentFragment} which assumes that the
 * activity into which the fragment is embedded into implements certain interfaces.
 *
 * <p>The activity which is using this fragment has to implement the {@link
 * CredentialsProviderResolver} and  {@link FragmentTerminationListener} interfaces. Optionally the
 * activity can implement any sub interface of {@link FlowListener}. For customization options see
 * {@link AbstractPaymentFragment}.</p>
 */
public class ActivityAwarePaymentFragment extends AbstractPaymentFragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        if (!(this.getActivity() instanceof CredentialsProviderResolver)) {
            throw new IllegalStateException("When using the " + ActivityAwarePaymentFragment.class +
                    " the activity must implement " + CredentialsProviderResolver.class);
        }
        if (!(this.getActivity() instanceof FragmentTerminationListener)) {
            throw new IllegalStateException("When using the " + ActivityAwarePaymentFragment.class +
                    " the activity must implement " + FragmentTerminationListener.class);
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    protected CredentialsProviderResolver getCredentialsProviderResolver() {
        return (CredentialsProviderResolver) this.getActivity();
    }

    @Override
    protected FragmentTerminationListener getFragmentTerminationListener() {
        return (FragmentTerminationListener) this.getActivity();
    }

    @Override
    protected FlowConfiguration.Builder createFlowConfigurationBuilder(FlowListener listener) {
        FlowConfiguration.Builder builder = super.createFlowConfigurationBuilder(listener);
        if (this.getActivity() instanceof FlowListener) {
            builder.addListeners((FlowListener) this.getActivity());
        }
        return builder;
    }
}
