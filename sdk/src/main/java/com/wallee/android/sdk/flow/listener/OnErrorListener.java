package com.wallee.android.sdk.flow.listener;

/**
 * This interface joins multiple common error listeners together into one interface. This is only
 * for convenience.
 */
public interface OnErrorListener extends OnApiErrorListener, OnNetworkErrorListener, OnHttpErrorListener {

}
