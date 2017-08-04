package com.wallee.android.sdk.util;


/**
 * Helper class to compare longs and integers.
 */
public final class CompareUtil {

    /**
     * Same as {@code Integer.compare(int, int)}.
     *
     * @param x the first number.
     * @param y the second number.
     * @return the resulting ordering.
     */
    public static int compare(long x, long y) {
        return (x < y) ? -1 : ((x == y) ? 0 : 1);
    }

    /**
     * Same as {@code Long.compare(long, long)}.
     *
     * @param x the first number.
     * @param y the second number.
     * @return the resulting ordering.
     */
    public static int compare(int x, int y) {
        return (x < y) ? -1 : ((x == y) ? 0 : 1);
    }

    private CompareUtil() {
        throw new IllegalAccessError();
    }

}
