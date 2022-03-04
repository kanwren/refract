package com.github.nprindle.refract.classes;

import com.github.nprindle.refract.d17n.A1;
import com.github.nprindle.refract.d17n.A2;
import com.github.nprindle.refract.d17n.K1;
import com.github.nprindle.refract.d17n.K2;
import java.util.function.Function;

// Closed omitted
public interface Mapping<P extends K2> extends Traversing<P> {
  <A, B, S, T> A2<P, S, T> roam(final Roam<S, T, A, B> roam, final A2<P, A, B> p);

  default <F extends K1, A, B> A2<P, A1<F, A>, A1<F, B>> mapP(
      final Functor<F> functor, final A2<P, A, B> p) {
    return roam(functor::map, p);
  }

  @FunctionalInterface
  public static interface Roam<S, T, A, B> {
    T roam(final Function<A, B> f, final S source);
  }
}
