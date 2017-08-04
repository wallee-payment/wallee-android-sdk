package com.wallee.android.sdk.flow;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

/**
 * The state handler represents a handler for {@link FlowState}. Each state has at least one
 * handler. The handler is responsible for triggering the state transitions and for managing the
 * underlying views.
 */
interface FlowStateHandler {

    /**
     * This method is invoked when the state handler can initialize the state and as such eventually
     * trigger a state change.
     *
     * <p>This method indicates to the flow state handler when the coordinator is onTokenSelectionViewReady to accept
     * state changes and view changes.</p>
     */
    void initialize();

    /**
     * This method is called to create the view which is shown to the user. The view will be added
     * to {@code container}. The method does not have to add the view to the container. This is done
     * by the {@link FlowCoordinator}.
     *
     * <p>The view is added by the coordinator to ensure that exactly one handler is writing to it
     * at the same moment!</p>
     *
     * <p>The method is called when the handler indicates that it reaches the onTokenSelectionViewReady state. Means the
     * method is called when {@link CoordinatorCallback#ready()} is invoked by this handler.</p>
     *
     * @param container the container to which the view will be added eventually.
     * @return the view which should be added.
     */
    View createView(ViewGroup container);

    /**
     * This method is called to test if a particular {@code action} can be executed with this
     * handler.
     *
     * <p>The handler should return {@code false} when it does not support the action. Otherwise it
     * should return {@code true}.</p>
     *
     * @param action the action which should be checked.
     * @return {@code true} when the handler supports the action in the current situation. Otherwise
     * the method should return {@code false}.
     */
    boolean dryTriggerAction(FlowAction action, View currentView);

    /**
     * This method is called to actually trigger the {@code action}.
     *
     * <p>The method can return {@code false} when the execution is not possible resp. the current
     * situation does not allow it. The behavior should be consistent to {@link
     * #dryTriggerAction(FlowAction, View)}.</p>
     *
     * @param action the action which should be executed.
     * @return {@code true} when the action was executed. Otherwise {@code false}.
     */
    boolean triggerAction(FlowAction action, View currentView);

}
