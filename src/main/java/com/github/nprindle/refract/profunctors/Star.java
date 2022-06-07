package com.github.nprindle.refract.profunctors;

import com.github.nprindle.refract.classes.AffineTraversing;
import com.github.nprindle.refract.classes.Applicative;
import com.github.nprindle.refract.classes.Choice;
import com.github.nprindle.refract.classes.Functor;
import com.github.nprindle.refract.classes.Profunctor;
import com.github.nprindle.refract.classes.Strong;
import com.github.nprindle.refract.classes.Traversing;
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
public interface Star<F extends K1, D, C>
    extends Function<D, A1<F, C>>,
        A1<Star.Mu<F, D>, C>,
        A2<Star.Mu2<F>, D, C>,
        A3<Star.Mu3, F, D, C> {
  static final class Mu<F extends K1, D> implements K1 {}

  static final class Mu2<F extends K1> implements K2 {}

  static final class Mu3 implements K3 {}

  static <F extends K1, D, C> Star<F, D, C> resolve(final A1<Star.Mu<F, D>, C> p) {
    return (Star<F, D, C>) p;
  }

  static <F extends K1, D, C> Star<F, D, C> resolve(final A2<Star.Mu2<F>, D, C> p) {
    return (Star<F, D, C>) p;
  }

  static <F extends K1, D, C> Star<F, D, C> resolve(final A3<Star.Mu3, F, D, C> p) {
    return (Star<F, D, C>) p;
  }

  static <F extends K1, D, C> Star<F, D, C> from(final Function<? super D, ? extends A1<F, C>> f) {
    return f::apply;
  }

  static final class Instances {
    public static <F extends K1> Profunctor<? extends Profunctor.Mu, Star.Mu2<F>> profunctor(
        final Functor<? extends Functor.Mu, F> functor) {
      return new Star.Instances.StrongI<>(functor);
    }

    public static <F extends K1> Strong<? extends Strong.Mu, Star.Mu2<F>> strong(
        final Functor<? extends Functor.Mu, F> functor) {
      return new Star.Instances.StrongI<>(functor);
    }

    public static <F extends K1> Choice<? extends Choice.Mu, Star.Mu2<F>> choice(
        final Applicative<? extends Applicative.Mu, F> applicative) {
      return new Star.Instances.ChoiceI<>(applicative);
    }

    public static <F extends K1>
        AffineTraversing<? extends AffineTraversing.Mu, Star.Mu2<F>> affineTraversing(
            final Applicative<? extends Applicative.Mu, F> applicative) {
      return new Star.Instances.ChoiceI<>(applicative);
    }

    public static <F extends K1> Traversing<? extends Traversing.Mu, Star.Mu2<F>> traversing(
        final Applicative<? extends Applicative.Mu, F> applicative) {
      return new Star.Instances.ChoiceI<>(applicative);
    }

    private static class StrongI<Mu extends StrongI.Mu, F extends K1>
        implements Profunctor<Mu, Star.Mu2<F>>, Strong<Mu, Star.Mu2<F>> {
      public static class Mu implements Strong.Mu {}

      final Functor<? extends Functor.Mu, F> functor;

      public StrongI(final Functor<? extends Functor.Mu, F> functor) {
        this.functor = functor;
      }

      @Override
      public <A, B, C, D> A2<Star.Mu2<F>, C, D> dimap(
          final Function<? super C, ? extends A> f,
          final Function<? super B, ? extends D> g,
          final A2<Star.Mu2<F>, A, B> x) {
        final Star<F, C, D> r = c -> functor.map(g, Star.resolve(x).apply(f.apply(c)));
        return r;
      }

      @Override
      public <A, B, C> A2<Star.Mu2<F>, Pair<A, C>, Pair<B, C>> first(
          final A2<Star.Mu2<F>, A, B> p) {
        final Star<F, Pair<A, C>, Pair<B, C>> r =
            ac -> functor.map(b -> Pair.of(b, ac.snd()), Star.resolve(p).apply(ac.fst()));
        return r;
      }

      @Override
      public <A, B, C> A2<Star.Mu2<F>, Pair<C, A>, Pair<C, B>> second(
          final A2<Star.Mu2<F>, A, B> p) {
        final Star<F, Pair<C, A>, Pair<C, B>> r =
            ac -> functor.map(b -> Pair.of(ac.fst(), b), Star.resolve(p).apply(ac.snd()));
        return r;
      }
    }

    private static class ChoiceI<F extends K1> extends StrongI<ChoiceI.Mu, F>
        implements Choice<ChoiceI.Mu, Star.Mu2<F>>, Traversing<ChoiceI.Mu, Star.Mu2<F>> {
      public static final class Mu extends StrongI.Mu implements Traversing.Mu, Choice.Mu {}

      final Applicative<? extends Functor.Mu, F> applicative;

      public ChoiceI(final Applicative<? extends Functor.Mu, F> applicative) {
        super(applicative);
        this.applicative = applicative;
      }

      @Override
      public <A, B, C> A2<Star.Mu2<F>, Either<A, C>, Either<B, C>> left(
          final A2<Star.Mu2<F>, A, B> p) {
        final Star<F, Either<A, C>, Either<B, C>> r =
            eac ->
                eac.either(
                    a -> applicative.map(Either::left, Star.resolve(p).apply(a)),
                    c -> applicative.pure(Either.right(c)));
        return r;
      }

      @Override
      public <A, B, C> A2<Star.Mu2<F>, Either<C, A>, Either<C, B>> right(
          final A2<Star.Mu2<F>, A, B> p) {
        final Star<F, Either<C, A>, Either<C, B>> r =
            eca ->
                eca.either(
                    c -> applicative.pure(Either.left(c)),
                    a -> applicative.map(Either::right, Star.resolve(p).apply(a)));
        return r;
      }

      @Override
      public <A, B, S, T> A2<Star.Mu2<F>, S, T> wander(
          final Traversing.Wander<S, T, A, B> wander, final A2<Star.Mu2<F>, A, B> p) {
        final Star<F, S, T> r = s -> wander.runWander(applicative, Star.resolve(p), s);
        return r;
      }
    }
  }
}
