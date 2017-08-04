package com.wallee.android.sdk.view.selection.token;

import android.view.View;
import android.view.ViewGroup;

import com.wallee.android.sdk.request.model.method.PaymentMethodConfiguration;
import com.wallee.android.sdk.request.model.token.TokenVersion;
import com.wallee.android.sdk.request.model.method.PaymentMethodIcon;
import com.wallee.android.sdk.view.ExpiringViewListener;

import java.util.List;
import java.util.Map;

/**
 * This factory is responsible to create the view which lists the available tokens.
 *
 * <p>The token list allows the user to select from a list of stored payment details (like card or a
 * PayPal account). The list is generated based up on the transaction and as such on the customer ID
 * set on the transaction.</p>
 */
public interface TokenListViewFactory {

    /**
     * This method is invoked to create a view which allows the user to select from a list of tokens
     * one which should be used to execute the payment.
     *
     * <p>The implementor does not need to insert the created view into the parent. This is done by
     * the caller of the method.</p>
     *
     * @param parent   the parent view into which the token list will be inserted into.
     * @param listener the listener which is listening on the selected token.
     * @param tokens   the list of tokens to display.
     * @param icons    the icons which can be used within the token list.
     * @return the view which should be inserted.
     */
    View build(ViewGroup parent, TokenViewListener listener, List<TokenVersion> tokens, Map<PaymentMethodConfiguration, PaymentMethodIcon> icons);

    /**
     * This listener is responsible for handling all events triggered from within the token list
     * view. The implementor can this way react on user actions.
     */
    interface TokenViewListener {

        /**
         * This method is called when a token has been selected by the user.
         *
         * @param token the token which has been selected by the user.
         */
        void onTokenClicked(TokenVersion token);
    }

}
