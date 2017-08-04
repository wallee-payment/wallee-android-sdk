package com.wallee.android.sdk.util;

/**
 * Generic interface which represents a listener which accepts exactly one argument.
 *
 * @param <T> the type of the argument.
 */
public interface AsynchronousCallback<T> {

    /**
     * This method is invoked when the callback is ready.
     *
     * @param parameter the parameter which is passed along the callback.
     */
    void process(T parameter);

}
