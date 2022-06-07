package com.github.nprindle.refract.optics;

import com.github.nprindle.refract.classes.AffineTraversing;
import com.github.nprindle.refract.classes.Choice;
import com.github.nprindle.refract.classes.Profunctor;
import com.github.nprindle.refract.classes.Strong;
import com.github.nprindle.refract.d17n.A1;
import com.github.nprindle.refract.d17n.A2;
import com.github.nprindle.refract.d17n.A3;
import com.github.nprindle.refract.d17n.A4;
import com.github.nprindle.refract.d17n.K1;
import com.github.nprindle.refract.d17n.K2;
import com.github.nprindle.refract.d17n.K3;
import com.github.nprindle.refract.d17n.K4;
import com.github.nprindle.refract.data.Either;
import com.github.nprindle.refract.data.Pair;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

public interface AffineTraversal<S, T, A, B> extends Optic<AffineTraversing.Mu, S, T, A, B> {
  static <S, T, A, B> AffineTraversal<S, T, A, B> fromOptic(
      final Optic<? super AffineTraversing.Mu, S, T, A, B> optic) {
    return new AffineTraversal<S, T, A, B>() {
      @Override
      public <P extends K2> A2<P, S, T> runOptic(
          final A1<? extends AffineTraversing.Mu, P> dict, final A2<P, A, B> rel) {
        return optic.runOptic(dict, rel);
      }
    };
  }

  static <S, T, A, B> AffineTraversal<S, T, A, B> affineTraversal(
      final Function<? super S, ? extends Either<T, A>> match,
      final BiFunction<? super S, ? super B, ? extends T> setter) {
    return new AffineTraversal<S, T, A, B>() {
      @Override
      public <P extends K2> A2<P, S, T> runOptic(
          final A1<? extends AffineTraversing.Mu, P> dict, final A2<P, A, B> rel) {
        final AffineTraversing<? extends AffineTraversing.Mu, P> at =
            AffineTraversing.resolve(dict);
        return at.dimap(
            s -> Pair.of(s, match.apply(s)),
            p -> p.snd().either(t -> t, b -> setter.apply(p.fst(), b)),
            at.second(at.right(rel)));
      }
    };
  }

  static <S, A, B> AffineTraversal<S, S, A, B> simpleAffineTraversal(
      final Function<? super S, ? extends Optional<A>> preview,
      final BiFunction<? super S, ? super B, ? extends S> setter) {
    return new AffineTraversal<S, S, A, B>() {
      @Override
      public <P extends K2> A2<P, S, S> runOptic(
          final A1<? extends AffineTraversing.Mu, P> dict, final A2<P, A, B> rel) {
        final AffineTraversing<? extends AffineTraversing.Mu, P> at =
            AffineTraversing.resolve(dict);
        return at.dimap(
            s ->
                Pair.of(
                    s,
                    preview
                        .apply(s)
                        .map(x -> Either.<S, A>right(x))
                        .orElseGet(() -> Either.left(s))),
            p -> p.snd().either(t -> t, b -> setter.apply(p.fst(), b)),
            at.second(at.right(rel)));
      }
    };
  }

  default <R> R withOptional(
      final BiFunction<? super Function<S, Either<T, A>>, ? super BiFunction<S, B, T>, R> k) {
    throw new Error("not implemented"); // TODO
  }

  default Either<T, A> matching(final S s) {
    final Match<A, A, B> trivialMatch = Either::right;
    return Match.resolve(this.runOptic(Match.Instances.affineTraversing(), trivialMatch)).apply(s);
  }

  default boolean is(final S s) {
    return this.matching(s).isRight();
  }

  static <S, A> AffineTraversal<S, S, A, A> ignored() {
    return AffineTraversal.simpleAffineTraversal(s -> Optional.empty(), (s, b) -> s);
  }

