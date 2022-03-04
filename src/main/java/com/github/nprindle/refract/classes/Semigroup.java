package com.github.nprindle.refract.classes;

public interface Semigroup<T> {
  T append(final T a, final T b);

  /**
   * The trivial semigroup whose binary operation always keeps the left-hand element and discards
   * the right.
   */
  static <T> Semigroup<T> first() {
    return (a, b) -> a;
  }

  /**
   * The trivial semigroup whose binary operation always keeps the right-hand element and discards
   * the left.
   */
  static <T> Semigroup<T> last() {
    return (a, b) -> b;
  }

  /** The dual of a semigroup, which swaps the arguments of {@code append}. */
  static <T> Semigroup<T> dual(final Semigroup<T> semigroup) {
    return (a, b) -> semigroup.append(b, a);
  }
}
