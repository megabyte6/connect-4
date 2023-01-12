package com.megabyte6.connect4.util.tuple;

public class Tuple {

    public static <A> Unit<A> of(A a) {
        return new Unit<A>(a);
    }

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

    public static <A, B, C, D, E, F> Sextet<A, B, C, D, E, F> of(A a, B b, C c, D d, E e, F f) {
        return new Sextet<A, B, C, D, E, F>(a, b, c, d, e, f);
    }

    public static <A, B, C, D, E, F, G> Septet<A, B, C, D, E, F, G> of(A a, B b, C c, D d, E e, F f, G g) {
        return new Septet<A, B, C, D, E, F, G>(a, b, c, d, e, f, g);
    }

    public static <A, B, C, D, E, F, G, H> Octet<A, B, C, D, E, F, G, H> of(A a, B b, C c, D d, E e, F f, G g, H h) {
        return new Octet<A, B, C, D, E, F, G, H>(a, b, c, d, e, f, g, h);
    }

    public static <A, B, C, D, E, F, G, H, I> Ennead<A, B, C, D, E, F, G, H, I> of(
            A a, B b, C c, D d, E e, F f, G g, H h, I i) {
        return new Ennead<A, B, C, D, E, F, G, H, I>(a, b, c, d, e, f, g, h, i);
    }

    public static <A, B, C, D, E, F, G, H, I, J> Decade<A, B, C, D, E, F, G, H, I, J> of(
            A a, B b, C c, D d, E e, F f, G g, H h, I i, J j) {
        return new Decade<A, B, C, D, E, F, G, H, I, J>(a, b, c, d, e, f, g, h, i, j);
    }

}
