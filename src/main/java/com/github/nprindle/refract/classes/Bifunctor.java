package com.github.nprindle.refract.classes;

import com.github.nprindle.refract.d17n.A1;
import com.github.nprindle.refract.d17n.A2;
import com.github.nprindle.refract.d17n.K2;
import java.util.function.Function;

public interface Bifunctor<Mu extends Bifunctor.Mu, P extends K2> extends Constraint2<Mu, P> {
  public static interface Mu extends Constraint2.Mu {}

  public static <Mu extends Bifunctor.Mu, P extends K2> Bifunctor<Mu, P> resolve(
      final A1<Mu, P> p) {
    return (Bifunctor<Mu, P>) p;
  }

  <A, B, C, D> A2<P, C, D> bimap(
      final Function<? super A, ? extends C> f,
      final Function<? super B, ? extends D> g,
      final A2<P, A, B> x);

  default <A, B, C> A2<P, C, B> bfirst(
      final Function<? super A, ? extends C> f, final A2<P, A, B> x) {
    return bimap(f, y -> y, x);
  }

  default <A, B, D> A2<P, A, D> bsecond(
      final Function<? super B, ? extends D> g, final A2<P, A, B> x) {
    return bimap(y -> y, g, x);
  }
}
