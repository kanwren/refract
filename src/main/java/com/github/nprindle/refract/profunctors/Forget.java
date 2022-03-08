package com.github.nprindle.refract.profunctors;

import com.github.nprindle.refract.classes.Applicative;
import com.github.nprindle.refract.classes.Bicontravariant;
import com.github.nprindle.refract.classes.Cochoice;
import com.github.nprindle.refract.classes.Contravariant;
import com.github.nprindle.refract.classes.Foldable;
import com.github.nprindle.refract.classes.Folding;
import com.github.nprindle.refract.classes.Functor;
import com.github.nprindle.refract.classes.Getting;
import com.github.nprindle.refract.classes.Monoid;
import com.github.nprindle.refract.classes.Profunctor;
import com.github.nprindle.refract.classes.Strong;
import com.github.nprindle.refract.classes.Traversable;
import com.github.nprindle.refract.classes.Traversing;
import com.github.nprindle.refract.d17n.A1;
import com.github.nprindle.refract.d17n.A2;
import com.github.nprindle.refract.d17n.A3;
import com.github.nprindle.refract.d17n.K1;
import com.github.nprindle.refract.d17n.K2;
import com.github.nprindle.refract.d17n.K3;
import com.github.nprindle.refract.data.Const;
import com.github.nprindle.refract.data.Either;
import com.github.nprindle.refract.data.Pair;
import java.util.function.Function;

