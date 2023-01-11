package com.megabyte6.connect4.util;

public class Range {

    /**
     * Creates an {@code int[]} of all integers from 0 to {@code to}. One
     * common use case is in for-each loops to offer nicer syntactical sugar
     * that many other languages provide.
     * 
     * @param to   Ending value (exclusive).
     * @return     An {@code int[]} of integers to loop through.
     */
    public static int[] range(int to) {
        return range(0, to);
    }

    /**
     * Creates an {@code int[]} of all integers from {@code from} to
     * {@code to}. One common use case is in for-each loops to offer nicer
     * syntactical sugar that many other languages provide.
     * 
     * @param from Starting value (inclusive).
     * @param to   Ending value (exclusive).
     * @return     An {@code int[]} of integers to loop through.
     */
    public static int[] range(int from, int to) {
        if (from == to)
            return new int[] {};

        int[] range = new int[Math.abs(to - from)];
        for (int i = 0; i < range.length; i++) {
            if (from > to) {
                range[i] = to - i;
            } else {
                range[i] = from + i;
            }
        }

        return range;
    }

}
