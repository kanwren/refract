package com.github.nprindle.refract.classes;

import com.github.nprindle.refract.d17n.A1;
import com.github.nprindle.refract.d17n.A2;
import com.github.nprindle.refract.d17n.K1;
import com.github.nprindle.refract.d17n.K2;
import java.util.function.Function;

public interface Traversing<Mu extends Traversing.Mu, P extends K2>
    extends AffineTraversing<Mu, P> {
  public static interface Mu extends AffineTraversing.Mu {}

  public static <Mu extends Traversing.Mu, P extends K2> Traversing<Mu, P> resolve(
      final A1<Mu, P> p) {
    return (Traversing<Mu, P>) p;
  }

  <A, B, S, T> A2<P, S, T> wander(final Wander<S, T, A, B> wander, final A2<P, A, B> p);

  default <F extends K1, A, B> A2<P, A1<F, A>, A1<F, B>> traverseP(
      final Traversable<? extends Traversable.Mu, F> traversable, final A2<P, A, B> p) {
    return wander(traversable::traverse, p);
  }

  @FunctionalInterface
  public static interface Wander<S, T, A, B> {
    <F extends K1> A1<F, T> wander(
        final Applicative<? extends Applicative.Mu, F> applicative,
        final Function<A, A1<F, B>> f,
        final S source);
  }
}
