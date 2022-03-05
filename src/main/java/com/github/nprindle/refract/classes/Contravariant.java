package com.github.nprindle.refract.classes;

import com.github.nprindle.refract.d17n.A1;
import com.github.nprindle.refract.d17n.K1;
import java.util.function.Function;

public interface Contravariant<Mu extends Contravariant.Mu, F extends K1>
    extends Constraint1<Mu, F> {
  public static interface Mu extends Constraint1.Mu {}

  public static <Mu extends Contravariant.Mu, F extends K1> Contravariant<Mu, F> resolve(
      final A1<Mu, F> p) {
    return (Contravariant<Mu, F>) p;
  }

  <A, B> A1<F, B> cmap(final Function<? extends B, ? super A> f, final A1<F, A> x);
}
