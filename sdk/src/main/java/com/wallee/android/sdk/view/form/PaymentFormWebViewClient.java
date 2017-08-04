package com.wallee.android.sdk.view.form;

import android.net.Uri;
import android.util.Log;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.google.gson.Gson;
import com.wallee.android.sdk.util.Check;
import com.wallee.android.sdk.util.HttpError;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * This is the implementation of the {@link WebViewClient} which handles the callbacks invoked from
 * within the {@link DefaultPaymentFormView}.
 */
final class PaymentFormWebViewClient extends WebViewClient {

    private final String TAG = PaymentFormWebViewClient.class.getCanonicalName();

    private final static String URL_PROTOCOL_HTTP = "https://localhost/mobile-sdk-callback/";

    private final CallbackListener listener;

    PaymentFormWebViewClient(CallbackListener listener) {
        this.listener = Check.requireNonNull(listener, "The listener is required.");
    }

    @Override
    public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
        if (this.triggerCallback(url)) {
            try {
                return new WebResourceResponse("text/html", "UTF-8", new ByteArrayInputStream("".getBytes("UTF-8")));
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
        } else {
            return null;
        }
    }

    private boolean triggerCallback(String url) {
        final String callbackName = extractCallbackName(url);
        if (callbackName != null) {
            Uri uri = Uri.parse(url);
            if (callbackName == null || callbackName.isEmpty()) {
                // The URL seems strange. We should not continue because it is clearly a not
                // meaningful URL and as such we should stop here.
                Log.e(TAG, "The callback name (host) is empty which should not be the case. We " +
                        "ignore it and continue.");
            } else {
                Callback callback = Callback.getByName(callbackName);
                if (callback != null) {
                    // We have triggered the listener and as such we do not want to continue here.
                    callback.invokeListener(uri, this.listener);
                } else {
                    // We did not found a callback. However it is clearly a callback which is from
                    // us. Therefore we stop here and ignore it, but we log it.
                    Log.e(TAG, "The callback name (host) is not known. We ignore it and continue. " +
                            "Callback Name: " + callbackName);
                }
            }
            return true;
        }
        else {
            return false;
        }
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        if (triggerCallback(url)) {
            // A callback was triggered.
           return true;
        } else {
            // When some other URL is invoked the user has been forwarded to a separate page
            // and as such we request to onEnlargeView the view.
            this.listener.onEnlargeView();
            return false;
        }
    }

    private String extractCallbackName(String url) {
        if (url.startsWith(URL_PROTOCOL_HTTP)) {
            final String callbackPart = url.substring(URL_PROTOCOL_HTTP.length());
            int queryStart = callbackPart.indexOf('?');
            if (queryStart > 0) {
                return callbackPart.substring(0, queryStart);
            } else {
                return callbackPart;
            }
        } else {
            return null;
        }
    }

    public void onReceivedError(WebView view, int errorCode,
                                String description, String failingUrl) {
        this.listener.onHttpError(new HttpError(failingUrl, description, errorCode));
    }

    /**
     * The callback helps to map the URL identifiers to the actual functionality in the code. The
     * callbacks are implemented according to <a href="https://app-wallee.com/en/doc/payment/mobile-sdk-web-service-api#_callbacks">callback
     * definitions</a>.
     */
    private enum Callback {

        INITIALIZE("initializeCallback") {
            @Override
            void invokeListener(Uri url, CallbackListener listener) {
                listener.onInitialize();
            }
        },
        HEIGHT_CHANGE("heightChangeCallback") {
            @Override
            void invokeListener(Uri url, CallbackListener listener) {
                try {
                    listener.onHeightChange(Integer.parseInt(readDataParameter(url)));
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("The callback should contain an integer " +
                            "with the desired height of the view. However it seems as it is not " +
                            "a valid number. Callback: " + url, e);
                }
            }
        },
        VALIDATION("validationCallback") {
            @Override
            void invokeListener(Uri url, CallbackListener listener) {
                String data = readDataParameter(url);
                Gson gson = new Gson();
                ValidationResult result = gson.fromJson(data, ValidationResult.class);
                if (result.isSuccess()) {
                    listener.onValidationSuccess();
                } else {
                    listener.onValidationFailure(result.getErrors());
                }

            }
        },
        AWAITING_FINAL_RESULT("awaitingFinalResultCallback") {
            @Override
            void invokeListener(Uri url, CallbackListener listener) {
                String id = readDataParameter(url);
                listener.onAwaitingFinalStatus(Long.parseLong(id));
            }
        },
        SUCCESS("successCallback") {
            @Override
            void invokeListener(Uri url, CallbackListener listener) {
                listener.onSuccess();
            }
        },
        FAILED("failureCallback") {
            @Override
            void invokeListener(Uri url, CallbackListener listener) {
                listener.onFailure();
            }
        },;
        private final String name;

        private static final Map<String, Callback> BY_NAME;

        static {
            Map<String, Callback> map = new HashMap<>();
            for (Callback callback : values()) {
                map.put(callback.name.toLowerCase(Locale.ROOT), callback);
            }
            BY_NAME = Collections.unmodifiableMap(map);
        }

        Callback(String name) {
            this.name = name;
        }

        static Callback getByName(String name) {
            Check.requireNonEmpty(name, "The name is required.");
            return BY_NAME.get(name.toLowerCase(Locale.ROOT));
        }

        abstract void invokeListener(Uri url, CallbackListener listener);

        protected String readDataParameter(Uri url) {
            String data = url.getQueryParameter("data");
            if (data == null || data.isEmpty()) {
                throw new IllegalArgumentException("The callback does not contain a data " +
                        "object even the contract says it should. Callback: " + url);
            }
            return data;
        }

    }


    interface CallbackListener {

        void onInitialize();

        void onHeightChange(int targetHeight);

        void onEnlargeView();

        void onValidationSuccess();

        void onValidationFailure(List<String> messages);

        void onAwaitingFinalStatus(long transactionId);

        void onSuccess();

        void onFailure();

        void onHttpError(HttpError error);

    }

    private static class ValidationResult {
        private boolean success;
        private List<String> errors;

        public boolean isSuccess() {
            return success;
        }

        public List<String> getErrors() {
            return errors;
        }
    }
}
