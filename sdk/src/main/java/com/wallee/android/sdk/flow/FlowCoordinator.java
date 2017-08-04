package com.wallee.android.sdk.flow;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wallee.android.sdk.R;
import com.wallee.android.sdk.flow.config.FlowConfiguration;
import com.wallee.android.sdk.util.Check;
import com.wallee.android.sdk.view.PersistableView;

import java.util.concurrent.atomic.AtomicReference;

/**
 * The flow coordinator organizes the payment flow. This means it is responsible to trigger the
 * state and handlerView transitions between the different states and views.
 *
 * <p>The flow coordinator can be used within a {@link android.app.Fragment} or an {@link
 * android.app.Activity}. The coordinator expects to be informed about certain life cycle
 * events:</p>
 *
 * <ul>
 *
 * <li>When a handlerView should be shown the method {@link #onCreateView(LayoutInflater, ViewGroup,
 * Bundle)} has to be called. The method returns a {@link View} which can be added to the current
 * layout.</li>
 *
 * <li>When the current handlerView hierarchy is destructed the method {@link
 * #onSaveInstanceState(Bundle)} and {@link #onDestroy()} should be invoked. Eventually only one of
 * them has to be invoked.</li>
 *
 * </ul>
 *
 * <h2>View References</h2> <p>Further the coordinator may have references to {@link View} and as
 * such the coordinator should be dereferenced once the handlerView hierarchy is destroyed. The
 * coordinator ensures that the views which are created as part of state changes gets properly
 * dereferenced. However the coordinator must have always a reference to the current handlerView and
 * as such when this current handlerView is destroyed also the coordinator has to be destroyed.</p>
 *
 * <h2>Listeners</h2> <p>To register for events the coordinator accepts through it's {@link
 * FlowConfiguration} listeners. Candidates about which the coordinator will inform about are
 * subinterfaces of {@link com.wallee.android.sdk.flow.listener.FlowListener}.</p>
 *
 * <h2>Actions</h2> <p>The coordinator allows also to trigger certain actions from the outside. The
 * possible actions can be found {@link FlowAction}. They can be triggered through {@link
 * #triggerAction(FlowAction)}.</p>
 *
 * <h2>States / Persistence</h2> <p>The coordinator has an internal state this state will be
 * persisted through {@link #onSaveInstanceState(Bundle)}. The state and all relevant data is
 * attached to the bundle. It will automatically restore the state throught {@link
 * #onCreateView(LayoutInflater, ViewGroup, Bundle)}.</p>
 */
public final class FlowCoordinator {

    private static final String TAG = FlowCoordinator.class.getSimpleName();

    private static final String STATE_BUNDLE_KEY = FlowCoordinator.class.getCanonicalName() + "_state";

    private final FlowConfiguration configuration;
    private volatile View currentView;
    private final AtomicReference<ViewGroup> container = new AtomicReference<>();
    private final AtomicReference<FlowState> state = new AtomicReference<>();
    private View waitingView = null;
    private final Handler uiThreadHandler;
    private volatile StateChangeHandler stateChangeHandler;
    private final Object viewChangeLock = new Object();
    private volatile Parcelable stateArgument;

    /**
     * Constructor of the coordinator. This method is only initializing the required properties. The
     * method itself does not trigger anything.
     *
     * @param configuration the configuration to use for this coordinator.
     */
    public FlowCoordinator(FlowConfiguration configuration) {
        this.configuration = Check.requireNonNull(configuration, "The configuration is required.");
        this.state.set(FlowState.TOKEN_LOADING);
        this.uiThreadHandler = new Handler(Looper.getMainLooper());
    }

    /**
     * This method will trigger the {@code action} without actually executing it. This method allows
     * to test if the execution of a particular action will be executed or not. The method will
     * return {@code true} when the action can be executed.
     *
     * <p>The method givens an indication of an execution is possible at the moment or not. However
     * there will be no guarantee that the call of {@link #triggerAction(FlowAction)} will execute
     * the {@code action}. There is a small time frame between those calls and as such the inner
     * state may change in this time frame. So the action may be right now executable but a bit
     * later not anymore. As such this method should only be used as an indicator.</p>
     *
     * @param action the action which should be tested to be executed.
     * @return {@code true} when the given action can be executed otherwise {@code false}.
     */
    public boolean dryTriggerAction(FlowAction action) {
        StateChangeHandler handler = this.stateChangeHandler;
        View currentView = this.currentView;
        if (handler != null && currentView != null) {
            return handler.handler.dryTriggerAction(action, currentView);
        }
        return false;
    }

