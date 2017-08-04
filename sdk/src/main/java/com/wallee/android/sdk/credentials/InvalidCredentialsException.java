package com.wallee.android.sdk.credentials;

/**
 * This exception is thrown when the {@link Credentials} are invalid.
 */
public class InvalidCredentialsException extends RuntimeException {

    /**
     * Constructor.
     *
     * @param message the message which indicates why the credentials are invalid.
     */
    public InvalidCredentialsException(String message) {
        super(message);
    }

}
