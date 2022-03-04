package com.github.nprindle.refract.classes;

import com.github.nprindle.refract.d17n.A1;
import com.github.nprindle.refract.d17n.A2;
import com.github.nprindle.refract.d17n.K1;
import com.github.nprindle.refract.d17n.K2;
import java.util.function.Function;

public interface Traversing<P extends K2> extends Strong<P>, Choice<P> {
  <A, B, S, T> A2<P, S, T> wander(final Wander<S, T, A, B> wander, final A2<P, A, B> p);

  default <F extends K1, A, B> A2<P, A1<F, A>, A1<F, B>> traverseP(
      final Traversable<F> traversable, final A2<P, A, B> p) {
    return wander(traversable::traverse, p);
  }

  @FunctionalInterface
  public static interface Wander<S, T, A, B> {
    <F extends K1> A1<F, T> wander(
        final Applicative<F> applicative, final Function<A, A1<F, B>> f, S source);
  }
}