    /**
     * This method triggers the {@code action}. The action will affect the inner state of the
     * coordinator.
     *
     * <p>To test if a particular action may be executed the method {@link
     * #dryTriggerAction(FlowAction)} can be used. The method gives an indication if a particular
     * {@link FlowAction} can be triggered according to the current inner state of the
     * coordinator.</p>
     *
     * <p>The method will return {@code true} when the action has been executed. Otherwise the
     * method will return {@code false}.</p>
     *
     * @param action the action which should be triggered. See the action description what the
     *               action will trigger and the action can be triggered.
     * @return {@code true} when the action has been executed. Otherwise the method will return
     * {@code false}.
     */
    public boolean triggerAction(FlowAction action) {
        if (this.stateChangeHandler != null) {
            synchronized (this.viewChangeLock) {
                if (this.stateChangeHandler != null) {
                    return this.stateChangeHandler.handler.triggerAction(action, this.currentView);
                }
            }
        }
        return false;
    }

    /**
     * This method creates the handlerView which is relevant for the {@code savedInstanceState} of
     * the coordinator. When the {@code savedInstanceState} is null (which is the case when the
     * coordinator is created the first time for a particular flow) the default handlerView will be
     * loaded.
     *
     * <p>The method will return a container into which all other views will be rendered into. The
     * returned handlerView is not added to the {@code parent}. The parent is only used for layout
     * purposes.</p>
     *
     * <p>The intention is to call this method from within {@link android.app.Activity#onCreateView(String,
     * Context, AttributeSet)} resp. {@link android.app.Fragment#onCreateView(LayoutInflater,
     * ViewGroup, Bundle)}.</p>
     *
     * @param inflater           the inflater to use.
     * @param parent             the parent handlerView into which the returned handlerView will be
     *                           embedded into by the caller.
     * @param savedInstanceState the state which should be used to restore the handlerView.
     * @return the handlerView which should be inserted into the {@code parent}. The handlerView
     * returned is a container. Any modification applied will only occur within this handlerView and
     * not outside.
     */
    public View onCreateView(LayoutInflater inflater, ViewGroup parent,
                             final Bundle savedInstanceState) {
        ViewGroup container = (ViewGroup) inflater.inflate(R.layout.wallee_container, parent, false);
        if (this.container.compareAndSet(null, container)) {
            this.waitingView = inflater.inflate(R.layout.wallee_waiting_view, container, false);

            FlowStateHolder stateHolder = null;
            if (savedInstanceState != null) {
                stateHolder = savedInstanceState.getParcelable(STATE_BUNDLE_KEY);
                if (stateHolder == null) {
                    // This happens when the coordinator is not invoked properly.
                    Log.w(TAG, "The state could not be properly restored.");
                }
            }
            if (stateHolder == null) {
                stateHolder = new FlowStateHolder(FlowState.TOKEN_LOADING, null);
            }
            this.state.set(stateHolder.getState());

            // Creating a state change uiThreadHandler will move into this state and setup the handlerView.
            this.stateChangeHandler = new StateChangeHandler(this, stateHolder.getState(), stateHolder.getStateArgument(), savedInstanceState);

            if (this.currentView instanceof PersistableView && savedInstanceState != null) {
                ((PersistableView) currentView).restoreViewState(savedInstanceState);
            }
        }
        return this.container.get();
    }

