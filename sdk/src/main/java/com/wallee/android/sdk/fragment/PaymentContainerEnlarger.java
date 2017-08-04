package com.wallee.android.sdk.fragment;

/**
 * The payment container enlarger manages the resizing of the payment container.
 *
 * <p>During the payment process the payment container may requires to be enlarged and later again
 * reduced in its size. This is due to the fact that the payment container may load a complete web
 * site of a external payment provider. Those pages are typically more complex and requires more
 * space.</p>
 *
 * <p>The payment container enlarger manages the process of creating more space and reduce the given
 * space again.</p>
 */
public interface PaymentContainerEnlarger {

    /**
     * This method should create more space.
     *
     * <p>This method may be called from a background process. This means any changes applied on the
     * view hierarchy has to be run in the main loop.</p>
     *
     * <p>The method may also be called multiple times and in parallel. As such the implementor has
     * to consider some kind of locking strategy.</p>
     *
     * <p>The implementor has also to consider that the call of this method and {@link #restore()}
     * occurs in parallel and as such the transition between those two states have to be
     * atomic.</p>
     */
    void enlarge();

    /**
     * This method should restore the situation as it was before calling {@link #enlarge()} has been
     * invoked.
     *
     * <p>This method may be called from a background process. This means any changes applied on the
     * view hierarchy has to be run in the main loop.</p>
     *
     * <p>The method may also be called multiple times and in parallel. As such the implementor has
     * to consider some kind of locking strategy.</p>
     */
    void restore();
}
