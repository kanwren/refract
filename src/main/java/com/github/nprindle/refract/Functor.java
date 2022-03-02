package com.github.nprindle.refract;

import java.util.function.Function;

// F is the Mu of the type the Functor is being implemented for, Mu is the Mu of
// the implementing dictionary class
public interface Functor<Mu extends Functor.Mu, F extends K1> extends Kind1<Mu, F> {
    /**
     * Mus in an instance dictionary should implement this interface.
     */
    interface Mu extends Kind1.Mu {}

    public static <Mu extends Functor.Mu, F extends K1> Functor<Mu, F> unbox(final A1<Mu, F> p) {
        return (Functor<Mu, F>) p;
    }

    <A, B> A1<F, B> map(final Function<? super A, ? extends B> f, final A1<F, A> x);
}
