package com.github.nprindle.refract.classes;

import java.util.Optional;

public interface Monoid<T> extends Semigroup<T> {
  T empty();

  /**
   * Lift a semigroup into a monoid by adjoining an extra element (in this case, {@code
   * Optional.none()}) to the type to act as an identity.
   */
  static <T> Monoid<Optional<T>> fromSemigroup(final Semigroup<T> semigroup) {
    return new Monoid<Optional<T>>() {
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
  static <T> Monoid<Optional<T>> first() {
    return fromSemigroup(Semigroup.first());
  }

  /**
   * The trivial monoid whose binary operation always keeps the right-hand element and discards the
   * left.
   */
  static <T> Monoid<Optional<T>> last() {
    return fromSemigroup(Semigroup.last());
  }

  /** The dual of a monoid, which swaps the arguments of {@code append}. */
  static <T> Monoid<T> dual(final Monoid<T> monoid) {
    return new Monoid<T>() {
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
