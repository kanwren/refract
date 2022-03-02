package com.github.nprindle.refract;

public interface Kind1<Mu extends Kind1.Mu, F extends K1> extends A1<Mu, F> {
    public static <Mu extends Kind1.Mu, F extends K1> Kind1<Mu, F> unbox(final A1<Mu, F> p) {
        return (Kind1<Mu, F>) p;
    }

    interface Mu extends K1 {}
}
