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

  default <R> R withAffineTraversal(
      final BiFunction<? super Function<S, Either<T, A>>, ? super BiFunction<S, B, T>, R> k) {
    final UnpackAffineTraversal<A, B, A, B> trivialUnpackAffineTraversal =
        new UnpackAffineTraversal<A, B, A, B>() {
          @Override
          public <R> R withUnpackAffineTraversal(
              final BiFunction<
                      ? super Function<A, Either<B, A>>, ? super BiFunction<A, B, B>, ? extends R>
                  k) {
            final Function<A, Either<B, A>> match = Either::right;
            final BiFunction<A, B, B> setter = (a, b) -> b;
            return k.apply(match, setter);
          }
        };
    return UnpackAffineTraversal.resolve(
            this.runOptic(
                UnpackAffineTraversal.Instances.affineTraversing(), trivialUnpackAffineTraversal))
        .withUnpackAffineTraversal(k);
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
            k);

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

      private static class AffineTraversingI<Mu extends AffineTraversingI.Mu, A, B>
          implements Profunctor<Mu, UnpackAffineTraversal.Mu2<A, B>>,
              Strong<Mu, UnpackAffineTraversal.Mu2<A, B>>,
              Choice<Mu, UnpackAffineTraversal.Mu2<A, B>>,
              AffineTraversing<Mu, UnpackAffineTraversal.Mu2<A, B>> {
        public static class Mu implements AffineTraversing.Mu {}

        @Override
        public <S, T, S2, T2> A2<UnpackAffineTraversal.Mu2<A, B>, S2, T2> dimap(
            final Function<? super S2, ? extends S> s2s,
            final Function<? super T, ? extends T2> tt2,
            final A2<UnpackAffineTraversal.Mu2<A, B>, S, T> p) {
          return UnpackAffineTraversal.resolve(p)
              .withUnpackAffineTraversal(
                  (match, setter) -> {
                    return new UnpackAffineTraversal<A, B, S2, T2>() {
                      @Override
                      public <R> R withUnpackAffineTraversal(
                          final BiFunction<
                                  ? super Function<S2, Either<T2, A>>,
                                  ? super BiFunction<S2, B, T2>,
                                  ? extends R>
                              k) {
                        final Function<S2, Either<T2, A>> match2 =
                            s2 -> match.apply(s2s.apply(s2)).mapLeft(tt2);
                        final BiFunction<S2, B, T2> setter2 =
                            (s2, b) -> tt2.apply(setter.apply(s2s.apply(s2), b));
                        return k.apply(match2, setter2);
                      }
                    };
                  });
        }

        @Override
        public <S, T, C> A2<UnpackAffineTraversal.Mu2<A, B>, Pair<S, C>, Pair<T, C>> first(
            final A2<UnpackAffineTraversal.Mu2<A, B>, S, T> p) {
          return UnpackAffineTraversal.resolve(p)
              .withUnpackAffineTraversal(
                  (match, setter) -> {
                    return new UnpackAffineTraversal<A, B, Pair<S, C>, Pair<T, C>>() {
                      @Override
                      public <R> R withUnpackAffineTraversal(
                          final BiFunction<
                                  ? super Function<Pair<S, C>, Either<Pair<T, C>, A>>,
                                  ? super BiFunction<Pair<S, C>, B, Pair<T, C>>,
                                  ? extends R>
                              k) {
                        final Function<Pair<S, C>, Either<Pair<T, C>, A>> match2 =
                            pair -> match.apply(pair.fst()).mapLeft(t -> Pair.of(t, pair.snd()));
                        final BiFunction<Pair<S, C>, B, Pair<T, C>> setter2 =
                            (pair, b) -> Pair.of(setter.apply(pair.fst(), b), pair.snd());
                        return k.apply(match2, setter2);
                      }
                    };
                  });
        }

        @Override
        public <S, T, C> A2<UnpackAffineTraversal.Mu2<A, B>, Pair<C, S>, Pair<C, T>> second(
            final A2<UnpackAffineTraversal.Mu2<A, B>, S, T> p) {
          return UnpackAffineTraversal.resolve(p)
              .withUnpackAffineTraversal(
                  (match, setter) -> {
                    return new UnpackAffineTraversal<A, B, Pair<C, S>, Pair<C, T>>() {
                      @Override
                      public <R> R withUnpackAffineTraversal(
                          final BiFunction<
                                  ? super Function<Pair<C, S>, Either<Pair<C, T>, A>>,
                                  ? super BiFunction<Pair<C, S>, B, Pair<C, T>>,
                                  ? extends R>
                              k) {
                        final Function<Pair<C, S>, Either<Pair<C, T>, A>> match2 =
                            pair -> match.apply(pair.snd()).mapLeft(t -> Pair.of(pair.fst(), t));
                        final BiFunction<Pair<C, S>, B, Pair<C, T>> setter2 =
                            (pair, b) -> Pair.of(pair.fst(), setter.apply(pair.snd(), b));
                        return k.apply(match2, setter2);
                      }
                    };
                  });
        }

        @Override
        public <S, T, C> A2<UnpackAffineTraversal.Mu2<A, B>, Either<S, C>, Either<T, C>> left(
            final A2<UnpackAffineTraversal.Mu2<A, B>, S, T> p) {
          return UnpackAffineTraversal.resolve(p)
              .withUnpackAffineTraversal(
                  (match, setter) -> {
                    return new UnpackAffineTraversal<A, B, Either<S, C>, Either<T, C>>() {
                      @Override
                      public <R> R withUnpackAffineTraversal(
                          final BiFunction<
                                  ? super Function<Either<S, C>, Either<Either<T, C>, A>>,
                                  ? super BiFunction<Either<S, C>, B, Either<T, C>>,
                                  ? extends R>
                              k) {
                        final Function<Either<S, C>, Either<Either<T, C>, A>> match2 =
                            e ->
                                e.either(
                                    s -> match.apply(s).mapLeft(Either::left),
                                    c -> Either.left(Either.right(c)));
                        final BiFunction<Either<S, C>, B, Either<T, C>> setter2 =
                            (e, b) -> e.either(s -> Either.left(setter.apply(s, b)), Either::right);
                        return k.apply(match2, setter2);
                      }
                    };
                  });
        }

        @Override
        public <S, T, C> A2<UnpackAffineTraversal.Mu2<A, B>, Either<C, S>, Either<C, T>> right(
            final A2<UnpackAffineTraversal.Mu2<A, B>, S, T> p) {
          return UnpackAffineTraversal.resolve(p)
              .withUnpackAffineTraversal(
                  (match, setter) -> {
                    return new UnpackAffineTraversal<A, B, Either<C, S>, Either<C, T>>() {
                      @Override
                      public <R> R withUnpackAffineTraversal(
                          final BiFunction<
                                  ? super Function<Either<C, S>, Either<Either<C, T>, A>>,
                                  ? super BiFunction<Either<C, S>, B, Either<C, T>>,
                                  ? extends R>
                              k) {
                        final Function<Either<C, S>, Either<Either<C, T>, A>> match2 =
                            e ->
                                e.either(
                                    c -> Either.left(Either.left(c)),
                                    s -> match.apply(s).mapLeft(Either::right));
                        final BiFunction<Either<C, S>, B, Either<C, T>> setter2 =
                            (e, b) -> e.either(Either::left, s -> Either.right(setter.apply(s, b)));
                        return k.apply(match2, setter2);
                      }
                    };
                  });
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
