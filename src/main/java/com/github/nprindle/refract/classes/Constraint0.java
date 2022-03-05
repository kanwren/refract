package com.github.nprindle.refract.classes;

import com.github.nprindle.refract.d17n.A1;
import com.github.nprindle.refract.d17n.K1;

/** The parent interface for typeclasses with kind {@code Type -> Constraint}. */
public interface Constraint0<Mu extends Constraint0.Mu, T> extends A1<Mu, T> {
  public static interface Mu extends K1 {}

  public static <T, Mu extends Constraint0.Mu> Constraint0<Mu, T> resolve(final A1<Mu, T> p) {
    return (Constraint0<Mu, T>) p;
  }
}