    /**
     * This method switches draws the given {@code handlerView} to the container. The method will
     * execute the change within the main thread (UI thread).
     *
     * @param view the handlerView to set into the container.
     */
    private void changeToView(final View view) {
        if (this.container.get() != null) {
            this.uiThreadHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (container.get() != null) {
                        synchronized (viewChangeLock) {
                            if (currentView != view) {
                                container.get().removeAllViews();
                                container.get().addView(view);
                                currentView = view;
                            }
                        }
                    }
                }
            });
        }
    }


    /**
     * This method persists the current state of the coordinator into the given {@code outState}
     * bundle.
     *
     * <p>The intention is that this method is called from within {@link
     * android.app.Activity#onSaveInstanceState(Bundle)} resp. {@link android.app.Fragment#onSaveInstanceState(Bundle)}.</p>
     *
     * @param outState
     */
    public void onSaveInstanceState(Bundle outState) {
        // We synchronize on the handlerView lock here to make sure we really store a consistent state. In
        // case a handlerView change occur after invoking this method we cannot do much to prevent it.
        synchronized (this.viewChangeLock) {
            View view = this.currentView;
            outState.putParcelable(STATE_BUNDLE_KEY, new FlowStateHolder(this.state.get(), this.stateArgument));
            StateChangeHandler handler = this.stateChangeHandler;
            if (handler != null) {
                handler.onSaveInstanceState(outState);
            }
            Log.i(TAG, "The coordinator state has been saved: " + this.state.get());
        }
    }

    /**
     * This method destroys any link to the handlerView references etc. This method cleans the
     * memory up.
     *
     * <p>The intention is that this method is called from within {@link Activity#onDestroy()} resp.
     * {@link Fragment#onDestroy()}.</p>
     */
    public void onDestroy() {
        synchronized (this.viewChangeLock) {
            this.container.set(null);
            this.currentView = null;
            this.state.set(null);
            this.stateChangeHandler = null;
            this.waitingView = null;
        }
    }

    private static class StateChangeHandler implements CoordinatorCallback {

        private final FlowCoordinator coordinator;
        private final FlowState currentState;
        private volatile boolean stateChangeable = true;
        private final Parcelable stateArgument;
        private final FlowStateHandler handler;
        private final Object stateChangeLock = new Object();
        private final AtomicReference<View> handlerView = new AtomicReference<>();
        private final Bundle savedInstanceState;

        private StateChangeHandler(FlowCoordinator coordinator, FlowState targetState, Parcelable stateArgument, Bundle savedInstanceState) {
            this.savedInstanceState = savedInstanceState;
            this.coordinator = coordinator;
            this.currentState = targetState;
            this.stateArgument = stateArgument;
            this.coordinator.stateArgument = stateArgument;
            this.coordinator.changeToView(coordinator.waitingView);
            this.handler = targetState.createStateHandler(this, coordinator.configuration, this.stateArgument);
            this.handler.initialize();
            Log.d(TAG, "StateChangeHandler initialized with: " + this.toString());
        }

        private void onSaveInstanceState(Bundle outState) {
            View view = this.handlerView.get();
            if (view instanceof PersistableView) {
                ((PersistableView) view).saveViewState(outState);
            }
        }

        @Override
        public void waiting() {
            coordinator.uiThreadHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (stateChangeable) {
                        coordinator.changeToView(coordinator.waitingView);
                    }
                }
            });
        }

        @Override
        public void ready() {
            coordinator.uiThreadHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (stateChangeable) {
                        if (handlerView.get() == null) {
                            ViewGroup parent = coordinator.container.get();
                            if (parent != null) {
                                View view = handler.createView(parent);
                                if (view == null) {
                                    throw new IllegalStateException("The state handler for the " +
                                            "state " + currentState + " has set an null handlerView.");
                                }
                                if (handlerView.compareAndSet(null, view)) {
                                    if (savedInstanceState != null && view instanceof PersistableView) {
                                        ((PersistableView) view).restoreViewState(savedInstanceState);
                                    }
                                }
                            }
                        }
                        coordinator.changeToView(handlerView.get());
                    }
                }
            });
        }

        @Override
        public void changeStateTo(FlowState targetState, Parcelable stateArgument) {
            if (this.handler == null) {
                throw new IllegalStateException("A state change can be initialized once the state " +
                        "handler has been invoked through the initialize method.");
            }
            if (stateChangeable) {
                synchronized (stateChangeLock) {
                    if (stateChangeable) {
                        if (coordinator.state.compareAndSet(currentState, targetState)) {
                            this.stateChangeable = false;
                            coordinator.stateChangeHandler = new StateChangeHandler(this.coordinator,
                                    targetState, stateArgument, null);
                        } else {
                            throw new IllegalStateException("We expect that the state " +
                                    currentState + " is set but the state " + coordinator.state.get() +
                                    " was set. We expect that the state is only changed by this " +
                                    "method and as such somehow the state was changed from " +
                                    "the outside.");
                        }
                    } else {
                        Log.i(TAG, "The state change to " + targetState + " was provided when the state " +
                                "control was already delegated to another uiThreadHandler.");
                    }
                }
            } else {
                Log.i(TAG, "The state change to " + targetState + " was provided when the state " +
                        "control was already delegated to another uiThreadHandler.");
            }
        }

        @Override
        public String toString() {
            return "StateChangeHandler{" +
                    "coordinator=" + coordinator +
                    ", currentState=" + currentState +
                    ", stateChangeable=" + stateChangeable +
                    ", stateArgument=" + stateArgument +
                    ", handler=" + handler +
                    ", stateChangeLock=" + stateChangeLock +
                    ", handlerView=" + handlerView +
                    '}';
        }
    }


    /**
     * The flow state holder allows to serialize the {@link FlowState} and the argument of the state
     * into a {@link Parcelable}. This allows to persist it into a {@link android.os.Bundle}.
     */
    final static class FlowStateHolder implements Parcelable {

        private final FlowState state;
        private final Parcelable stateArgument;

        FlowStateHolder(FlowState state, Parcelable stateArgument) {
            this.state = state;
            this.stateArgument = stateArgument;
        }

        private FlowStateHolder(Parcel in) {
            this.state = (FlowState) in.readSerializable();
            this.stateArgument = in.readParcelable(Parcelable.class.getClassLoader());
        }

        public static final Creator<FlowStateHolder> CREATOR = new Creator<FlowStateHolder>() {
            @Override
            public FlowStateHolder createFromParcel(Parcel in) {
                return new FlowStateHolder(in);
            }

            @Override
            public FlowStateHolder[] newArray(int size) {
                return new FlowStateHolder[size];
            }
        };

        public FlowState getState() {
            return state;
        }

        public Parcelable getStateArgument() {
            return stateArgument;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeSerializable(this.state);
            dest.writeParcelable(stateArgument, flags);
        }
    }

}
