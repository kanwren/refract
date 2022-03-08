package com.github.nprindle.refract.d17n;

/** The parent interface for typeclasses with kind {@code Type -> Constraint}. */
public interface C0<Mu extends C0.Mu, T> extends A1<Mu, T> {
  public static interface Mu extends K1 {}

  public static <T, Mu extends C0.Mu> C0<Mu, T> resolve(final A1<Mu, T> p) {
    return (C0<Mu, T>) p;
  }
}
