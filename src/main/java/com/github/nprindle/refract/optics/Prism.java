package com.github.nprindle.refract.optics;

import com.github.nprindle.refract.classes.Choice;
import com.github.nprindle.refract.classes.Profunctor;
import com.github.nprindle.refract.classes.Traversable;
import com.github.nprindle.refract.d17n.A1;
import com.github.nprindle.refract.d17n.A2;
import com.github.nprindle.refract.d17n.A3;
import com.github.nprindle.refract.d17n.A4;
import com.github.nprindle.refract.d17n.K1;
import com.github.nprindle.refract.d17n.K2;
import com.github.nprindle.refract.d17n.K3;
import com.github.nprindle.refract.d17n.K4;
import com.github.nprindle.refract.data.Either;
import com.github.nprindle.refract.data.Unit;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

// TODO:
// prisms for Optional

public interface Prism<S, T, A, B> extends Optic<Choice.Mu, S, T, A, B> {
  static <S, T, A, B> Prism<S, T, A, B> fromOptic(
      final Optic<? super Choice.Mu, S, T, A, B> optic) {
    return new Prism<S, T, A, B>() {
      @Override
      public <P extends K2> A2<P, S, T> runOptic(
          final A1<? extends Choice.Mu, P> dict, final A2<P, A, B> rel) {
        return optic.runOptic(dict, rel);
      }
    };
  }

  static <S, T, A, B> Prism<S, T, A, B> prism(
      final Function<? super S, ? extends Either<T, A>> match,
      final Function<? super B, ? extends T> review) {
    return new Prism<S, T, A, B>() {
      @Override
      public <P extends K2> A2<P, S, T> runOptic(
          final A1<? extends Choice.Mu, P> dict, final A2<P, A, B> rel) {
        final Choice<? extends Choice.Mu, P> choice = Choice.resolve(dict);
        return choice.dimap(match, e -> e.either(t -> t, review), choice.right(rel));
      }
    };
  }

  static <S, A, B> Prism<S, S, A, B> simplePrism(
      final Function<? super S, ? extends Optional<A>> preview,
      final Function<? super B, ? extends S> review) {
    return new Prism<S, S, A, B>() {
      @Override
      public <P extends K2> A2<P, S, S> runOptic(
          final A1<? extends Choice.Mu, P> dict, final A2<P, A, B> rel) {
        final Choice<? extends Choice.Mu, P> choice = Choice.resolve(dict);
        return choice.dimap(
            s -> preview.apply(s).map(x -> Either.<S, A>right(x)).orElseGet(() -> Either.left(s)),
            e -> e.either(t -> t, review),
            choice.right(rel));
      }
    };
  }

  default <R> R withPrism(
      final BiFunction<? super Function<S, Either<T, A>>, ? super Function<B, T>, R> k) {
    final UnpackPrism<A, B, A, B> trivialUnpackPrism =
        new UnpackPrism<A, B, A, B>() {
          @Override
          public <R> R withUnpackPrism(
              final BiFunction<
                      ? super Function<A, Either<B, A>>, ? super Function<B, B>, ? extends R>
                  k) {
            final Function<A, Either<B, A>> match = Either::right;
            final Function<B, B> review = b -> b;
            return k.apply(match, review);
          }
        };
    return UnpackPrism.resolve(this.runOptic(UnpackPrism.Instances.choice(), trivialUnpackPrism))
        .withUnpackPrism(k);
  }

  default Prism.Bundle<S, T, A, B> bundle() {
    final Prism.Bundle.FlippedBundle<A, B, A, B> trivialFlippedBundle =
        new Prism.Bundle.FlippedBundle<>(Either::right, b -> b);
    final Prism.Bundle.FlippedBundle<A, B, S, T> flippedBundle =
        Prism.Bundle.FlippedBundle.resolve(
            this.runOptic(Prism.Bundle.FlippedBundle.Instances.choice(), trivialFlippedBundle));
    return flippedBundle.flip();
  }

  static <F extends K1, S, A> Prism<A1<F, S>, A1<F, S>, A1<F, A>, A1<F, A>> below(
      final Traversable<? extends Traversable.Mu, F> traversable, final Prism<S, S, A, A> prism) {
    final Prism.Bundle<S, S, A, A> bundle = prism.bundle();
    return Prism.simplePrism(
        fs ->
            Either.resolve(traversable.traverse(Either.Instances.applicative(), bundle.match, fs))
                .fromRight(),
        fa -> traversable.map(bundle.review, fa));
  }

  static <A> Prism<A, A, Unit, Unit> nearly(final A a, final Predicate<A> pred) {
    return Prism.simplePrism(x -> pred.test(x) ? Optional.of(Unit.UNIT) : Optional.empty(), u -> a);
  }

  /**
   * A bundle of functions given by an {@code Prism}. This can also be seen as {@code UnpackPrism}
   * without a CPS transform.
   */
  public static final class Bundle<S, T, A, B> {
    public final Function<S, Either<T, A>> match;
    public final Function<B, T> review;

