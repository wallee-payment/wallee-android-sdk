package com.wallee.android.sample.request;

/**
 * Created by simonwalter on 27.07.17.
 */

public interface HTTPResponseListener<T> {
    void onResponse(T response);
}
