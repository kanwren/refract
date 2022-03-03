package com.github.nprindle.refract.data;

import com.github.nprindle.refract.d17n.A1;
import com.github.nprindle.refract.d17n.K1;
import com.github.nprindle.refract.classes.Functor;
import com.github.nprindle.refract.classes.Applicative;

import java.util.function.Function;

/**
 * The identity functor, which is isomorphic to the underlying type (modulo
 * subtyping in Java)
 */
public final class Identity<A> implements A1<Identity.M, A> {
    public static final class M implements K1 {}

    public static <A> Identity<A> unbox(final A1<Identity.M, A> p) {
        return (Identity<A>) p;
    }

    private final A value;

    public Identity(final A value) {
        this.value = value;
    }

    public A value() {
        return this.value;
    }

    /**
     * Extract the value from a fully-applied defunctionalization symbol.
     */
    public static <A> A get(final A1<Identity.M, A> p) {
        return Identity.unbox(p).value();
    }

    public static final class Instances {
        public static final Functor<Identity.M> functor() {
            return Identity.Instances.ApplicativeI.INSTANCE;
        }

        public static final Applicative<Identity.M> applicative() {
            return Identity.Instances.ApplicativeI.INSTANCE;
        }

        private static enum ApplicativeI implements Applicative<Identity.M>, Functor<Identity.M> {
            INSTANCE;

            @Override
            public <A, B> A1<Identity.M, B> map(final Function<? super A, ? extends B> f, final A1<Identity.M, A> x) {
                return new Identity<>(f.apply(Identity.get(x)));
            }

            @Override
            public <A> A1<Identity.M, A> pure(final A x) {
                return new Identity<>(x);
            }

            @Override
            public <A, B> A1<Identity.M, B> ap(final A1<Identity.M, Function<? super A, ? extends B>> f, final A1<Identity.M, A> x) {
                return new Identity<>(Identity.get(f).apply(Identity.get(x)));
            }
        }
    }
}