    public Bundle(Function<S, Either<T, A>> match, Function<B, T> review) {
      this.match = match;
      this.review = review;
    }

    // Like 'Bundle', but with the arguments in an order that's amenable to the
    // profunctor transformation
    private static final class FlippedBundle<A, B, S, T>
        implements A1<FlippedBundle.Mu<A, B, S>, T>,
            A2<FlippedBundle.Mu2<A, B>, S, T>,
            A3<FlippedBundle.Mu3<A>, B, S, T>,
            A4<FlippedBundle.Mu4, A, B, S, T> {
      static final class Mu<A, B, S> implements K1 {}

      static final class Mu2<A, B> implements K2 {}

      static final class Mu3<A> implements K3 {}

      static final class Mu4 implements K4 {}

      static <A, B, S, T> FlippedBundle<A, B, S, T> resolve(
          final A1<FlippedBundle.Mu<A, B, S>, T> p) {
        return (FlippedBundle<A, B, S, T>) p;
      }

      static <A, B, S, T> FlippedBundle<A, B, S, T> resolve(
          final A2<FlippedBundle.Mu2<A, B>, S, T> p) {
        return (FlippedBundle<A, B, S, T>) p;
      }

      static <A, B, S, T> FlippedBundle<A, B, S, T> resolve(
          final A3<FlippedBundle.Mu3<A>, B, S, T> p) {
        return (FlippedBundle<A, B, S, T>) p;
      }

      static <A, B, S, T> FlippedBundle<A, B, S, T> resolve(
          final A4<FlippedBundle.Mu4, A, B, S, T> p) {
        return (FlippedBundle<A, B, S, T>) p;
      }

      public final Function<S, Either<T, A>> match;
      public final Function<B, T> review;

      public FlippedBundle(Function<S, Either<T, A>> match, Function<B, T> review) {
        this.match = match;
        this.review = review;
      }

      public Bundle<S, T, A, B> flip() {
        return new Bundle<>(this.match, this.review);
      }

      public static final class Instances {
        public static <A, B>
            Profunctor<? extends Profunctor.Mu, FlippedBundle.Mu2<A, B>> profunctor() {
          return new FlippedBundle.Instances.ChoiceI<>();
        }

        public static <A, B> Choice<? extends Choice.Mu, FlippedBundle.Mu2<A, B>> choice() {
          return new FlippedBundle.Instances.ChoiceI<>();
        }

        private static class ChoiceI<Mu extends ChoiceI.Mu, A, B>
            implements Profunctor<Mu, FlippedBundle.Mu2<A, B>>,
                Choice<Mu, FlippedBundle.Mu2<A, B>> {
          public static class Mu implements Choice.Mu {}

          @Override
          public <S, T, S2, T2> A2<FlippedBundle.Mu2<A, B>, S2, T2> dimap(
              final Function<? super S2, ? extends S> s2s,
              final Function<? super T, ? extends T2> tt2,
              final A2<FlippedBundle.Mu2<A, B>, S, T> p) {
            final FlippedBundle<A, B, S, T> fb = FlippedBundle.resolve(p);
            return new FlippedBundle<>(
                s2 -> fb.match.apply(s2s.apply(s2)).mapLeft(tt2),
                b -> tt2.apply(fb.review.apply(b)));
          }

          @Override
          public <S, T, C> A2<FlippedBundle.Mu2<A, B>, Either<S, C>, Either<T, C>> left(
              final A2<FlippedBundle.Mu2<A, B>, S, T> p) {
            final FlippedBundle<A, B, S, T> fb = FlippedBundle.resolve(p);
            return new FlippedBundle<>(
                e ->
                    e.either(
                        s -> fb.match.apply(s).mapLeft(Either::left),
                        c -> Either.left(Either.right(c))),
                b -> Either.left(fb.review.apply(b)));
          }

          @Override
          public <S, T, C> A2<FlippedBundle.Mu2<A, B>, Either<C, S>, Either<C, T>> right(
              final A2<FlippedBundle.Mu2<A, B>, S, T> p) {
            final FlippedBundle<A, B, S, T> fb = FlippedBundle.resolve(p);
            return new FlippedBundle<>(
                e ->
                    e.either(
                        c -> Either.left(Either.left(c)),
                        s -> fb.match.apply(s).mapLeft(Either::right)),
                b -> Either.right(fb.review.apply(b)));
          }
        }
      }
    }
  }

