package com.github.nprindle.refract.classes;

import com.github.nprindle.refract.d17n.A1;
import com.github.nprindle.refract.d17n.A2;
import com.github.nprindle.refract.d17n.K2;
import java.util.function.Function;

public interface Profunctor<Mu extends Profunctor.Mu, P extends K2> extends Constraint2<Mu, P> {
  public static interface Mu extends Constraint2.Mu {}

  public static <Mu extends Profunctor.Mu, P extends K2> Profunctor<Mu, P> resolve(
      final A1<Mu, P> p) {
    return (Profunctor<Mu, P>) p;
  }

  <A, B, C, D> A2<P, C, D> dimap(
      final Function<? super C, ? extends A> f,
      final Function<? super B, ? extends D> g,
      final A2<P, A, B> x);

  default <A, B, C> A2<P, C, B> lmap(
      final Function<? super C, ? extends A> f, final A2<P, A, B> x) {
    return dimap(f, y -> y, x);
  }

  default <A, B, D> A2<P, A, D> rmap(
      final Function<? super B, ? extends D> g, final A2<P, A, B> x) {
    return dimap(y -> y, g, x);
  }
}
