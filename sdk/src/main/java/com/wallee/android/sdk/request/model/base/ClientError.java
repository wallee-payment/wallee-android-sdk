package com.wallee.android.sdk.request.model.base;

/**
 * Created by simonwalter on 31.07.17.
 */

public class ClientError {
    private String date;
    private String defaultMessage;
    private String id;
    private String message;
    private ClientErrorType type;

    public String getDate() {
        return date;
    }

    public String getDefaultMessage() {
        return defaultMessage;
    }

    public String getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    public ClientErrorType getType() {
        return type;
    }

    @Override
    public String toString() {
        return this.getClass().getCanonicalName() + " (" + type.name() + "): " + message + " \n" +
                "(id: " + id + ", date: " + date + ")";
    }
}
