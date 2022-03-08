package com.github.nprindle.refract.d17n;

/** The parent interface for typeclasses with kind {@code (Type -> Type -> Type) -> Constraint}. */
public interface C2<Mu extends C2.Mu, P extends K2> extends A1<Mu, P> {
  public static interface Mu extends K1 {}

  public static <P extends K2, Mu extends C2.Mu> C2<Mu, P> resolve(final A1<Mu, P> p) {
    return (C2<Mu, P>) p;
  }

  public static <P extends K2> C2<? extends C2.Mu, P> dict() {
    return new C2<C2.Mu, P>() {};
  }
}
