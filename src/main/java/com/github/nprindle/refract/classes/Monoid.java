package com.github.nprindle.refract.classes;

import com.github.nprindle.refract.d17n.A1;
import com.github.nprindle.refract.data.Lazy;
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
      public Optional<T> append(final Optional<T> ma, final Optional<T> mb) {
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

  static class Instances {
    private Instances() {}

    public static Monoid<? extends Monoid.Mu, Boolean> or() {
      return OrI.INSTANCE;
    }

    public static Monoid<? extends Monoid.Mu, Boolean> and() {
      return AndI.INSTANCE;
    }

    public static Monoid<? extends Monoid.Mu, Lazy<Boolean>> lazyOr() {
      return LazyOrI.INSTANCE;
    }

    public static Monoid<? extends Monoid.Mu, Lazy<Boolean>> lazyAnd() {
      return LazyAndI.INSTANCE;
    }

    public static Monoid<? extends Monoid.Mu, Integer> intSum() {
      return SumIntegerI.INSTANCE;
    }

    public static Monoid<? extends Monoid.Mu, Integer> intProduct() {
      return ProductIntegerI.INSTANCE;
    }

    public static <T> Monoid<? extends Monoid.Mu, Optional<T>> first() {
      return new FirstI<>();
    }

    public static <T> Monoid<? extends Monoid.Mu, Optional<T>> last() {
      return new LastI<>();
    }

    public static <T> Monoid<? extends Monoid.Mu, Lazy<Optional<T>>> lazyFirst() {
      return new LazyFirstI<>();
    }

    public static <T> Monoid<? extends Monoid.Mu, Lazy<Optional<T>>> lazyLast() {
      return new LazyLastI<>();
    }

    // TODO: add more

    private enum AndI implements Monoid<AndI.Mu, Boolean> {
      INSTANCE;

      public static final class Mu implements Monoid.Mu {}

      @Override
      public Boolean empty() {
        return true;
      }

      @Override
      public Boolean append(final Boolean a, final Boolean b) {
        return a && b;
      }
    }

    private enum OrI implements Monoid<OrI.Mu, Boolean> {
      INSTANCE;

      public static final class Mu implements Monoid.Mu {}

      @Override
      public Boolean empty() {
        return false;
      }

      @Override
      public Boolean append(final Boolean a, final Boolean b) {
        return a || b;
      }
    }

    private enum LazyAndI implements Monoid<LazyAndI.Mu, Lazy<Boolean>> {
      INSTANCE;

      public static final class Mu implements Monoid.Mu {}

      @Override
      public Lazy<Boolean> empty() {
        return Lazy.of(true);
      }

      @Override
      public Lazy<Boolean> append(final Lazy<Boolean> a, final Lazy<Boolean> b) {
        return Lazy.defer(() -> a.get() && b.get());
      }
    }

    private enum LazyOrI implements Monoid<LazyOrI.Mu, Lazy<Boolean>> {
      INSTANCE;

      public static final class Mu implements Monoid.Mu {}

      @Override
      public Lazy<Boolean> empty() {
        return Lazy.of(false);
      }

      @Override
      public Lazy<Boolean> append(final Lazy<Boolean> a, final Lazy<Boolean> b) {
        return Lazy.defer(() -> a.get() || b.get());
      }
    }

    private enum SumIntegerI implements Monoid<SumIntegerI.Mu, Integer> {
      INSTANCE;

      public static final class Mu implements Monoid.Mu {}

      @Override
      public Integer empty() {
        return 0;
      }

      @Override
      public Integer append(final Integer a, final Integer b) {
        return a + b;
      }
    }

    private enum ProductIntegerI implements Monoid<SumIntegerI.Mu, Integer> {
      INSTANCE;

      public static final class Mu implements Monoid.Mu {}

      @Override
      public Integer empty() {
        return 1;
      }

      @Override
      public Integer append(final Integer a, final Integer b) {
        return a * b;
      }
    }

    private static final class FirstI<T> implements Monoid<FirstI.Mu, Optional<T>> {
      public static final class Mu implements Monoid.Mu {}

      @Override
      public Optional<T> empty() {
        return Optional.empty();
      }

      @Override
      public Optional<T> append(final Optional<T> a, final Optional<T> b) {
        return a.or(() -> b);
      }
    }

    private static final class LastI<T> implements Monoid<LastI.Mu, Optional<T>> {
      public static final class Mu implements Monoid.Mu {}

      @Override
      public Optional<T> empty() {
        return Optional.empty();
      }

      @Override
      public Optional<T> append(final Optional<T> a, final Optional<T> b) {
        return b.or(() -> a);
      }
    }

    private static final class LazyFirstI<T> implements Monoid<FirstI.Mu, Lazy<Optional<T>>> {
      public static final class Mu implements Monoid.Mu {}

      @Override
      public Lazy<Optional<T>> empty() {
        return Lazy.of(Optional.empty());
      }

      @Override
      public Lazy<Optional<T>> append(final Lazy<Optional<T>> a, final Lazy<Optional<T>> b) {
        return Lazy.defer(() -> a.get().or(() -> b.get()));
      }
    }

    private static final class LazyLastI<T> implements Monoid<LastI.Mu, Lazy<Optional<T>>> {
      public static final class Mu implements Monoid.Mu {}

      @Override
      public Lazy<Optional<T>> empty() {
        return Lazy.of(Optional.empty());
      }

      @Override
      public Lazy<Optional<T>> append(final Lazy<Optional<T>> a, final Lazy<Optional<T>> b) {
        return Lazy.defer(() -> b.get().or(() -> a.get()));
      }
    }
  }
}
