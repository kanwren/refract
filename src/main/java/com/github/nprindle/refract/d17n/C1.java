package com.github.nprindle.refract.d17n;

/**
 * The parent interface for typeclasses with kind {@code (Type -> Type) -> Constraint}.
 *
 * <p>Typeclasses would ideally be defunctionalized the same way as normal type, with an inner
 * {@code Mu} and extends {@code A1<Foo.Mu, F>}. However, this runs into issues with inheritance,
 * since an interface cannot extend {@code A1} with two different sets of arguments, and typeclasses
 * often inherit from supertypeclasses. Therefore, the {@code Mu} is parameterized into the generic
 * parameters, and the top of the hierarchy will extends {@code A1} with that {@code Mu}.
 */
public interface C1<Mu extends C1.Mu, F extends K1> extends A1<Mu, F> {
  public static interface Mu extends K1 {}

  public static <F extends K1, Mu extends C1.Mu> C1<Mu, F> resolve(final A1<Mu, F> p) {
    return (C1<Mu, F>) p;
  }
}
