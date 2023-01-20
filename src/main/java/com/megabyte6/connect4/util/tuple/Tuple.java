package com.megabyte6.connect4.util.tuple;

public class Tuple {

    public static <A, B> Pair<A, B> of(A a, B b) {
        return new Pair<A, B>(a, b);
    }

    public static <A, B, C> Triplet<A, B, C> of(A a, B b, C c) {
        return new Triplet<A, B, C>(a, b, c);
    }

    public static <A, B, C, D> Quartet<A, B, C, D> of(A a, B b, C c, D d) {
        return new Quartet<A, B, C, D>(a, b, c, d);
    }

    public static <A, B, C, D, E> Quintet<A, B, C, D, E> of(A a, B b, C c, D d, E e) {
        return new Quintet<A, B, C, D, E>(a, b, c, d, e);
    }

}
