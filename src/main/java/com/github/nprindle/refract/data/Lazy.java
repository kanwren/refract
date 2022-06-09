package com.github.nprindle.refract.data;

import com.github.nprindle.refract.classes.Applicative;
import com.github.nprindle.refract.classes.Foldable;
import com.github.nprindle.refract.classes.Functor;
import com.github.nprindle.refract.classes.Monoid;
import com.github.nprindle.refract.classes.Traversable;
import com.github.nprindle.refract.d17n.A1;
import com.github.nprindle.refract.d17n.K1;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public final class Lazy<A> implements A1<Lazy.Mu, A> {
  public static final class Mu implements K1 {}

  public static <A> Lazy<A> resolve(final A1<Lazy.Mu, A> p) {
    return (Lazy<A>) p;
  }

  private boolean resolved = false;
  private A value = null;
  private final Supplier<A> supplier;

  private Lazy(A value) {
    this.resolved = true;
    this.value = value;
    this.supplier = null;
  }

  private Lazy(Supplier<A> supplier) {
    this.resolved = false;
    this.value = null;
    this.supplier = supplier;
  }

  public static <A> Lazy<A> of(A value) {
    return new Lazy<>(value);
  }

  public static <A> Lazy<A> defer(Supplier<A> supplier) {
    return new Lazy<>(supplier);
  }

  public A get() {
    if (resolved) {
      return this.value;
    } else {
      this.value = supplier.get();
      this.resolved = true;
      return this.value;
    }
  }

  public static <A> A get(final A1<Lazy.Mu, A> p) {
    return Lazy.resolve(p).get();
  }

  public <B> Lazy<B> map(final Function<? super A, ? extends B> f) {
    return Lazy.defer(() -> f.apply(this.get()));
  }

  public static final class Instances {
    private Instances() {}

    public static final <M> Monoid<? extends Monoid.Mu, Lazy<M>> monoid(
        final Monoid<? extends Monoid.Mu, M> baseMonoid) {
      return new Monoid<Monoid.Mu, Lazy<M>>() {
        @Override
        public Lazy<M> empty() {
          return Lazy.defer(baseMonoid::empty);
        }

        @Override
        public Lazy<M> append(final Lazy<M> x, final Lazy<M> y) {
          return Lazy.defer(() -> baseMonoid.append(x.get(), y.get()));
        }
      };
    }

    public static final Functor<? extends Functor.Mu, Lazy.Mu> functor() {
      return Lazy.Instances.ApplicativeI.INSTANCE;
    }

    public static final Applicative<? extends Applicative.Mu, Lazy.Mu> applicative() {
      return Lazy.Instances.ApplicativeI.INSTANCE;
    }

    private static enum ApplicativeI
        implements
            Applicative<ApplicativeI.Mu, Lazy.Mu>, Functor<ApplicativeI.Mu, Lazy.Mu>,
            Foldable<ApplicativeI.Mu, Lazy.Mu>, Traversable<ApplicativeI.Mu, Lazy.Mu> {
      INSTANCE;

      public static final class Mu implements Traversable.Mu, Applicative.Mu {}

      @Override
      public <A, B> A1<Lazy.Mu, B> map(
          final Function<? super A, ? extends B> f, final A1<Lazy.Mu, A> x) {
        return Lazy.defer(() -> f.apply(Lazy.get(x)));
      }

      @Override
      public <A> A1<Lazy.Mu, A> pure(final A x) {
        return Lazy.of(x);
      }

      @Override
      public <A, B> A1<Lazy.Mu, B> ap(
          final A1<Lazy.Mu, Function<? super A, ? extends B>> f, final A1<Lazy.Mu, A> x) {
        return Lazy.defer(() -> Lazy.get(f).apply(Lazy.get(x)));
      }

      @Override
      public <A, B, C> A1<Lazy.Mu, C> apply2(
          final BiFunction<? super A, ? super B, ? extends C> f,
          final A1<Lazy.Mu, A> x,
          final A1<Lazy.Mu, B> y) {
        return Lazy.defer(() -> f.apply(Lazy.get(x), Lazy.get(y)));
      }

      @Override
      public <A, B> A1<Lazy.Mu, B> before(final A1<Lazy.Mu, A> fx, final A1<Lazy.Mu, B> fy) {
        return fy; // NOTE: assumes fx is not side-effectful
      }

      @Override
      public <A, B> A1<Lazy.Mu, A> then(final A1<Lazy.Mu, A> fx, final A1<Lazy.Mu, B> fy) {
        return fx; // NOTE: assumes fy is not side-effectful
      }

      @Override
      public <M, A> M foldMap(
          final Monoid<? extends Monoid.Mu, M> monoid,
          final Function<? super A, ? extends M> f,
          final A1<Lazy.Mu, A> x) {
        return f.apply(Lazy.get(x));
      }

      @Override
      public <A, B> B foldr(
          final BiFunction<? super A, ? super B, ? extends B> f,
          final B z,
          final A1<Lazy.Mu, A> x) {
        return f.apply(Lazy.get(x), z);
      }

      @Override
      public <F extends K1, A, B> A1<F, A1<Lazy.Mu, B>> traverse(
          final Applicative<? extends Applicative.Mu, F> applicative,
          final Function<? super A, ? extends A1<F, B>> f,
          final A1<Lazy.Mu, A> x) {
        return applicative.map(Lazy::of, f.apply(Lazy.get(x)));
      }
    }
  }
}
