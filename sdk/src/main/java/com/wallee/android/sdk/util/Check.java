package com.wallee.android.sdk.util;

/**
 * Helper class which allows easily to check whether an argument is in the right shape.
 */
public final class Check {

    /**
     * Checks whether the given {@code obj} is {@code null}. If it is {@code null} it will throw an
     * {@link  IllegalArgumentException}.
     *
     * @param obj the object which should be checked.
     * @param <T> the type of the object.
     * @return the object which is guaranteed not {@code null}.
     */
    public static <T> T requireNonNull(T obj) {
        if (obj == null) {
            throw new IllegalArgumentException();
        }
        return obj;
    }

    /**
     * Checks whether the given {@code obj} is {@code null}. If it is {@code null} it will throw an
     * {@link  IllegalArgumentException}.
     *
     * @param obj     the object which should be checked.
     * @param <T>     the type of the object.
     * @param message the message which is thrown when the {@code obj} is null.
     * @return the object which is guaranteed not {@code null}.
     */
    public static <T> T requireNonNull(T obj, String message) {
        if (obj == null) {
            throw new IllegalArgumentException(message);
        }
        return obj;
    }

    /**
     * Checks if the given {@code obj} is not {@code null} and it is not empty.
     *
     * @param obj the object which should be checked.
     * @param message the message which is thrown when it is empty.
     * @return the string which is guaranteed not {@code null} and not empty.
     */
    public static String requireNonEmpty(String obj, String message) {
        if (obj == null || obj.isEmpty()) {
            throw new IllegalArgumentException(message);
        }
        return obj;
    }

    private Check() {
        throw new IllegalAccessError();
    }

}