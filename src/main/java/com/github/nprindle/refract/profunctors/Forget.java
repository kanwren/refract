package com.github.nprindle.refract.profunctors;

import com.github.nprindle.refract.classes.Applicative;
import com.github.nprindle.refract.classes.Bicontravariant;
import com.github.nprindle.refract.classes.Cochoice;
import com.github.nprindle.refract.classes.Contravariant;
import com.github.nprindle.refract.classes.Functor;
import com.github.nprindle.refract.classes.Profunctor;
import com.github.nprindle.refract.classes.Strong;
import com.github.nprindle.refract.classes.Traversable;
import com.github.nprindle.refract.d17n.A1;
import com.github.nprindle.refract.d17n.A2;
import com.github.nprindle.refract.d17n.A3;
import com.github.nprindle.refract.d17n.K1;
import com.github.nprindle.refract.d17n.K2;
import com.github.nprindle.refract.d17n.K3;
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

  static <R, A, B> Forget<R, A, B> unbox(final A1<Forget.Mu<R, A>, B> p) {
    return (Forget<R, A, B>) p;
  }

  static <R, A, B> Forget<R, A, B> unbox(final A2<Forget.Mu2<R>, A, B> p) {
    return (Forget<R, A, B>) p;
  }

  static <R, A, B> Forget<R, A, B> unbox(final A3<Forget.Mu3, R, A, B> p) {
    return (Forget<R, A, B>) p;
  }

  static <R, A, B> Forget<R, A, B> from(final Function<? super A, ? extends R> f) {
    return f::apply;
  }

  static <A, B> Forget<A, A, B> identity() {
    return x -> x;
  }

  static final class Instances {
    public static <R, A> Functor<Forget.Mu<R, A>> functor() {
      return new Forget.Instances.FunctorI<>();
    }

    public static <R, A> Functor<Forget.Mu<R, A>> contravariant() {
      return new Forget.Instances.FunctorI<>();
    }

    public static <R, A> Functor<Forget.Mu<R, A>> traversable() {
      return new Forget.Instances.FunctorI<>();
    }

    public static <R> Profunctor<Forget.Mu2<R>> profunctor() {
      return new Forget.Instances.ProfunctorI<>();
    }

    public static <R> Strong<Forget.Mu2<R>> strong() {
      return new Forget.Instances.ProfunctorI<>();
    }

    public static <R> Cochoice<Forget.Mu2<R>> cochoice() {
      return new Forget.Instances.ProfunctorI<>();
    }

    public static <R> Bicontravariant<Forget.Mu2<R>> bicontravariant() {
      return new Forget.Instances.ProfunctorI<>();
    }

    private static class FunctorI<R, K>
        implements Functor<Forget.Mu<R, K>>,
            Contravariant<Forget.Mu<R, K>>,
            Traversable<Forget.Mu<R, K>> {
      @Override
      public <A, B> A1<Forget.Mu<R, K>, B> map(
          final Function<? super A, ? extends B> f, final A1<Forget.Mu<R, K>, A> x) {
        final Forget<R, K, B> r = Forget.unbox(x)::apply;
        return r;
      }

      @Override
      public <A, B> A1<Forget.Mu<R, K>, B> cmap(
          final Function<? extends B, ? super A> f, final A1<Forget.Mu<R, K>, A> x) {
        final Forget<R, K, B> r = Forget.unbox(x)::apply;
        return r;
      }

      @Override
      public <F extends K1, A, B> A1<F, A1<Forget.Mu<R, K>, B>> traverse(
          final Applicative<F> applicative,
          final Function<? super A, ? extends A1<F, B>> f,
          final A1<Forget.Mu<R, K>, A> x) {
        final Forget<R, K, B> r = Forget.unbox(x)::apply;
        return applicative.pure(r);
      }
    }

    private static class ProfunctorI<R>
        implements Profunctor<Forget.Mu2<R>>,
            Strong<Forget.Mu2<R>>,
            Cochoice<Forget.Mu2<R>>,
            Bicontravariant<Forget.Mu2<R>> {
      @Override
      public <A, B, C, D> A2<Forget.Mu2<R>, C, D> dimap(
          final Function<? super C, ? extends A> f,
          final Function<? super B, ? extends D> g,
          final A2<Forget.Mu2<R>, A, B> x) {
        final Forget<R, C, D> r = c -> Forget.unbox(x).apply(f.apply(c));
        return r;
      }

      @Override
      public <A, B, C, D> A2<Forget.Mu2<R>, C, D> cimap(
          final Function<? super C, ? extends A> f,
          final Function<? super D, ? extends B> g,
          final A2<Forget.Mu2<R>, A, B> x) {
        final Forget<R, C, D> r = c -> Forget.unbox(x).apply(f.apply(c));
        return r;
      }

      @Override
      public <A, B, C> A2<Forget.Mu2<R>, Pair<A, C>, Pair<B, C>> first(
          final A2<Forget.Mu2<R>, A, B> p) {
        final Forget<R, Pair<A, C>, Pair<B, C>> r = ac -> Forget.unbox(p).apply(ac.fst());
        return r;
      }

      @Override
      public <A, B, C> A2<Forget.Mu2<R>, Pair<C, A>, Pair<C, B>> second(
          final A2<Forget.Mu2<R>, A, B> p) {
        final Forget<R, Pair<C, A>, Pair<C, B>> r = ca -> Forget.unbox(p).apply(ca.snd());
        return r;
      }

      @Override
      public <A, B, C> A2<Forget.Mu2<R>, A, B> unleft(
          final A2<Forget.Mu2<R>, Either<A, C>, Either<B, C>> p) {
        final Forget<R, A, B> r = a -> Forget.unbox(p).apply(Either.left(a));
        return r;
      }

      @Override
      public <A, B, C> A2<Forget.Mu2<R>, A, B> unright(
          final A2<Forget.Mu2<R>, Either<C, A>, Either<C, B>> p) {
        final Forget<R, A, B> r = a -> Forget.unbox(p).apply(Either.right(a));
        return r;
      }
    }
  }
}
