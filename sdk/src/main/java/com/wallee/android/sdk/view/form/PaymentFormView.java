package com.wallee.android.sdk.view.form;


import android.view.View;

import com.wallee.android.sdk.request.model.transaction.Transaction;
import com.wallee.android.sdk.util.HttpError;
import com.wallee.android.sdk.view.ExpiringViewListener;
import com.wallee.android.sdk.view.LoadingViewListener;
import com.wallee.android.sdk.view.PersistableView;

import java.util.List;

/**
 * The payment form view is responsible for loading and triggering the form which collects the
 * payment details such as card numbers etc. The form is implemented in a generic way so it does not
 * matter what kind of payment method it is. The form has to handle the specifics of a particular
 * payment method. To achieve this generic implementation a {@link android.webkit.WebView} is used
 * which loads the form from a remote server.
 *
 * <p>The view is also responsible for handling redirects to external payment processors such as
 * online banking service or any other service which requires the user to enter information details
 * on a dedicated web site. Therefore the {@link Listener#onEnlargeView(View)} will be triggered
 * when more space is required. Further the view is also responsible to resize itself to the size
 * which is required by the form within the view. So that the view will not require within it a
 * scrollbar. If the form does not have enough space the view within which this view is placed in
 * should add scrollbars.</p>
 *
 * <p>The styling of the form itself can be done through the backend of wallee. There is a resource
 * editor which allows to adjust the layout to the required styling of the app.</p>
 */
public interface PaymentFormView extends PersistableView {

    /**
     * The height of the view may change depending on the user input. The target height indicates
     * how height the view should be means this is the calculated height which is expected based up
     * on the content of the view. The actual view height may be slightly different.
     *
     * @return the current target height in dp.
     */
    int getCurrentTargetHeight();

    /**
     * The form can be submitted through {@link #submit()}. The method will return {@code false} as
     * long as the submit method was not invoked.
     *
     * @return {@code true} when the form within the view has been submitted.
     */
    boolean isSubmitted();

    /**
     * This method validates the form content. This may trigger some network communication. The
     * result of the validation will be communicated through the {@link Listener}.
     *
     * <p>This method should be invoked before {@link #submit()} to give the user a chance to fix
     * the input.</p>
     *
     * <p>If there are inputs which are not valid (for example wrong format) the corresponding
     * fields get highlighted and an error message is shown along the field.</p>
     */
    void validate();

    /**
     * This method submits the form content. This may trigger further page loads. The result of the
     * submit will be communicated through the {@link Listener}.
     */
    void submit();

    /**
     * The listener is used to communicate back to the invoker of the view what is going on within
     * the view. The caller may want to adjust certain things according to the events fired. For
     * example the caller should add a submit button which triggers the submission of the form once
     * the form has been loaded.
     *
     * <p>The form may expire as such it extends {@link ExpiringViewListener}. The implementor may
     * want to reload the form in this situation.</p>
     *
     * <p>The method invocation of the listener may or may not occur in the main loop. As such the
     * implementor may want to delegate changes on the views triggered by this listener into the
     * main loop.</p>
     *
     * @param <T> the type of the view.
     */
    interface Listener<T extends View & PaymentFormView> extends LoadingViewListener<T>,
            ExpiringViewListener<T> {

        /**
         * This method is invoked when there occurred a HTTP loading during the loading of the
         * {@link android.webkit.WebView}.
         *
         * @param view  the view which is responsible for the error.
         * @param error the error object which gives more details about the error.
         */
        void onHttpError(T view, HttpError error);


        /**
         * This method is called when the view should be enlarged. This means that we are going to
         * load some content which requires eventually a lot more space. This happens for example
         * when we forward the user to a 3-D secure page or another external service.
         *
         * <p>The implementor has to make sure that the adjustment is done right away. It is also no
         * option to load the URL assigned to the {@code currentView} in a new WebView. The process
         * has to continue within the {@code currentView}.</p>
         *
         * @param currentView the view object which requests the adjustment.
         */
        void onEnlargeView(T currentView);

        /**
         * This method is called when the enlarged screen is not required anymore. This happens
         * typically when the transaction has been completed. So this event is typically fired
         * before {@link #onSuccess(View)}, {@link #onFailure(View)} or {@link
         * #onAwaitingFinalState(View)}.
         *
         * @param currentView the view object which holds the form.
         */
        void onResetView(T currentView);

        /**
         * This method is invoked when the invocation of {@link #validate()} was successful.
         *
         * <p>The implementor may us this event to {@link #submit()} the form. The submission be
         * also postponed and the user can be ask to execute further tasks before submitting the
         * form.</p>
         *
         * @param currentView the current view object which was successfully validated.
         */
        void onValidationSuccess(T currentView);

        /**
         * This method is invoked when the the invocation of {@link #validate()} failed.
         *
         * <p>The implementor may want to give the user the chance to adjust the input. The {@code
         * errors} may contain additional messages which can be shown to the user.</p>
         *
         * @param currentView the current view which failed to validate successfully.
         * @param errors      the list of errors which may give additional information to the user
         *                    about the validation failure.
         */
        void onValidationFailure(T currentView, List<String> errors);

        /**
         * This method is invoked when the transaction has been completed successfully.
         *
         * <p>The invocation of this method is no guarantee that the transaction is really
         * successful. The user may have tampered it. As such this event should be only used to
         * adjust the UI but not to change the merchant backend database. This update should occur
         * through a webhook sent through the wallee platform. Further this method is invoked when
         * at least the authorization was successful. This does not mean that the money is already
         * transferred. For example with bank transfer this can take several days.</p>
         *
         * <p>The implementor may want to remove the view and show some success message.</p>
         *
         * @param currentView the view which has been completed successfully.
         */
        void onSuccess(T currentView);

        /**
         * This method is invoked when the transaction has failed.
         *
         * <p>The transaction fail because of different reasons. The user was not able to complete
         * the payment or the transaction was refused by the processor etc. The {@link
         * Transaction#getUserFailureMessage()} may provide more details for the user about the
         * failure reason.</p>
         *
         * @param currentView the view which contains the form with the failed transaction.
         */
        void onFailure(T currentView);

        /**
         * This method is invoked when the user has completed the payment process but the
         * transaction has neither a {@link #onSuccess(View)} nor {@link #onFailure(View)} state
         * reached.
         *
         * <p>This can happen when the payment processor requires some time to find out what is
         * going on with the transaction. This delays can take up several minutes. However if we end
         * up here normally reaching a final result will take a few seconds.</p>
         *
         * <p>The implementor may present some screen to the user which explains the situation and
         * ask for patient. Additionally the implementor should implement a pooling strategy by
         * which the web service API is asked for the latest transaction object. This object can be
         * used to determine if the transaction has reached a final state. It is recommended to
         * increase the interval over time.</p>
         *
         * @param currentView the view which was not able to complete immediately.
         */
        void onAwaitingFinalState(T currentView);

    }

}