  @FunctionalInterface
  public static interface UnpackAffineTraversal<A, B, S, T>
      extends A1<UnpackAffineTraversal.Mu<A, B, S>, T>,
          A2<UnpackAffineTraversal.Mu2<A, B>, S, T>,
          A3<UnpackAffineTraversal.Mu3<A>, B, S, T>,
          A4<UnpackAffineTraversal.Mu4, A, B, S, T> {

    <R> R withUnpackAffineTraversal(
        final BiFunction<
                ? super Function<S, Either<T, A>>, ? super BiFunction<S, B, T>, ? extends R>
            f);

    static final class Mu<A, B, S> implements K1 {}

    static final class Mu2<A, B> implements K2 {}

    static final class Mu3<A> implements K3 {}

    static final class Mu4 implements K4 {}

    static <A, B, S, T> UnpackAffineTraversal<A, B, S, T> resolve(
        final A1<UnpackAffineTraversal.Mu<A, B, S>, T> p) {
      return (UnpackAffineTraversal<A, B, S, T>) p;
    }

    static <A, B, S, T> UnpackAffineTraversal<A, B, S, T> resolve(
        final A2<UnpackAffineTraversal.Mu2<A, B>, S, T> p) {
      return (UnpackAffineTraversal<A, B, S, T>) p;
    }

    static <A, B, S, T> UnpackAffineTraversal<A, B, S, T> resolve(
        final A3<UnpackAffineTraversal.Mu3<A>, B, S, T> p) {
      return (UnpackAffineTraversal<A, B, S, T>) p;
    }

    static <A, B, S, T> UnpackAffineTraversal<A, B, S, T> resolve(
        final A4<UnpackAffineTraversal.Mu4, A, B, S, T> p) {
      return (UnpackAffineTraversal<A, B, S, T>) p;
    }

    static final class Instances {
      public static <A, B>
          Profunctor<? extends Profunctor.Mu, UnpackAffineTraversal.Mu2<A, B>> profunctor() {
        return new UnpackAffineTraversal.Instances.AffineTraversingI<>();
      }

      public static <A, B> Strong<? extends Strong.Mu, UnpackAffineTraversal.Mu2<A, B>> strong() {
        return new UnpackAffineTraversal.Instances.AffineTraversingI<>();
      }

      public static <A, B> Choice<? extends Choice.Mu, UnpackAffineTraversal.Mu2<A, B>> choice() {
        return new UnpackAffineTraversal.Instances.AffineTraversingI<>();
      }

      public static <A, B>
          AffineTraversing<? extends AffineTraversing.Mu, UnpackAffineTraversal.Mu2<A, B>>
              affineTraversing() {
        return new UnpackAffineTraversal.Instances.AffineTraversingI<>();
      }

      private static class AffineTraversingI<Mu extends AffineTraversingI.Mu, AR, BR>
          implements Profunctor<Mu, UnpackAffineTraversal.Mu2<AR, BR>>,
              Strong<Mu, UnpackAffineTraversal.Mu2<AR, BR>>,
              Choice<Mu, UnpackAffineTraversal.Mu2<AR, BR>>,
              AffineTraversing<Mu, UnpackAffineTraversal.Mu2<AR, BR>> {
        public static class Mu implements AffineTraversing.Mu {}

        @Override
        public <A, B, C, D> A2<UnpackAffineTraversal.Mu2<AR, BR>, C, D> dimap(
            final Function<? super C, ? extends A> f,
            final Function<? super B, ? extends D> g,
            final A2<UnpackAffineTraversal.Mu2<AR, BR>, A, B> x) {
          throw new Error("not implemented"); // TODO
        }

        @Override
        public <A, B, C> A2<UnpackAffineTraversal.Mu2<AR, BR>, Pair<A, C>, Pair<B, C>> first(
            final A2<UnpackAffineTraversal.Mu2<AR, BR>, A, B> p) {
          throw new Error("not implemented"); // TODO
        }

        @Override
        public <A, B, C> A2<UnpackAffineTraversal.Mu2<AR, BR>, Pair<C, A>, Pair<C, B>> second(
            final A2<UnpackAffineTraversal.Mu2<AR, BR>, A, B> p) {
          throw new Error("not implemented"); // TODO
        }

        @Override
        public <A, B, C> A2<UnpackAffineTraversal.Mu2<AR, BR>, Either<A, C>, Either<B, C>> left(
            final A2<UnpackAffineTraversal.Mu2<AR, BR>, A, B> p) {
          throw new Error("not implemented"); // TODO
        }

        @Override
        public <A, B, C> A2<UnpackAffineTraversal.Mu2<AR, BR>, Either<C, A>, Either<C, B>> right(
            final A2<UnpackAffineTraversal.Mu2<AR, BR>, A, B> p) {
          throw new Error("not implemented"); // TODO
        }
      }
    }
  }

