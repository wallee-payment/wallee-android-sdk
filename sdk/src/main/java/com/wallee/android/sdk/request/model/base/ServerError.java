package com.wallee.android.sdk.request.model.base;

/**
 * Created by simonwalter on 31.07.17.
 */

public class ServerError {
    private String date;
    private String id;
    private String message;

    public String getDate() {
        return date;
    }

    public String getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return this.getClass().getCanonicalName() + ": " + message + " \n" +
                "( id: " + id + "date: " + date + " )";
    }
}
