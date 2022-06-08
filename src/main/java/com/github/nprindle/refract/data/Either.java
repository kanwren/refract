package com.github.nprindle.refract.data;

import com.github.nprindle.refract.classes.Applicative;
import com.github.nprindle.refract.classes.Bifunctor;
import com.github.nprindle.refract.classes.Foldable;
import com.github.nprindle.refract.classes.Functor;
import com.github.nprindle.refract.classes.Monoid;
import com.github.nprindle.refract.classes.Traversable;
import com.github.nprindle.refract.d17n.A1;
import com.github.nprindle.refract.d17n.A2;
import com.github.nprindle.refract.d17n.K1;
import com.github.nprindle.refract.d17n.K2;
import com.github.nprindle.refract.optics.Prism;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

public abstract class Either<A, B> implements A1<Either.Mu<A>, B>, A2<Either.Mu2, A, B> {
  public static final class Mu<A> implements K1 {}

  public static final <A, B> Either<A, B> resolve(final A1<Either.Mu<A>, B> p) {
    return (Either<A, B>) p;
  }

  public static final class Mu2 implements K2 {}

  public static final <A, B> Either<A, B> resolve(final A2<Either.Mu2, A, B> p) {
    return (Either<A, B>) p;
  }

  private Either() {}

  public abstract boolean isLeft();

  public abstract boolean isRight();

  public abstract Optional<A> fromLeft();

  public abstract Optional<B> fromRight();

  public abstract void whenLeft(final Consumer<? super A> f);

  public abstract void whenRight(final Consumer<? super B> f);

  public abstract <C> Either<C, B> mapLeft(final Function<? super A, ? extends C> f);

  public abstract <C> Either<A, C> mapRight(final Function<? super B, ? extends C> f);

  public abstract <C, D> Either<C, D> mapBoth(
      final Function<? super A, ? extends C> f, final Function<? super B, ? extends D> g);

  public abstract <C> Either<A, C> flatMap(final Function<? super B, ? extends Either<A, C>> k);

  public abstract <C> C either(
      final Function<? super A, ? extends C> onLeft,
      final Function<? super B, ? extends C> onRight);

  public static final class Left<A, B> extends Either<A, B> {
    private final A value;

    public Left(A value) {
      this.value = value;
    }

    @Override
    public boolean isLeft() {
      return true;
    }

    @Override
    public boolean isRight() {
      return false;
    }

    @Override
    public Optional<A> fromLeft() {
      return Optional.of(this.value);
    }

    @Override
    public Optional<B> fromRight() {
      return Optional.empty();
    }

    @Override
    public void whenLeft(final Consumer<? super A> f) {
      f.accept(this.value);
    }

    @Override
    public void whenRight(final Consumer<? super B> f) {
      return;
    }

    @Override
    public <C> Either<C, B> mapLeft(final Function<? super A, ? extends C> f) {
      return new Left<>(f.apply(this.value));
    }

    @Override
    public <C> Either<A, C> mapRight(final Function<? super B, ? extends C> f) {
      return new Left<>(this.value);
    }

    @Override
    public <C, D> Either<C, D> mapBoth(
        final Function<? super A, ? extends C> f, final Function<? super B, ? extends D> g) {
      return new Left<>(f.apply(this.value));
    }

    @Override
    public <C> C either(
        final Function<? super A, ? extends C> onLeft,
        final Function<? super B, ? extends C> onRight) {
      return onLeft.apply(this.value);
    }

    @Override
    public <C> Either<A, C> flatMap(final Function<? super B, ? extends Either<A, C>> k) {
      return new Left<>(this.value);
    }
  }

  public static final class Right<A, B> extends Either<A, B> {
    private final B value;

    public Right(B value) {
      this.value = value;
    }

    @Override
    public boolean isLeft() {
      return false;
    }

    @Override
    public boolean isRight() {
      return true;
    }

    @Override
    public Optional<A> fromLeft() {
      return Optional.empty();
    }

    @Override
    public Optional<B> fromRight() {
      return Optional.of(this.value);
    }

    @Override
    public void whenLeft(final Consumer<? super A> f) {
      return;
    }

    @Override
    public void whenRight(final Consumer<? super B> f) {
      f.accept(this.value);
    }

    @Override
    public <C> Either<C, B> mapLeft(final Function<? super A, ? extends C> f) {
      return new Right<>(this.value);
    }

    @Override
    public <C> Either<A, C> mapRight(final Function<? super B, ? extends C> f) {
      return new Right<>(f.apply(this.value));
    }

    @Override
    public <C, D> Either<C, D> mapBoth(
        final Function<? super A, ? extends C> f, final Function<? super B, ? extends D> g) {
      return new Right<>(g.apply(this.value));
    }

    @Override
    public <C> C either(
        final Function<? super A, ? extends C> onLeft,
        final Function<? super B, ? extends C> onRight) {
      return onRight.apply(this.value);
    }

    @Override
    public <C> Either<A, C> flatMap(final Function<? super B, ? extends Either<A, C>> k) {
      return k.apply(this.value);
    }
  }

  public static final class Instances {
    private Instances() {}

