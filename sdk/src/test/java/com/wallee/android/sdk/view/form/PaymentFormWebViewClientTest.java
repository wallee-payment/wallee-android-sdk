package com.wallee.android.sdk.view.form;

import com.wallee.android.sdk.util.HttpError;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.List;

/**
 * Tests {@link PaymentFormWebViewClient}
 */
@Config(manifest=Config.NONE)
@RunWith(RobolectricTestRunner.class)
public class PaymentFormWebViewClientTest {


    @Test
    public void testListenerMapping() {

        RecordingListener listener = new RecordingListener();
        PaymentFormWebViewClient client = new PaymentFormWebViewClient(listener);

        client.shouldOverrideUrlLoading(null, "https://localhost/mobile-sdk-callback/heightChangeCallback?data=1000");
        Assert.assertEquals(1000, listener.height);
        client.shouldOverrideUrlLoading(null, "https://localhost/mobile-sdk-callback/initializeCallback");
        Assert.assertEquals(true, listener.initialized);
        client.shouldOverrideUrlLoading(null, "https://localhost/mobile-sdk-callback/validationCallback?data=" + getValidationDataFailure());
        Assert.assertEquals(false, listener.successfulValidated);
        if(listener.validationErrors == null || listener.validationErrors.isEmpty()) {
            Assert.fail("No validation errors received.");
        }
        client.shouldOverrideUrlLoading(null, "https://localhost/mobile-sdk-callback/validationCallback?data=" + getValidationDataSuccess());
        Assert.assertEquals(true, listener.successfulValidated);

        client.shouldOverrideUrlLoading(null, "https://localhost/mobile-sdk-callback/awaitingFinalResultCallback?data=42");
        Assert.assertEquals(42, listener.awaitingFinalStateId);
        client.shouldOverrideUrlLoading(null, "https://localhost/mobile-sdk-callback/successCallback");
        Assert.assertEquals(true, listener.success);
        client.shouldOverrideUrlLoading(null, "https://localhost/mobile-sdk-callback/failureCallback");
        Assert.assertEquals(true, listener.failure);


    }

    private String getValidationDataFailure() {
        return "{success: false, errors: ['Message 1', 'Message 2']}";
    }

    private String getValidationDataSuccess() {
        return "{success: true, errors: []}";
    }

    private static class RecordingListener implements PaymentFormWebViewClient.CallbackListener {

        private boolean initialized = false;
        private int height = -1;
        private boolean enlarge = false;
        private boolean successfulValidated = false;
        private List<String> validationErrors = null;
        private long awaitingFinalStateId = -1;
        private boolean success = false;
        private boolean failure = false;

        @Override
        public void onInitialize() {
            this.initialized = true;
        }

        @Override
        public void onHeightChange(int targetHeight) {
            this.height = targetHeight;
        }

        @Override
        public void onEnlargeView() {
            this.enlarge = true;
        }

        @Override
        public void onValidationSuccess() {
            this.successfulValidated = true;
        }

        @Override
        public void onValidationFailure(List<String> messages) {
            this.validationErrors = messages;
        }

        @Override
        public void onAwaitingFinalStatus(long transactionId) {
            this.awaitingFinalStateId = transactionId;
        }

        @Override
        public void onSuccess() {
            this.success = true;
        }

        @Override
        public void onFailure() {
            this.failure = true;
        }

        @Override
        public void onHttpError(HttpError httpError) {

        }
    }

}