  /**
   * A CPS-transformed bundle of a pair of functions {@code match :: s -> Either t a} and {@code set
   * :: s -> b -> t}, given by an {@code Prism}.
   */
  @FunctionalInterface
  public static interface UnpackPrism<A, B, S, T>
      extends A1<UnpackPrism.Mu<A, B, S>, T>,
          A2<UnpackPrism.Mu2<A, B>, S, T>,
          A3<UnpackPrism.Mu3<A>, B, S, T>,
          A4<UnpackPrism.Mu4, A, B, S, T> {

    <R> R withUnpackPrism(
        final BiFunction<? super Function<S, Either<T, A>>, ? super Function<B, T>, ? extends R> k);

    static final class Mu<A, B, S> implements K1 {}

    static final class Mu2<A, B> implements K2 {}

    static final class Mu3<A> implements K3 {}

    static final class Mu4 implements K4 {}

    static <A, B, S, T> UnpackPrism<A, B, S, T> resolve(final A1<UnpackPrism.Mu<A, B, S>, T> p) {
      return (UnpackPrism<A, B, S, T>) p;
    }

    static <A, B, S, T> UnpackPrism<A, B, S, T> resolve(final A2<UnpackPrism.Mu2<A, B>, S, T> p) {
      return (UnpackPrism<A, B, S, T>) p;
    }

    static <A, B, S, T> UnpackPrism<A, B, S, T> resolve(final A3<UnpackPrism.Mu3<A>, B, S, T> p) {
      return (UnpackPrism<A, B, S, T>) p;
    }

    static <A, B, S, T> UnpackPrism<A, B, S, T> resolve(final A4<UnpackPrism.Mu4, A, B, S, T> p) {
      return (UnpackPrism<A, B, S, T>) p;
    }

    static final class Instances {
      public static <A, B> Profunctor<? extends Profunctor.Mu, UnpackPrism.Mu2<A, B>> profunctor() {
        return new UnpackPrism.Instances.ChoiceI<>();
      }

      public static <A, B> Choice<? extends Choice.Mu, UnpackPrism.Mu2<A, B>> choice() {
        return new UnpackPrism.Instances.ChoiceI<>();
      }

      private static class ChoiceI<Mu extends ChoiceI.Mu, A, B>
          implements Profunctor<Mu, UnpackPrism.Mu2<A, B>>, Choice<Mu, UnpackPrism.Mu2<A, B>> {
        public static class Mu implements Choice.Mu {}

        @Override
        public <S, T, S2, T2> A2<UnpackPrism.Mu2<A, B>, S2, T2> dimap(
            final Function<? super S2, ? extends S> s2s,
            final Function<? super T, ? extends T2> tt2,
            final A2<UnpackPrism.Mu2<A, B>, S, T> p) {
          return UnpackPrism.resolve(p)
              .withUnpackPrism(
                  (match, review) -> {
                    return new UnpackPrism<A, B, S2, T2>() {
                      @Override
                      public <R> R withUnpackPrism(
                          final BiFunction<
                                  ? super Function<S2, Either<T2, A>>,
                                  ? super Function<B, T2>,
                                  ? extends R>
                              k) {
                        final Function<S2, Either<T2, A>> match2 =
                            s2 -> match.apply(s2s.apply(s2)).mapLeft(tt2);
                        final Function<B, T2> review2 = b -> tt2.apply(review.apply(b));
                        return k.apply(match2, review2);
                      }
                    };
                  });
        }

        @Override
        public <S, T, C> A2<UnpackPrism.Mu2<A, B>, Either<S, C>, Either<T, C>> left(
            final A2<UnpackPrism.Mu2<A, B>, S, T> p) {
          return UnpackPrism.resolve(p)
              .withUnpackPrism(
                  (match, review) -> {
                    return new UnpackPrism<A, B, Either<S, C>, Either<T, C>>() {
                      @Override
                      public <R> R withUnpackPrism(
                          final BiFunction<
                                  ? super Function<Either<S, C>, Either<Either<T, C>, A>>,
                                  ? super Function<B, Either<T, C>>,
                                  ? extends R>
                              k) {
                        final Function<Either<S, C>, Either<Either<T, C>, A>> match2 =
                            e ->
                                e.either(
                                    s -> match.apply(s).mapLeft(Either::left),
                                    c -> Either.left(Either.right(c)));
                        final Function<B, Either<T, C>> review2 = b -> Either.left(review.apply(b));
                        return k.apply(match2, review2);
                      }
                    };
                  });
        }

        @Override
        public <S, T, C> A2<UnpackPrism.Mu2<A, B>, Either<C, S>, Either<C, T>> right(
            final A2<UnpackPrism.Mu2<A, B>, S, T> p) {
          return UnpackPrism.resolve(p)
              .withUnpackPrism(
                  (match, review) -> {
                    return new UnpackPrism<A, B, Either<C, S>, Either<C, T>>() {
                      @Override
                      public <R> R withUnpackPrism(
                          final BiFunction<
                                  ? super Function<Either<C, S>, Either<Either<C, T>, A>>,
                                  ? super Function<B, Either<C, T>>,
                                  ? extends R>
                              k) {
                        final Function<Either<C, S>, Either<Either<C, T>, A>> match2 =
                            e ->
                                e.either(
                                    c -> Either.left(Either.left(c)),
                                    s -> match.apply(s).mapLeft(Either::right));
                        final Function<B, Either<C, T>> review2 =
                            b -> Either.right(review.apply(b));
                        return k.apply(match2, review2);
                      }
                    };
                  });
        }
      }
    }
  }
}