    public static <A> Functor<? extends Functor.Mu, Either.Mu<A>> functor() {
      return new Either.Instances.ApplicativeI<>();
    }

    public static <A> Applicative<? extends Applicative.Mu, Either.Mu<A>> applicative() {
      return new Either.Instances.ApplicativeI<>();
    }

    public static <A> Foldable<? extends Foldable.Mu, Either.Mu<A>> foldable() {
      return new Either.Instances.ApplicativeI<>();
    }

    public static <A> Traversable<? extends Traversable.Mu, Either.Mu<A>> traversable() {
      return new Either.Instances.ApplicativeI<>();
    }

    public static <A> Bifunctor<? extends Bifunctor.Mu, Either.Mu2> bifunctor() {
      return new Either.Instances.BifunctorI<>();
    }

    private static final class ApplicativeI<K>
        implements Applicative<ApplicativeI.Mu, Either.Mu<K>>,
            Functor<ApplicativeI.Mu, Either.Mu<K>>,
            Foldable<ApplicativeI.Mu, Either.Mu<K>>,
            Traversable<ApplicativeI.Mu, Either.Mu<K>> {
      public static final class Mu implements Traversable.Mu, Applicative.Mu {}

      @Override
      public <A, B> A1<Either.Mu<K>, B> map(
          final Function<? super A, ? extends B> f, final A1<Either.Mu<K>, A> x) {
        return Either.resolve(x).mapRight(f);
      }

      @Override
      public <A> A1<Either.Mu<K>, A> pure(final A x) {
        return new Either.Right<>(x);
      }

      @Override
      public <A, B> A1<Either.Mu<K>, B> ap(
          final A1<Either.Mu<K>, Function<? super A, ? extends B>> f, final A1<Either.Mu<K>, A> x) {
        return Either.resolve(f).flatMap(g -> Either.resolve(x).mapRight(y -> g.apply(y)));
      }

      @Override
      public <A, B, C> A1<Either.Mu<K>, C> apply2(
          final BiFunction<? super A, ? super B, ? extends C> f,
          final A1<Either.Mu<K>, A> x,
          final A1<Either.Mu<K>, B> y) {
        return Either.resolve(x).flatMap(a -> Either.resolve(y).mapRight(b -> f.apply(a, b)));
      }

      @Override
      public <A, B> A1<Either.Mu<K>, B> before(
          final A1<Either.Mu<K>, A> fx, final A1<Either.Mu<K>, B> fy) {
        return Either.resolve(fx).flatMap(x -> Either.resolve(fy));
      }

      @Override
      public <A, B> A1<Either.Mu<K>, A> then(
          final A1<Either.Mu<K>, A> fx, final A1<Either.Mu<K>, B> fy) {
        return Either.resolve(fx).flatMap(x -> Either.resolve(fy).mapRight(y -> x));
      }

      @Override
      public <M, A> M foldMap(
          final Monoid<? extends Monoid.Mu, M> monoid,
          final Function<? super A, ? extends M> f,
          final A1<Either.Mu<K>, A> x) {
        return Either.resolve(x).either(e -> monoid.empty(), f);
      }

      @Override
      public <A, B> B foldr(
          final BiFunction<? super A, ? super B, ? extends B> f,
          final B z,
          final A1<Either.Mu<K>, A> x) {
        return Either.resolve(x).either(e -> z, a -> f.apply(a, z));
      }

      @Override
      public <F extends K1, A, B> A1<F, A1<Either.Mu<K>, B>> traverse(
          final Applicative<? extends Applicative.Mu, F> applicative,
          final Function<? super A, ? extends A1<F, B>> f,
          final A1<Either.Mu<K>, A> x) {
        final Either<K, A> e = Either.resolve(x);
        return e.either(
            k -> applicative.pure(Either.left(k)), a -> applicative.map(Either::right, f.apply(a)));
      }
    }

    private static final class BifunctorI<K> implements Bifunctor<BifunctorI.Mu, Either.Mu2> {
      public static final class Mu implements Bifunctor.Mu {}

      @Override
      public <A, B, C, D> A2<Either.Mu2, C, D> bimap(
          final Function<? super A, ? extends C> f,
          final Function<? super B, ? extends D> g,
          final A2<Either.Mu2, A, B> x) {
        return Either.resolve(x).mapBoth(f, g);
      }
    }
  }

  public Either<B, A> swap() {
    return this.either(Either::right, Either::left);
  }

  public static <A, B> Either<A, B> left(A value) {
    return new Either.Left<>(value);
  }

  public static <A, B> Either<A, B> right(B value) {
    return new Either.Right<>(value);
  }

  public static final class Optics {
    public static <A, B, C> Prism<Either<A, C>, Either<B, C>, A, B> left() {
      return Prism.prism(
          s -> s.either(Either::right, c -> Either.left(Either.right(c))), Either::left);
    }

    public static <A, B, C> Prism<Either<A, B>, Either<A, C>, B, C> right() {
      return Prism.prism(
          s -> s.either(c -> Either.left(Either.left(c)), Either::right), Either::right);
    }
  }
}
