package com.github.nprindle.refract.classes;

import com.github.nprindle.refract.d17n.A1;
import com.github.nprindle.refract.d17n.C1;
import com.github.nprindle.refract.d17n.K1;
import java.util.function.Function;

public interface Foldable<Mu extends Foldable.Mu, T extends K1> extends C1<Mu, T> {
  public static interface Mu extends C1.Mu {}

  public static <Mu extends Foldable.Mu, T extends K1> Foldable<Mu, T> resolve(final A1<Mu, T> p) {
    return (Foldable<Mu, T>) p;
  }

  <M, A> M foldMap(
      final Monoid<? extends Monoid.Mu, M> monoid,
      final Function<? super A, ? extends M> f,
      final A1<T, A> x);
}
