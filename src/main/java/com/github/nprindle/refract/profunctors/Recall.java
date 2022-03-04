package com.github.nprindle.refract.profunctors;

import com.github.nprindle.refract.classes.Bifunctor;
import com.github.nprindle.refract.classes.Choice;
import com.github.nprindle.refract.classes.Costrong;
import com.github.nprindle.refract.classes.Profunctor;
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
public interface Recall<R, A, B>
    extends Function<R, B>,
        A1<Recall.Mu<R, A>, B>,
        A2<Recall.Mu2<R>, A, B>,
        A3<Recall.Mu3, R, A, B> {
  static final class Mu<R, A> implements K1 {}

  static final class Mu2<R> implements K2 {}

  static final class Mu3 implements K3 {}

  static <R, A, B> Recall<R, A, B> unbox(final A1<Recall.Mu<R, A>, B> p) {
    return (Recall<R, A, B>) p;
  }

  static <R, A, B> Recall<R, A, B> unbox(final A2<Recall.Mu2<R>, A, B> p) {
    return (Recall<R, A, B>) p;
  }

  static <R, A, B> Recall<R, A, B> unbox(final A3<Recall.Mu3, R, A, B> p) {
    return (Recall<R, A, B>) p;
  }

  static <R, A, B> Recall<R, A, B> from(final Function<? super R, ? extends B> f) {
    return f::apply;
  }

  static <A, B> Recall<B, A, B> identity() {
    return x -> x;
  }

  static final class Instances {
    public static <R> Profunctor<Recall.Mu2<R>> profunctor() {
      return new Recall.Instances.ProfunctorI<>();
    }

    public static <R> Choice<Recall.Mu2<R>> choice() {
      return new Recall.Instances.ProfunctorI<>();
    }

    public static <R> Costrong<Recall.Mu2<R>> costrong() {
      return new Recall.Instances.ProfunctorI<>();
    }

    public static <R> Bifunctor<Recall.Mu2<R>> bifunctor() {
      return new Recall.Instances.ProfunctorI<>();
    }

    private static class ProfunctorI<R>
        implements Profunctor<Recall.Mu2<R>>,
            Choice<Recall.Mu2<R>>,
            Costrong<Recall.Mu2<R>>,
            Bifunctor<Recall.Mu2<R>> {
      @Override
      public <A, B, C, D> A2<Recall.Mu2<R>, C, D> dimap(
          final Function<? super C, ? extends A> f,
          final Function<? super B, ? extends D> g,
          final A2<Recall.Mu2<R>, A, B> x) {
        final Recall<R, C, D> h = r -> g.apply(Recall.unbox(x).apply(r));
        return h;
      }

      @Override
      public <A, B, C, D> A2<Recall.Mu2<R>, C, D> bimap(
          final Function<? super A, ? extends C> f,
          final Function<? super B, ? extends D> g,
          final A2<Recall.Mu2<R>, A, B> x) {
        final Recall<R, C, D> h = r -> g.apply(Recall.unbox(x).apply(r));
        return h;
      }

      @Override
      public <A, B, C> A2<Recall.Mu2<R>, Either<A, C>, Either<B, C>> left(
          final A2<Recall.Mu2<R>, A, B> p) {
        final Recall<R, Either<A, C>, Either<B, C>> h = r -> Either.left(Recall.unbox(p).apply(r));
        return h;
      }

      @Override
      public <A, B, C> A2<Recall.Mu2<R>, Either<C, A>, Either<C, B>> right(
          final A2<Recall.Mu2<R>, A, B> p) {
        final Recall<R, Either<C, A>, Either<C, B>> h = r -> Either.right(Recall.unbox(p).apply(r));
        return h;
      }

      @Override
      public <A, B, C> A2<Recall.Mu2<R>, A, B> unfirst(
          final A2<Recall.Mu2<R>, Pair<A, C>, Pair<B, C>> p) {
        final Recall<R, A, B> h = r -> Recall.unbox(p).apply(r).fst();
        return h;
      }

      @Override
      public <A, B, C> A2<Recall.Mu2<R>, A, B> unsecond(
          final A2<Recall.Mu2<R>, Pair<C, A>, Pair<C, B>> p) {
        final Recall<R, A, B> h = r -> Recall.unbox(p).apply(r).snd();
        return h;
      }
    }
  }
}
