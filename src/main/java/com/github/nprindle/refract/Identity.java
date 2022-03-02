package com.github.nprindle.refract;

import java.util.function.Function;

public final class Identity<A> implements A1<Identity.Mu, A> {
    public static final class Mu implements K1 {}

    public static <A> Identity<A> unbox(final A1<Identity.Mu, A> p) {
        return (Identity<A>) p;
    }

    private final A value;

    public Identity(final A value) {
        this.value = value;
    }

    public A value() {
        return this.value;
    }

    public static <A> A get(final A1<Identity.Mu, A> p) {
        return Identity.unbox(p).value();
    }

    public enum FunctorI implements Functor<FunctorI.Mu, Identity.Mu> {
        INSTANCE;

        public static final class Mu implements Functor.Mu {}

        @Override
        public <A, B> A1<Identity.Mu, B> map(final Function<? super A, ? extends B> f, final A1<Identity.Mu, A> x) {
            return new Identity(f.apply(Identity.get(x)));
        }
    }
}
