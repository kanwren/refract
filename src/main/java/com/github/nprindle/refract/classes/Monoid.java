package com.github.nprindle.refract.classes;

import com.github.nprindle.refract.d17n.A1;
import java.util.Optional;

public interface Monoid<Mu extends Monoid.Mu, T> extends Semigroup<Mu, T> {
  public static interface Mu extends Semigroup.Mu {}

  public static <Mu extends Monoid.Mu, T> Monoid<Mu, T> resolve(final A1<Mu, T> p) {
    return (Monoid<Mu, T>) p;
  }

  T empty();

  /**
   * Lift a semigroup into a monoid by adjoining an extra element (in this case, {@code
   * Optional.none()}) to the type to act as an identity.
   */
  static <T> Monoid<? extends Monoid.Mu, Optional<T>> fromSemigroup(
      final Semigroup<? extends Semigroup.Mu, T> semigroup) {
    return new Monoid<Monoid.Mu, Optional<T>>() {
      @Override
      public Optional<T> empty() {
        return Optional.empty();
      }

      @Override
      public Optional<T> append(Optional<T> ma, Optional<T> mb) {
        return ma.map(a -> Optional.of(mb.map(b -> semigroup.append(a, b)).orElse(a))).orElse(mb);
      }
    };
  }

  /**
   * The trivial monoid whose binary operation always keeps the left-hand element and discards the
   * right.
   */
  static <T> Monoid<? extends Monoid.Mu, Optional<T>> first() {
    return fromSemigroup(Semigroup.first());
  }

  /**
   * The trivial monoid whose binary operation always keeps the right-hand element and discards the
   * left.
   */
  static <T> Monoid<? extends Monoid.Mu, Optional<T>> last() {
    return fromSemigroup(Semigroup.last());
  }

  /** The dual of a monoid, which swaps the arguments of {@code append}. */
  static <T> Monoid<? extends Monoid.Mu, T> dual(final Monoid<? extends Monoid.Mu, T> monoid) {
    return new Monoid<Monoid.Mu, T>() {
      @Override
      public T empty() {
        return monoid.empty();
      }

      @Override
      public T append(T a, T b) {
        return monoid.append(b, a);
      }
    };
  }
}
