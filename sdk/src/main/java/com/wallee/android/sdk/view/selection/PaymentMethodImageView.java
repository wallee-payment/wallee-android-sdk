package com.wallee.android.sdk.view.selection;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.wallee.android.sdk.request.model.method.PaymentMethodIcon;

import java.util.Map;

/**
 * This view allows to display a {@link PaymentMethodIcon}.
 *
 * <p>The icon can have multiple formats including SVG. Additionally the merchant can upload custom
 * icons which we cannot test if they are going to be displayable within a regular image view. What
 * we can be relatively sure about is that the image is rendered nicely within a browser. As such we
 * use a web view to render the image.</p>
 */
public final class PaymentMethodImageView extends WebView {

    public PaymentMethodImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setIcon(PaymentMethodIcon icon) {
        if (icon != null) {
            // we have to do it this way because we cant control the size otherwise.
            String summary = "<html><body style=\"width:100%;height:100%;margin:0;padding:0\">" +
                    "<img style=\"width:100%;height:100%;margin:0;padding:0\" alt=\"tick\" src=\"data:" +
                    icon.getMimeType() + ";base64," + icon.getBase64Data() + "\"/></body></html>";
            super.loadData(summary, "text/html", null);
        }
        this.setBackgroundColor(0x00000000);
    }


    @Override
    public void setOnClickListener(@Nullable final OnClickListener l) {

        // The onclick event is not properly registered with a web view as such we use the onTouch
        // listener to get the same result.

        this.setOnTouchListener(new View.OnTouchListener() {

            public final static int FINGER_RELEASED = 0;
            public final static int FINGER_TOUCHED = 1;
            public final static int FINGER_DRAGGING = 2;
            public final static int FINGER_UNDEFINED = 3;

            private int fingerState = FINGER_RELEASED;


            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_UP:
                            l.onClick(view);
                        break;
                }

                return false;
            }
        });

    }

    @Override
    public final void loadData(String data, String mimeType, String encoding) {
        throw new UnsupportedOperationException("This method cannot be invoked in " +
                PaymentMethodImageView.class + ".");
    }

    @Override
    public final void loadUrl(String url) {
        throw new UnsupportedOperationException("This method cannot be invoked in " +
                PaymentMethodImageView.class + ".");
    }

    @Override
    public final void loadDataWithBaseURL(String baseUrl, String data, String mimeType, String encoding, String historyUrl) {
        throw new UnsupportedOperationException("This method cannot be invoked in " +
                PaymentMethodImageView.class + ".");
    }

    @Override
    public final void loadUrl(String url, Map<String, String> additionalHttpHeaders) {
        throw new UnsupportedOperationException("This method cannot be invoked in " +
                PaymentMethodImageView.class + ".");
    }

    @Override
    public final void setWebViewClient(WebViewClient client) {
        throw new UnsupportedOperationException("The web client cannot be overridden in the " +
                PaymentMethodImageView.class + ".");
    }
}