@FunctionalInterface
public interface Forget<R, A, B>
    extends Function<A, R>,
        A1<Forget.Mu<R, A>, B>,
        A2<Forget.Mu2<R>, A, B>,
        A3<Forget.Mu3, R, A, B> {
  static final class Mu<R, A> implements K1 {}

  static final class Mu2<R> implements K2 {}

  static final class Mu3 implements K3 {}

  static <R, A, B> Forget<R, A, B> resolve(final A1<Forget.Mu<R, A>, B> p) {
    return (Forget<R, A, B>) p;
  }

  static <R, A, B> Forget<R, A, B> resolve(final A2<Forget.Mu2<R>, A, B> p) {
    return (Forget<R, A, B>) p;
  }

  static <R, A, B> Forget<R, A, B> resolve(final A3<Forget.Mu3, R, A, B> p) {
    return (Forget<R, A, B>) p;
  }

  static <R, A, B> Forget<R, A, B> from(final Function<? super A, ? extends R> f) {
    return f::apply;
  }

  static <A, B> Forget<A, A, B> identity() {
    return x -> x;
  }

  static final class Instances {
    public static <R, A> Functor<? extends Functor.Mu, Forget.Mu<R, A>> functor() {
      return new Forget.Instances.FunctorI<>();
    }

    public static <R, A>
        Contravariant<? extends Contravariant.Mu, Forget.Mu<R, A>> contravariant() {
      return new Forget.Instances.FunctorI<>();
    }

    public static <R, A> Foldable<? extends Foldable.Mu, Forget.Mu<R, A>> foldable() {
      return new Forget.Instances.FunctorI<>();
    }

    public static <R, A> Traversable<? extends Traversable.Mu, Forget.Mu<R, A>> traversable() {
      return new Forget.Instances.FunctorI<>();
    }

    public static <R> Profunctor<? extends Profunctor.Mu, Forget.Mu2<R>> profunctor() {
      return new Forget.Instances.GettingI<>();
    }

    public static <R> Strong<? extends Strong.Mu, Forget.Mu2<R>> strong() {
      return new Forget.Instances.GettingI<>();
    }

    public static <R> Cochoice<? extends Cochoice.Mu, Forget.Mu2<R>> cochoice() {
      return new Forget.Instances.GettingI<>();
    }

    public static <R>
        Bicontravariant<? extends Bicontravariant.Mu, Forget.Mu2<R>> bicontravariant() {
      return new Forget.Instances.GettingI<>();
    }

    public static <R> Getting<? extends Getting.Mu, Forget.Mu2<R>> getting() {
      return new Forget.Instances.GettingI<>();
    }

    public static <R> Getting<? extends Getting.Mu, Forget.Mu2<R>> choice(
        final Monoid<? extends Monoid.Mu, R> monoid) {
      return new Forget.Instances.FoldingI<>(monoid);
    }

    private static class FunctorI<R, K>
        implements Functor<FunctorI.Mu, Forget.Mu<R, K>>,
            Contravariant<FunctorI.Mu, Forget.Mu<R, K>>,
            Foldable<FunctorI.Mu, Forget.Mu<R, K>>,
            Traversable<FunctorI.Mu, Forget.Mu<R, K>> {
      public static class Mu implements Traversable.Mu, Contravariant.Mu {}

      @Override
      public <A, B> A1<Forget.Mu<R, K>, B> map(
          final Function<? super A, ? extends B> f, final A1<Forget.Mu<R, K>, A> x) {
        final Forget<R, K, B> r = Forget.resolve(x)::apply;
        return r;
      }

      @Override
      public <A, B> A1<Forget.Mu<R, K>, B> cmap(
          final Function<? extends B, ? super A> f, final A1<Forget.Mu<R, K>, A> x) {
        final Forget<R, K, B> r = Forget.resolve(x)::apply;
        return r;
      }

      @Override
      public <M, A> M foldMap(
          final Monoid<? extends Monoid.Mu, M> monoid,
          final Function<? super A, ? extends M> f,
          final A1<Forget.Mu<R, K>, A> x) {
        return monoid.empty();
      }

      @Override
      public <F extends K1, A, B> A1<F, A1<Forget.Mu<R, K>, B>> traverse(
          final Applicative<? extends Applicative.Mu, F> applicative,
          final Function<? super A, ? extends A1<F, B>> f,
          final A1<Forget.Mu<R, K>, A> x) {
        final Forget<R, K, B> r = Forget.resolve(x)::apply;
        return applicative.pure(r);
      }
    }

    private static class GettingI<Mu extends GettingI.Mu, R> implements Getting<Mu, Forget.Mu2<R>> {
      public static class Mu implements Getting.Mu {}

      @Override
      public <A, B, C, D> A2<Forget.Mu2<R>, C, D> dimap(
          final Function<? super C, ? extends A> f,
          final Function<? super B, ? extends D> g,
          final A2<Forget.Mu2<R>, A, B> x) {
        final Forget<R, C, D> r = c -> Forget.resolve(x).apply(f.apply(c));
        return r;
      }

      @Override
      public <A, B, C, D> A2<Forget.Mu2<R>, C, D> cimap(
          final Function<? super C, ? extends A> f,
          final Function<? super D, ? extends B> g,
          final A2<Forget.Mu2<R>, A, B> x) {
        final Forget<R, C, D> r = c -> Forget.resolve(x).apply(f.apply(c));
        return r;
      }

      @Override
      public <A, B, C> A2<Forget.Mu2<R>, Pair<A, C>, Pair<B, C>> first(
          final A2<Forget.Mu2<R>, A, B> p) {
        final Forget<R, Pair<A, C>, Pair<B, C>> r = ac -> Forget.resolve(p).apply(ac.fst());
        return r;
      }

      @Override
      public <A, B, C> A2<Forget.Mu2<R>, Pair<C, A>, Pair<C, B>> second(
          final A2<Forget.Mu2<R>, A, B> p) {
        final Forget<R, Pair<C, A>, Pair<C, B>> r = ca -> Forget.resolve(p).apply(ca.snd());
        return r;
      }

      @Override
      public <A, B, C> A2<Forget.Mu2<R>, A, B> unleft(
          final A2<Forget.Mu2<R>, Either<A, C>, Either<B, C>> p) {
        final Forget<R, A, B> r = a -> Forget.resolve(p).apply(Either.left(a));
        return r;
      }

      @Override
      public <A, B, C> A2<Forget.Mu2<R>, A, B> unright(
          final A2<Forget.Mu2<R>, Either<C, A>, Either<C, B>> p) {
        final Forget<R, A, B> r = a -> Forget.resolve(p).apply(Either.right(a));
        return r;
      }
    }

    private static class FoldingI<R> extends GettingI<FoldingI.Mu, R>
        implements Folding<FoldingI.Mu, Forget.Mu2<R>> {
      public static final class Mu extends GettingI.Mu implements Folding.Mu {}

      final Monoid<? extends Monoid.Mu, R> monoid;

      public FoldingI(final Monoid<? extends Monoid.Mu, R> monoid) {
        this.monoid = monoid;
      }

      @Override
      public <A, B, C> A2<Forget.Mu2<R>, Either<A, C>, Either<B, C>> left(
          final A2<Forget.Mu2<R>, A, B> p) {
        final Forget<R, Either<A, C>, Either<B, C>> r =
            eac -> eac.either(a -> Forget.resolve(p).apply(a), c -> this.monoid.empty());
        return r;
      }

      @Override
      public <A, B, C> A2<Forget.Mu2<R>, Either<C, A>, Either<C, B>> right(
          final A2<Forget.Mu2<R>, A, B> p) {
        final Forget<R, Either<C, A>, Either<C, B>> r =
            eca -> eca.either(c -> this.monoid.empty(), a -> Forget.resolve(p).apply(a));
        return r;
      }

      @Override
      public <A, B, S, T> A2<Forget.Mu2<R>, S, T> wander(
          final Traversing.Wander<S, T, A, B> wander, final A2<Forget.Mu2<R>, A, B> p) {
        // wander t (Forget f) = Forget (\s -> getConst (t (Const . f) s))
        final Function<A, A1<Const.Mu<R>, B>> g = Forget.resolve(p).andThen(Const::of);
        final Applicative<? extends Applicative.Mu, Const.Mu<R>> applicative =
            Const.Instances.applicative(this.monoid);
        final Forget<R, S, T> r = s -> Const.get(wander.wander(applicative, g, s));
        return r;
      }
    }
  }
}
