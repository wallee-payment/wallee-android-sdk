package com.wallee.android.sdk.fragment;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * This implementation of {@link PaymentContainerEnlarger} traverses the view hierarchy up until the
 * {@link Activity}'s base container has been reached and remove all views along the hierarchy.
 *
 * <p>The result is that the payment container spans over the whole screen. During restoring all
 * views will be set back to the visibility they had before and also the size will be the same as
 * before.</p>
 *
 * <p>This implementation of a {@link PaymentContainerEnlarger} is quit aggressive and may lead to
 * some issues in particular cases. As such this is a good default but a custom implementation may
 * achieve better results.</p>
 */
public final class HierarchicalPaymentContainerEnlarger implements PaymentContainerEnlarger {

    private final View viewToEnlarge;
    private final View container;
    private final Object lock = new Object();
    private final Set<View> keepingViews = new HashSet<>();
    private final Map<View, Integer> viewVisibilities = new HashMap<>();
    private final Map<View, LayoutSizeParams> enlargedViewLayoutParameters = new HashMap<>();
    private volatile boolean expanded = false;
    private final Handler uiThreadHandler;

    public HierarchicalPaymentContainerEnlarger(View viewToEnlarge, Activity activity) {
        this(viewToEnlarge, ((ViewGroup) activity
                .findViewById(android.R.id.content)).getChildAt(0));
    }

    public HierarchicalPaymentContainerEnlarger(View viewToEnlarge, View container) {
        this.viewToEnlarge = viewToEnlarge;
        this.container = container;
        this.uiThreadHandler = new Handler(Looper.getMainLooper());
    }

    @Override
    public void enlarge() {
        this.uiThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                if (!expanded) {
                    synchronized (lock) {
                        if (!expanded) {
                            expandOneLevel(viewToEnlarge);
                            expanded = true;
                        }
                    }
                }
            }
        });
    }

    @Override
    public void restore() {
        this.uiThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                if (expanded) {
                    synchronized (lock) {
                        if (expanded) {
                            runRestore();
                        }
                    }
                }
            }
        });

    }

    private void expandOneLevel(View view) {
        if (view != container) {
            this.keepingViews.add(view);
            ViewGroup.LayoutParams params = view.getLayoutParams();
            enlargedViewLayoutParameters.put(view, new LayoutSizeParams(params));
            params.height = ViewGroup.LayoutParams.MATCH_PARENT;
            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
            view.setLayoutParams(params);

            ViewParent parent = view.getParent();
            if (parent instanceof ViewGroup) {
                ViewGroup group = (ViewGroup) parent;
                for (int i = 0; i < group.getChildCount(); i++) {
                    View child = group.getChildAt(i);
                    if (!this.keepingViews.contains(child) && child.getVisibility() != View.GONE) {
                        viewVisibilities.put(child, child.getVisibility());
                        child.setVisibility(View.GONE);
                    }
                }
                expandOneLevel(group);
            }
        }
    }

    private void runRestore() {
        for (View view : this.keepingViews) {
            LayoutSizeParams storedParams = enlargedViewLayoutParameters.get(view);
            ViewGroup.LayoutParams params = view.getLayoutParams();
            storedParams.applyOn(params);
            view.setLayoutParams(params);

            if (view instanceof ViewGroup) {
                ViewGroup group = (ViewGroup) view;
                for (int i = 0; i < group.getChildCount(); i++) {
                    View child = group.getChildAt(i);
                    Integer targetVisibility = this.viewVisibilities.get(child);
                    if (targetVisibility != null) {
                        if (targetVisibility.intValue() == View.GONE) {
                            child.setVisibility(View.GONE);
                        } else if (targetVisibility.intValue() == View.VISIBLE) {
                            child.setVisibility(View.VISIBLE);
                        } else if (targetVisibility.intValue() == View.INVISIBLE) {
                            child.setVisibility(View.INVISIBLE);
                        }
                    }
                }
            }
        }
        this.expanded = false;
    }

    private static class LayoutSizeParams {
        private final int width;
        private final int height;

        private LayoutSizeParams(ViewGroup.LayoutParams params) {
            this.width = params.width;
            this.height = params.height;
        }

        private LayoutSizeParams(int width, int height) {
            this.width = width;
            this.height = height;
        }

        private void applyOn(ViewGroup.LayoutParams params) {
            params.height = this.height;
            params.width = this.width;
        }
    }

}
