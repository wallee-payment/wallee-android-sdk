package com.wallee.android.sdk.flow;

import android.os.Parcelable;

/**
 * The coordinator callback allows to trigger certain operations on the coordinator.
 *
 * <p>This is the way the {@link FlowStateHandler} communicates with the {@link
 * FlowCoordinator}.</p>
 *
 * <p>This interface is intentionally package protected to prevent using it by other classes to
 * trigger something.</p>
 */
interface CoordinatorCallback {

    /**
     * This triggers a state change to the {@code targetState}. The {@code argument} is passed along
     * to the new state.
     *
     * @param targetState the state to which the coordinator should switch to.
     * @param argument    the argument which is passed to the new {@link FlowStateHandler}.
     */
    void changeStateTo(FlowState targetState, Parcelable argument);

    /**
     * To indicate that the view provided to the {@link FlowCoordinator} can be shown.
     */
    void ready();

    /**
     * By calling this method the {@link FlowCoordinator} will show the waiting view.
     */
    void waiting();

}
