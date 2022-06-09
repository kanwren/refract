package com.github.nprindle.refract.classes;

import com.github.nprindle.refract.d17n.A1;
import com.github.nprindle.refract.d17n.C0;
import com.github.nprindle.refract.data.Lazy;

public interface Semigroup<Mu extends Semigroup.Mu, T> extends C0<Mu, T> {
  public static interface Mu extends C0.Mu {}

  public static <Mu extends Semigroup.Mu, T> Semigroup<Mu, T> resolve(final A1<Mu, T> p) {
    return (Semigroup<Mu, T>) p;
  }

  T append(final T a, final T b);

  /**
   * The trivial semigroup whose binary operation always keeps the left-hand element and discards
   * the right.
   */
  static <T> Semigroup<? extends Semigroup.Mu, T> first() {
    return (a, b) -> a;
  }

  /**
   * The trivial semigroup whose binary operation always keeps the right-hand element and discards
   * the left.
   */
  static <T> Semigroup<? extends Semigroup.Mu, T> last() {
    return (a, b) -> b;
  }

  /** The dual of a semigroup, which swaps the arguments of {@code append}. */
  static <T> Semigroup<? extends Semigroup.Mu, T> dual(
      final Semigroup<? extends Semigroup.Mu, T> semigroup) {
    return (a, b) -> semigroup.append(b, a);
  }

  static class Instances {
    private Instances() {}

    public static Semigroup<? extends Semigroup.Mu, Boolean> or() {
      return Monoid.Instances.or();
    }

    public static Semigroup<? extends Semigroup.Mu, Boolean> and() {
      return Monoid.Instances.and();
    }

    public static Semigroup<? extends Semigroup.Mu, Lazy<Boolean>> lazyOr() {
      return Monoid.Instances.lazyOr();
    }

    public static Semigroup<? extends Semigroup.Mu, Lazy<Boolean>> lazyAnd() {
      return Monoid.Instances.lazyAnd();
    }

    public static Semigroup<? extends Semigroup.Mu, Integer> intSum() {
      return Monoid.Instances.intSum();
    }

    public static Semigroup<? extends Semigroup.Mu, Integer> intProduct() {
      return Monoid.Instances.intProduct();
    }

    public static <T> Semigroup<? extends Semigroup.Mu, T> first() {
      return new FirstI<T>();
    }

    public static <T> Semigroup<? extends Semigroup.Mu, T> last() {
      return new LastI<T>();
    }

    // TODO: add more

    private static final class FirstI<T> implements Semigroup<FirstI.Mu, T> {
      public static final class Mu implements Semigroup.Mu {}

      @Override
      public T append(final T a, final T b) {
        return a;
      }
    }

    private static final class LastI<T> implements Semigroup<LastI.Mu, T> {
      public static final class Mu implements Semigroup.Mu {}

      @Override
      public T append(final T a, final T b) {
        return b;
      }
    }
  }
}
