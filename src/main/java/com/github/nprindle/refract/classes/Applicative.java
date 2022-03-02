package com.github.nprindle.refract.classes;

import com.github.nprindle.refract.d17n.A1;
import com.github.nprindle.refract.d17n.K1;

import java.util.function.Function;
import java.util.function.BiFunction;

/**
 * Minimal complete definition: <code>pure, ap</code>
 */
public interface Applicative<F extends K1> extends Functor<F> {
    <A> A1<F, A> pure(final A x);

    <A, B> A1<F, B> ap(final A1<F, Function<? super A, ? extends B>> f, final A1<F, A> x);
    // TODO: allow defining lift2 instead of ap
    // {
    //     final BiFunction<A1<F, Function<? super A, ? extends B>>, A1<F, A>, A1<F, B>> lifted = lift2((f2, x2) -> f2.apply(x2));
    //     return lifted.apply(f, x);
    // }

    default <A, B, C> BiFunction<A1<F, A>, A1<F, B>, A1<F, C>> lift2(final BiFunction<? super A, ? super B, ? extends C> f) {
        return (x, y) -> apply2(f, x, y);
    }

    default <A, B, C> A1<F, C> apply2(final BiFunction<? super A, ? super B, ? extends C> f, final A1<F, A> x, final A1<F, B> y) {
        return ap(map(x2 -> y2 -> f.apply(x2, y2), x), y);
    }
}
