package com.wallee.android.sdk.view.form;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.wallee.android.sdk.R;
import com.wallee.android.sdk.request.model.transaction.MobileSdkUrl;
import com.wallee.android.sdk.util.AsynchronousCallback;
import com.wallee.android.sdk.util.Check;
import com.wallee.android.sdk.util.HttpError;
import com.wallee.android.sdk.util.MeasurementUnitUtil;

import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

/**
 * The payment form view is responsible for loading and handling the web form loaded to collect the
 * payment information. This is a default implementation of {@link PaymentFormView}.Subclasses may
 * override this class to adjust the layout.
 *
 * <p>This view is responsible for loading the SDK URL and to load the form within a {@link
 * WebView}.</p>
 *
 * <p>The {@link WebView} will by default not survive a screen rotation. This class implements the
 * {@link com.wallee.android.sdk.view.PersistableView} interface and as such certain input values
 * get stored. However Android does not provide a way to store the whole state of a web view. By
 * adding {@code android:configChanges="orientation|screenSize"} on the activity XML element within
 * the manifest file the issue can be solved.</p>
 */
public class DefaultPaymentFormView extends WebView implements PaymentFormView {

    private final static String TAG = DefaultPaymentFormView.class.getSimpleName();
    private final static String PARCEABLE_IDENTIFIER = DefaultPaymentFormView.class.getCanonicalName();


    private final PaymentFormView.Listener<DefaultPaymentFormView> listener;
    private final PaymentFormViewFactory.UrlFetcher urlFetcher;
    private final AtomicBoolean submitted = new AtomicBoolean(false);
    private final AtomicBoolean executingValidation = new AtomicBoolean(false);
    private final AtomicReference<MobileSdkUrl> mobileSdkUrl = new AtomicReference<>();
    private final long paymentMethodConfigurationId;
    private volatile int currentHeight = 0;
    private final AtomicBoolean enlarged = new AtomicBoolean(false);
    private final AtomicBoolean ready = new AtomicBoolean(false);
    private final Handler uiThreadHandler;
    private final Timer timer = new Timer();
    private final AtomicReference<TimerTask> timerTask = new AtomicReference<>();


    public DefaultPaymentFormView(Context context, PaymentFormView.Listener<DefaultPaymentFormView> listener, PaymentFormViewFactory.UrlFetcher urlFetcher, long selectedPaymentMethodConfigurationId) {
        super(context);
        this.listener = Check.requireNonNull(listener, "The listener is required.");
        this.urlFetcher = Check.requireNonNull(urlFetcher, "The URL provider is required.");
        this.paymentMethodConfigurationId = selectedPaymentMethodConfigurationId;
        this.uiThreadHandler = new Handler(Looper.getMainLooper());
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.init();
        this.setupTimerTask();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.cancelTimerTask();
    }

    @Override
    public void saveViewState(Bundle outState) {
        this.saveState(outState);
        outState.putParcelable(PARCEABLE_IDENTIFIER, new PaymentFormState(
                this.submitted.get(),
                this.executingValidation.get(),
                this.mobileSdkUrl.get(),
                this.paymentMethodConfigurationId,
                this.currentHeight,
                this.enlarged.get(),
                this.ready.get()));
    }

    @Override
    public void restoreViewState(Bundle inState) {
        this.restoreState(inState);

        Parcelable state = inState.getParcelable(PARCEABLE_IDENTIFIER);
        if (state != null && state instanceof PaymentFormState) {
            PaymentFormState casted = (PaymentFormState) state;
            this.submitted.set(casted.isSubmitted());
            this.executingValidation.set(casted.isExecutingValidation());
            this.ready.set(casted.isReady());
            this.enlarged.set(casted.isEnlarged());
            this.mobileSdkUrl.set(casted.getMobileSdkUrl());

            // We cannot allow a change of the payment method configuration ID through saving and
            // restoring. If we would so the form may be broken because we would not reload the
            // content of the WebView.
            if (this.paymentMethodConfigurationId != casted.getPaymentMethodConfigurationId()) {
                throw new IllegalStateException("It is not allowed to change the payment method " +
                        "configuration ID during the view usage. To change the payment method " +
                        "configuration the whole we has to be reloaded.");
            }

            this.currentHeight = casted.getCurrentHeight();
        }

    }

