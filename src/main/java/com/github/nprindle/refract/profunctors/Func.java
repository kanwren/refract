package com.github.nprindle.refract.profunctors;

import com.github.nprindle.refract.classes.Applicative;
import com.github.nprindle.refract.classes.Choice;
import com.github.nprindle.refract.classes.Functor;
import com.github.nprindle.refract.classes.Monoid;
import com.github.nprindle.refract.classes.Profunctor;
import com.github.nprindle.refract.classes.Semigroup;
import com.github.nprindle.refract.classes.Strong;
import com.github.nprindle.refract.d17n.A1;
import com.github.nprindle.refract.d17n.A2;
import com.github.nprindle.refract.d17n.K1;
import com.github.nprindle.refract.d17n.K2;
import com.github.nprindle.refract.data.Either;
import com.github.nprindle.refract.data.Pair;
import java.util.function.Function;

public interface Func<A, B> extends Function<A, B>, A1<Func.Mu<A>, B>, A2<Func.Mu2, A, B> {
  static final class Mu<A> implements K1 {}

  static final class Mu2 implements K2 {}

  static <A, B> Func<A, B> unbox(A1<Func.Mu<A>, B> p) {
    return (Func<A, B>) p;
  }

  static <A, B> Func<A, B> unbox(A2<Func.Mu2, A, B> p) {
    return (Func<A, B>) p;
  }

  static <A, B> Func<A, B> from(Function<? super A, ? extends B> f) {
    return f::apply;
  }

  static final class Instances {
    public static <A> Functor<Func.Mu<A>> functor() {
      return new Func.Instances.ApplicativeI<>();
    }

    public static <A> Applicative<Func.Mu<A>> applicative() {
      return new Func.Instances.ApplicativeI<>();
    }

    public static Profunctor<Func.Mu2> profunctor() {
      return ProfunctorI.INSTANCE;
    }

    public static Strong<Func.Mu2> strong() {
      return ProfunctorI.INSTANCE;
    }

    public static Choice<Func.Mu2> choice() {
      return ProfunctorI.INSTANCE;
    }

    public static <A, B> Semigroup<Func<A, B>> semigroup(Semigroup<B> resultSemigroup) {
      return new SemigroupI<>(resultSemigroup);
    }

    public static <A, B> Monoid<Func<A, B>> monoid(Monoid<B> resultMonoid) {
      return new MonoidI<>(resultMonoid);
    }

    private static final class ApplicativeI<E>
        implements Applicative<Func.Mu<E>>, Functor<Func.Mu<E>> {
      @Override
      public <A, B> A1<Func.Mu<E>, B> map(
          final Function<? super A, ? extends B> f, final A1<Func.Mu<E>, A> x) {
        return Func.from(f.compose(Func.unbox(x)));
      }

      @Override
      public <A> A1<Func.Mu<E>, A> pure(final A x) {
        final Func<E, A> f = e -> x;
        return f;
      }

      @Override
      public <A, B> A1<Func.Mu<E>, B> ap(
          final A1<Func.Mu<E>, Function<? super A, ? extends B>> f, final A1<Func.Mu<E>, A> x) {
        // (e -> a -> b) -> (e -> a) -> (e -> b)
        // \f g e -> f e (g e)
        final Func<E, B> g = e -> Func.unbox(f).apply(e).apply(Func.unbox(x).apply(e));
        return g;
      }
    }

    private static enum ProfunctorI
        implements Profunctor<Func.Mu2>, Strong<Func.Mu2>, Choice<Func.Mu2> {
      INSTANCE;

      @Override
      public <A, B, C, D> A2<Func.Mu2, C, D> dimap(
          final Function<? super C, ? extends A> f,
          final Function<? super B, ? extends D> g,
          final A2<Func.Mu2, A, B> x) {
        // (c -> a) -> (b -> d) -> (a -> b) -> (c -> d)
        final Func<C, D> r = c -> g.apply(Func.unbox(x).apply(f.apply(c)));
        return r;
      }

      @Override
      public <A, B, C> A2<Func.Mu2, Pair<A, C>, Pair<B, C>> first(final A2<Func.Mu2, A, B> p) {
        // (a -> b) -> ((a, c) -> (b, c))
        final Func<Pair<A, C>, Pair<B, C>> r = ac -> ac.mapFst(Func.unbox(p));
        return r;
      }

      @Override
      public <A, B, C> A2<Func.Mu2, Pair<C, A>, Pair<C, B>> second(final A2<Func.Mu2, A, B> p) {
        // (a -> b) -> ((c, a) -> (c, b))
        final Func<Pair<C, A>, Pair<C, B>> r = ac -> ac.mapSnd(Func.unbox(p));
        return r;
      }

      @Override
      public <A, B, C> A2<Func.Mu2, Either<A, C>, Either<B, C>> left(final A2<Func.Mu2, A, B> p) {
        // (a -> b) -> (Either a c -> Either b c)
        final Func<Either<A, C>, Either<B, C>> r = eac -> eac.mapLeft(Func.unbox(p));
        return r;
      }

      @Override
      public <A, B, C> A2<Func.Mu2, Either<C, A>, Either<C, B>> right(final A2<Func.Mu2, A, B> p) {
        // (a -> b) -> (Either c a -> Either c b)
        final Func<Either<C, A>, Either<C, B>> r = eac -> eac.mapRight(Func.unbox(p));
        return r;
      }
    }

    private static class SemigroupI<A, B> implements Semigroup<Func<A, B>> {
      private final Semigroup<B> resultSemigroup;

      public SemigroupI(final Semigroup<B> resultSemigroup) {
        this.resultSemigroup = resultSemigroup;
      }

      @Override
      public Func<A, B> append(final Func<A, B> f, final Func<A, B> g) {
        final Func<A, B> r = a -> resultSemigroup.append(f.apply(a), g.apply(a));
        return r;
      }
    }

    private static class MonoidI<A, B> extends SemigroupI<A, B> implements Monoid<Func<A, B>> {
      private final Monoid<B> resultMonoid;

      public MonoidI(final Monoid<B> resultMonoid) {
        super(resultMonoid);
        this.resultMonoid = resultMonoid;
      }

      @Override
      public Func<A, B> empty() {
        final Func<A, B> r = a -> resultMonoid.empty();
        return r;
      }
    }
  }
}
