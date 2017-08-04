package com.wallee.android.sample;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.wallee.android.sdk.credentials.CredentialsProvider;
import com.wallee.android.sdk.credentials.CredentialsStore;
import com.wallee.android.sdk.credentials.SimpleCredentialsStore;
import com.wallee.android.sdk.fragment.ActivityAwarePaymentFragment;
import com.wallee.android.sdk.fragment.CredentialsProviderResolver;
import com.wallee.android.sdk.fragment.FragmentTerminationListener;
import com.wallee.android.sdk.request.api.VolleyWebServiceApiClient;
import com.wallee.android.sdk.request.model.transaction.Transaction;

/**
 * Sample activity which shows how to use the {@link ActivityAwarePaymentFragment}.
 *
 * <p>In summary the activity has two fragments:</p>
 *
 * <ul>
 * <li>A checkout fragment which simulates a very simple checkout flow.</li>
 * <li>A {@link ActivityAwarePaymentFragment} which handles the payment flow.</li>
 * </ul>
 */
public class SamplePaymentFormActivity extends AppCompatActivity implements CredentialsProviderResolver, FragmentTerminationListener {
    private static final String TAG = SamplePaymentFormActivity.class.getSimpleName();
    private static final String BUNDLE_KEY = SamplePaymentFormActivity.class.getCanonicalName();


    private static final String BASE_URL = VolleyWebServiceApiClient.DEFAULT_BASE_URL;
    public final static long USER_ID = 480l;
    public final static String HMAC_KEY = "644gZTvd8KR2V+Lf4I9zmSnZVuXxd5YTT2U/CTKXHhk=";
    public final static long SPACE_ID = 316;

    private final CheckoutFragment checkoutFragment = new CheckoutFragment();
    private final ActivityAwarePaymentFragment paymentFragment = new ActivityAwarePaymentFragment();
    private boolean showPayment = false;

    private final static CredentialsStore credentialStore = new SimpleCredentialsStore();

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.sample_payment_activity);

        if (savedInstanceState != null && savedInstanceState.containsKey(BUNDLE_KEY) && savedInstanceState.getBoolean(BUNDLE_KEY)) {
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.checkout_fragment_container, paymentFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        } else {
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.checkout_fragment_container, checkoutFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
    }


    @Override
    public void onSuccessfulTermination(Transaction transactionObject) {
        checkoutFragment.setShowSuccessMessage(true);
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.checkout_fragment_container, checkoutFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
        this.showPayment = false;
    }

    @Override
    public void onFailureTermination(String userMessage, Transaction transaction) {
        checkoutFragment.setFailureMessage(userMessage);
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.checkout_fragment_container, checkoutFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
        this.showPayment = false;
    }

    private void startPayment() {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.checkout_fragment_container, paymentFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
        this.showPayment = true;
    }

    @Override
    public CredentialsProvider resolveCredentialsProvider() {
        return new CredentialsProvider(new SimpleCredentialsStore(), new TestCredentialsFetcher(getApplicationContext(), BASE_URL, USER_ID, HMAC_KEY, SPACE_ID));
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        if (this.showPayment) {
            this.paymentFragment.onSaveInstanceState(outState);
        }
    }

    @Override
    public void onBackPressed() {
        if (this.showPayment && this.paymentFragment != null) {
            if (!this.paymentFragment.onBackPressed()) {
                super.onBackPressed();
            }
        } else {
            super.onBackPressed();
        }
    }

    public static class CheckoutFragment extends Fragment {

        private String failureMessage;
        private TextView failureMessageView;
        private boolean showSuccessMessage = false;
        private TextView successMessageView;
        private Button payButton;

        public CheckoutFragment() {

        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            ViewGroup view = (ViewGroup) inflater.inflate(R.layout.checkout_fragment, container, false);
            failureMessageView = (TextView) view.findViewById(R.id.checkout_failure_message);
            successMessageView = (TextView) view.findViewById(R.id.checkout_success_message);
            this.payButton = (Button) view.findViewById(R.id.button_pay);
            this.updateFailureMessageView();
            this.updateSuccessMessage();

            this.payButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((SamplePaymentFormActivity) getActivity()).startPayment();
                }
            });

            return view;
        }

        public void setFailureMessage(String message) {
            this.failureMessage = message;
            this.updateFailureMessageView();
        }

        public void setShowSuccessMessage(boolean show) {
            this.showSuccessMessage = show;
            updateSuccessMessage();
        }

        private void updateFailureMessageView() {
            if (this.failureMessageView != null) {
                Activity activity = this.getActivity();
                if (activity != null) {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (failureMessageView != null) {
                                if (failureMessage == null) {
                                    failureMessageView.setVisibility(View.GONE);
                                } else {
                                    failureMessageView.setText(failureMessage);
                                    failureMessageView.setVisibility(View.VISIBLE);
                                }
                            }
                        }
                    });
                }
            }
        }

        private void updateSuccessMessage() {
            if (this.successMessageView != null) {
                Activity activity = this.getActivity();
                if (activity != null) {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (successMessageView != null) {
                                if (showSuccessMessage) {
                                    successMessageView.setVisibility(View.VISIBLE);
                                } else {
                                    successMessageView.setVisibility(View.GONE);
                                }
                            }
                        }
                    });
                }
            }
        }

    }

}