    private void init() {
        super.setWebViewClient(new PaymentFormWebViewClient(new CallbackListenerMapper(this)));
        super.getSettings().setJavaScriptEnabled(true);
        super.getSettings().setSaveFormData(false);
        super.getSettings().setSupportMultipleWindows(false);
        super.getSettings().setAllowUniversalAccessFromFileURLs(true);

        final DefaultPaymentFormView view = this;
        if (!this.ready.get()) {
            this.listener.onLoading(view);
        }
        this.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                return false;
            }
        });

        if (this.mobileSdkUrl.get() == null) {
            // It seems as we have not initialized the view at all. As such we need to get the URL
            // and load the form.
            this.urlFetcher.fetchMobileSdkUrl(new AsynchronousCallback<MobileSdkUrl>() {
                @Override
                public void process(MobileSdkUrl parameter) {
                    if (mobileSdkUrl.compareAndSet(null, parameter)) {
                        loadSdkUrl(parameter);
                        setupTimerTask();
                    }
                }
            });
        } else {
            if (isExpired()) {
                this.listener.onExpired(this);
            } else {
                if (this.ready.get()) {
                    this.listener.onReady(this);
                    setupTimerTask();
                }
                if (this.enlarged.get()) {
                    this.listener.onEnlargeView(this);
                } else {
                    this.adjustViewHeight();
                }
            }
        }
    }

    private void setupTimerTask() {
        if (this.mobileSdkUrl.get() != null && !this.enlarged.get()) {
            final DefaultPaymentFormView view = this;
            boolean added = this.timerTask.compareAndSet(null, new TimerTask() {
                @Override
                public void run() {
                    listener.onExpired(view);
                }
            });
            if (added) {
                timer.schedule(timerTask.get(), this.mobileSdkUrl.get().getExpiryDate() - System.currentTimeMillis());
            }
        }
    }

    private void cancelTimerTask() {
        TimerTask task = this.timerTask.get();
        if (task != null) {
            task.cancel();
        }
    }

    private void adjustViewHeight() {
        final DefaultPaymentFormView view = this;
        this.uiThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                if (!view.enlarged.get()) {
                    ViewGroup.LayoutParams params = view.getLayoutParams();
                    params.height = view.getCurrentTargetHeight();
                    Log.i(TAG, "Adjust Height to: " + params.height);
                    view.setLayoutParams(params);
                }
            }
        });
    }

    private void resetViewHeight() {
        final DefaultPaymentFormView view = this;
        this.uiThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                if (view.enlarged.get()) {
                    ViewGroup.LayoutParams params = view.getLayoutParams();
                    params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                    Log.i(TAG, "Reset View Height.");
                    view.setLayoutParams(params);
                }
            }
        });
    }


    private boolean isExpired() {
        // We are still on the same page as initially loaded and the URL has expired.
        return this.getUrl().startsWith(this.mobileSdkUrl.get().toString()) && this.mobileSdkUrl.get().isExpired();
    }

    private void loadSdkUrl(MobileSdkUrl url) {
        super.loadUrl(url.buildPaymentMethodUrl(this.paymentMethodConfigurationId).toString());

        // Make sure we use a transparent background.
        this.setBackgroundColor(0x00000000);

    }

    @Override
    public boolean isSubmitted() {
        return this.submitted.get();
    }

    @Override
    public final void validate() {
        if (this.executingValidation.compareAndSet(false, true)) {
            uiThreadHandler.post(new Runnable() {
                @Override
                public void run() {
                    sendValidationCommand();
                }
            });
        }
    }

    private void sendValidationCommand() {
        super.loadUrl("javascript:(function () { MobileSdkHandler.validate(); })()");
    }

    @Override
    public final void submit() {
        if (this.submitted.compareAndSet(false, true)) {
            uiThreadHandler.post(new Runnable() {
                @Override
                public void run() {
                    sendSubmitCommand();
                }
            });
        }
    }

    private void sendSubmitCommand() {
        super.loadUrl("javascript:(function () { MobileSdkHandler.submit(); })()");
    }

    @Override
    public final void loadData(String data, String mimeType, String encoding) {
        throw new UnsupportedOperationException("This method cannot be invoked in " +
                DefaultPaymentFormView.class + ".");
    }

    @Override
    public final void loadUrl(String url) {
        throw new UnsupportedOperationException("This method cannot be invoked in " +
                DefaultPaymentFormView.class + ".");
    }

    @Override
    public final void loadDataWithBaseURL(String baseUrl, String data, String mimeType, String encoding, String historyUrl) {
        throw new UnsupportedOperationException("This method cannot be invoked in " +
                DefaultPaymentFormView.class + ".");
    }

    @Override
    public final void loadUrl(String url, Map<String, String> additionalHttpHeaders) {
        throw new UnsupportedOperationException("This method cannot be invoked in " +
                DefaultPaymentFormView.class + ".");
    }

    @Override
    public final void setWebViewClient(WebViewClient client) {
        // We cannot allow to change the web client otherwise we do not control anymore the
        // interaction with the web page, which is an issue since such modification may cause issues
        // with the flow / state we assume.
        throw new UnsupportedOperationException("The web client cannot be overridden in the " +
                DefaultPaymentFormView.class + ".");
    }

    @Override
    public int getCurrentTargetHeight() {
        return (int) Math.ceil(MeasurementUnitUtil.convertDpToPixel(this.currentHeight, this.getContext()));
    }

    private static class CallbackListenerMapper implements PaymentFormWebViewClient.CallbackListener {

        private final DefaultPaymentFormView view;

        CallbackListenerMapper(DefaultPaymentFormView view) {
            this.view = view;
        }

        @Override
        public void onInitialize() {
            // We propagate the initialize event with the first height adjustment. Which is
            // triggered directly after this event. We do so that we can determine if the form
            // is empty or not.
        }

        @Override
        public void onHeightChange(int targetHeight) {
            this.view.currentHeight = targetHeight;
            this.view.adjustViewHeight();
            if (this.view.ready.compareAndSet(false, true)) {
                this.view.listener.onReady(view);
            }
        }

        @Override
        public void onEnlargeView() {
            if (this.view.enlarged.compareAndSet(false, true)) {
                this.view.resetViewHeight();
                this.view.listener.onEnlargeView(view);
                this.view.cancelTimerTask();
            }
        }

        @Override
        public void onValidationSuccess() {
            this.view.executingValidation.set(false);
            this.view.listener.onValidationSuccess(view);
        }

        @Override
        public void onValidationFailure(List<String> messages) {
            this.view.executingValidation.set(false);
            this.view.listener.onValidationFailure(view, messages);
        }

        @Override
        public void onAwaitingFinalStatus(long transactionId) {
            if (this.view.enlarged.compareAndSet(true, false)) {
                this.view.listener.onResetView(view);
            }
            this.view.listener.onAwaitingFinalState(view);
        }

        @Override
        public void onSuccess() {
            if (this.view.enlarged.compareAndSet(true, false)) {
                this.view.listener.onResetView(view);
            }
            this.view.listener.onSuccess(view);
        }

        @Override
        public void onFailure() {
            if (this.view.enlarged.compareAndSet(true, false)) {
                this.view.listener.onResetView(view);
            }
            this.view.listener.onFailure(view);
        }

        @Override
        public void onHttpError(HttpError error) {
            this.view.listener.onHttpError(view, error);
        }
    }

    /**
     * Holds the inner state of the payment form. This gets persisted into the bundle provided
     * through {@link com.wallee.android.sdk.view.PersistableView}.
     */
    private static class PaymentFormState implements Parcelable {

        private final boolean submitted;
        private final boolean executingValidation;
        private final MobileSdkUrl mobileSdkUrl;
        private final long paymentMethodConfigurationId;
        private final int currentHeight;
        private final boolean enlarged;
        private final boolean ready;

        PaymentFormState(boolean submitted, boolean executingValidation, MobileSdkUrl mobileSdkUrl, long paymentMethodConfigurationId, int currentHeight, boolean enlarged, boolean ready) {
            this.submitted = submitted;
            this.executingValidation = executingValidation;
            this.mobileSdkUrl = mobileSdkUrl;
            this.paymentMethodConfigurationId = paymentMethodConfigurationId;
            this.currentHeight = currentHeight;
            this.enlarged = enlarged;
            this.ready = ready;
        }


        protected PaymentFormState(Parcel in) {
            submitted = in.readByte() != 0;
            executingValidation = in.readByte() != 0;
            mobileSdkUrl = in.readParcelable(MobileSdkUrl.class.getClassLoader());
            paymentMethodConfigurationId = in.readLong();
            currentHeight = in.readInt();
            enlarged = in.readByte() != 0;
            ready = in.readByte() != 0;
        }

        public static final Creator<PaymentFormState> CREATOR = new Creator<PaymentFormState>() {
            @Override
            public PaymentFormState createFromParcel(Parcel in) {
                return new PaymentFormState(in);
            }

            @Override
            public PaymentFormState[] newArray(int size) {
                return new PaymentFormState[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeByte((byte) (submitted ? 1 : 0));
            dest.writeByte((byte) (executingValidation ? 1 : 0));
            dest.writeParcelable(mobileSdkUrl, flags);
            dest.writeLong(paymentMethodConfigurationId);
            dest.writeInt(currentHeight);
            dest.writeByte((byte) (enlarged ? 1 : 0));
            dest.writeByte((byte) (ready ? 1 : 0));
        }

        public boolean isSubmitted() {
            return submitted;
        }

        public boolean isExecutingValidation() {
            return executingValidation;
        }

        public MobileSdkUrl getMobileSdkUrl() {
            return mobileSdkUrl;
        }

        public long getPaymentMethodConfigurationId() {
            return paymentMethodConfigurationId;
        }

        public int getCurrentHeight() {
            return currentHeight;
        }

        public boolean isEnlarged() {
            return enlarged;
        }

        public boolean isReady() {
            return ready;
        }

        public static Creator<PaymentFormState> getCREATOR() {
            return CREATOR;
        }

    }
}