  @FunctionalInterface
  public static interface Match<R, A, B>
      extends Function<A, Either<B, R>>,
          A1<Match.Mu<R, A>, B>,
          A2<Match.Mu2<R>, A, B>,
          A3<Match.Mu3, R, A, B> {
    static final class Mu<R, A> implements K1 {}

    static final class Mu2<R> implements K2 {}

    static final class Mu3 implements K3 {}

    static <R, A, B> Match<R, A, B> resolve(final A1<Match.Mu<R, A>, B> p) {
      return (Match<R, A, B>) p;
    }

    static <R, A, B> Match<R, A, B> resolve(final A2<Match.Mu2<R>, A, B> p) {
      return (Match<R, A, B>) p;
    }

    static <R, A, B> Match<R, A, B> resolve(final A3<Match.Mu3, R, A, B> p) {
      return (Match<R, A, B>) p;
    }

    static final class Instances {
      public static <R> Profunctor<? extends Profunctor.Mu, Match.Mu2<R>> profunctor() {
        return new Match.Instances.AffineTraversingI<>();
      }

      public static <R> Strong<? extends Strong.Mu, Match.Mu2<R>> strong() {
        return new Match.Instances.AffineTraversingI<>();
      }

      public static <R> Choice<? extends Choice.Mu, Match.Mu2<R>> choice() {
        return new Match.Instances.AffineTraversingI<>();
      }

      public static <R>
          AffineTraversing<? extends AffineTraversing.Mu, Match.Mu2<R>> affineTraversing() {
        return new Match.Instances.AffineTraversingI<>();
      }

      private static class AffineTraversingI<Mu extends AffineTraversingI.Mu, R>
          implements Profunctor<Mu, Match.Mu2<R>>,
              Strong<Mu, Match.Mu2<R>>,
              Choice<Mu, Match.Mu2<R>>,
              AffineTraversing<Mu, Match.Mu2<R>> {
        public static class Mu implements AffineTraversing.Mu {}

        @Override
        public <A, B, C, D> A2<Match.Mu2<R>, C, D> dimap(
            final Function<? super C, ? extends A> f,
            final Function<? super B, ? extends D> g,
            final A2<Match.Mu2<R>, A, B> x) {
          final Match<R, C, D> match = c -> Match.resolve(x).apply(f.apply(c)).mapBoth(g, r -> r);
          return match;
        }

        @Override
        public <A, B, C> A2<Match.Mu2<R>, Pair<A, C>, Pair<B, C>> first(
            final A2<Match.Mu2<R>, A, B> p) {
          final Match<R, Pair<A, C>, Pair<B, C>> match =
              pair -> Match.resolve(p).apply(pair.fst()).mapLeft(b -> Pair.of(b, pair.snd()));
          return match;
        }

        @Override
        public <A, B, C> A2<Match.Mu2<R>, Pair<C, A>, Pair<C, B>> second(
            final A2<Match.Mu2<R>, A, B> p) {
          final Match<R, Pair<C, A>, Pair<C, B>> match =
              pair -> Match.resolve(p).apply(pair.snd()).mapLeft(b -> Pair.of(pair.fst(), b));
          return match;
        }

        @Override
        public <A, B, C> A2<Match.Mu2<R>, Either<A, C>, Either<B, C>> left(
            final A2<Match.Mu2<R>, A, B> p) {
          final Match<R, Either<A, C>, Either<B, C>> match =
              e ->
                  e.either(
                      a -> Match.resolve(p).apply(a).mapLeft(Either::left),
                      c -> Either.left(Either.right(c)));
          return match;
        }

        @Override
        public <A, B, C> A2<Match.Mu2<R>, Either<C, A>, Either<C, B>> right(
            final A2<Match.Mu2<R>, A, B> p) {
          final Match<R, Either<C, A>, Either<C, B>> match =
              e ->
                  e.either(
                      c -> Either.left(Either.left(c)),
                      a -> Match.resolve(p).apply(a).mapLeft(Either::right));
          return match;
        }
      }
    }
  }
}
