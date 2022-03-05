package com.github.nprindle.refract.classes;

import com.github.nprindle.refract.d17n.A1;
import com.github.nprindle.refract.d17n.K1;
import com.github.nprindle.refract.d17n.K2;

/** The parent interface for typeclasses with kind {@code (Type -> Type -> Type) -> Constraint}. */
public interface Constraint2<Mu extends Constraint2.Mu, P extends K2> extends A1<Mu, P> {
  public static interface Mu extends K1 {}

  public static <P extends K2, Mu extends Constraint2.Mu> Constraint2<Mu, P> resolve(
      final A1<Mu, P> p) {
    return (Constraint2<Mu, P>) p;
  }
}
