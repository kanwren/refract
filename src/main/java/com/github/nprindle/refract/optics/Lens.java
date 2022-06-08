package com.github.nprindle.refract.optics;

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
import com.github.nprindle.refract.data.Pair;
import com.github.nprindle.refract.data.Unit;
import java.util.function.BiFunction;
import java.util.function.Function;

public interface Lens<S, T, A, B> extends Optic<Strong.Mu, S, T, A, B> {
  static <S, T, A, B> Lens<S, T, A, B> fromOptic(final Optic<? super Strong.Mu, S, T, A, B> optic) {
    return new Lens<S, T, A, B>() {
      @Override
      public <P extends K2> A2<P, S, T> runOptic(
          final A1<? extends Strong.Mu, P> dict, final A2<P, A, B> rel) {
        return optic.runOptic(dict, rel);
      }
    };
  }

  static <S, T, A, B> Lens<S, T, A, B> lens(
      final Function<? super S, ? extends A> getter,
      final BiFunction<? super S, ? super B, ? extends T> setter) {
    return new Lens<S, T, A, B>() {
      @Override
      public <P extends K2> A2<P, S, T> runOptic(
          final A1<? extends Strong.Mu, P> dict, final A2<P, A, B> rel) {
        final Strong<? extends Strong.Mu, P> strong = Strong.resolve(dict);
        return strong.dimap(
            s -> Pair.of(s, getter.apply(s)),
            t -> setter.apply(t.fst(), t.snd()),
            strong.second(rel));
      }
    };
  }

  default <R> R withLens(
      final BiFunction<? super Function<S, A>, ? super BiFunction<S, B, T>, R> k) {
    final UnpackLens<A, B, A, B> trivialUnpackLens =
        new UnpackLens<A, B, A, B>() {
          @Override
          public <R> R withUnpackLens(
              final BiFunction<? super Function<A, A>, ? super BiFunction<A, B, B>, ? extends R>
                  k) {
            final Function<A, A> view = a -> a;
            final BiFunction<A, B, B> set = (a, b) -> b;
            return k.apply(view, set);
          }
        };
    return UnpackLens.resolve(this.runOptic(UnpackLens.Instances.strong(), trivialUnpackLens))
        .withUnpackLens(k);
  }

  default Lens.Bundle<S, T, A, B> bundle() {
    final Lens.Bundle.FlippedBundle<A, B, A, B> trivialFlippedBundle =
        new Lens.Bundle.FlippedBundle<>(a -> a, (a, b) -> b);
    final Lens.Bundle.FlippedBundle<A, B, S, T> flippedBundle =
        Lens.Bundle.FlippedBundle.resolve(
            this.runOptic(Lens.Bundle.FlippedBundle.Instances.strong(), trivialFlippedBundle));
    return flippedBundle.flip();
  }

  static <A> Lens<A, A, Unit, Unit> united() {
    return Lens.lens(a -> Unit.UNIT, (a, u) -> a);
  }

  /**
   * A bundle of functions given by a {@code Lens}. This can also be seen as {@code UnpackLens}
   * without a CPS transform.
   */
  public static final class Bundle<S, T, A, B> {
    public final Function<S, A> view;
    public final BiFunction<S, B, T> set;

    public Bundle(Function<S, A> view, BiFunction<S, B, T> set) {
      this.view = view;
      this.set = set;
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

      public final Function<S, A> view;
      public final BiFunction<S, B, T> set;

      public FlippedBundle(Function<S, A> view, BiFunction<S, B, T> set) {
        this.view = view;
        this.set = set;
      }

      public Bundle<S, T, A, B> flip() {
        return new Bundle<>(this.view, this.set);
      }

      public static final class Instances {
        public static <A, B>
            Profunctor<? extends Profunctor.Mu, FlippedBundle.Mu2<A, B>> profunctor() {
          return new FlippedBundle.Instances.StrongI<>();
        }

        public static <A, B> Strong<? extends Strong.Mu, FlippedBundle.Mu2<A, B>> strong() {
          return new FlippedBundle.Instances.StrongI<>();
        }

        private static class StrongI<Mu extends StrongI.Mu, A, B>
            implements Profunctor<Mu, FlippedBundle.Mu2<A, B>>,
                Strong<Mu, FlippedBundle.Mu2<A, B>> {
          public static class Mu implements Strong.Mu {}

          @Override
          public <S, T, S2, T2> A2<FlippedBundle.Mu2<A, B>, S2, T2> dimap(
              final Function<? super S2, ? extends S> s2s,
              final Function<? super T, ? extends T2> tt2,
              final A2<FlippedBundle.Mu2<A, B>, S, T> p) {
            final FlippedBundle<A, B, S, T> fb = FlippedBundle.resolve(p);
            return new FlippedBundle<>(
                s2 -> fb.view.apply(s2s.apply(s2)),
                (s2, b) -> tt2.apply(fb.set.apply(s2s.apply(s2), b)));
          }

          @Override
          public <S, T, C> A2<FlippedBundle.Mu2<A, B>, Pair<S, C>, Pair<T, C>> first(
              final A2<FlippedBundle.Mu2<A, B>, S, T> p) {
            final FlippedBundle<A, B, S, T> fb = FlippedBundle.resolve(p);
            return new FlippedBundle<>(
                pair -> fb.view.apply(pair.fst()),
                (pair, b) -> Pair.of(fb.set.apply(pair.fst(), b), pair.snd()));
          }

          @Override
          public <S, T, C> A2<FlippedBundle.Mu2<A, B>, Pair<C, S>, Pair<C, T>> second(
              final A2<FlippedBundle.Mu2<A, B>, S, T> p) {
            final FlippedBundle<A, B, S, T> fb = FlippedBundle.resolve(p);
            return new FlippedBundle<>(
                pair -> fb.view.apply(pair.snd()),
                (pair, b) -> Pair.of(pair.fst(), fb.set.apply(pair.snd(), b)));
          }
        }
      }
    }
  }

  /**
   * A CPS-transformed bundle of a pair of functions {@code view :: s -> a} and {@code set :: s -> b
   * -> t}, given by a {@code Lens}.
   */
  @FunctionalInterface
  public static interface UnpackLens<A, B, S, T>
      extends A1<UnpackLens.Mu<A, B, S>, T>,
          A2<UnpackLens.Mu2<A, B>, S, T>,
          A3<UnpackLens.Mu3<A>, B, S, T>,
          A4<UnpackLens.Mu4, A, B, S, T> {

    <R> R withUnpackLens(
        final BiFunction<? super Function<S, A>, ? super BiFunction<S, B, T>, ? extends R> k);

    static final class Mu<A, B, S> implements K1 {}

    static final class Mu2<A, B> implements K2 {}

    static final class Mu3<A> implements K3 {}

    static final class Mu4 implements K4 {}

    static <A, B, S, T> UnpackLens<A, B, S, T> resolve(final A1<UnpackLens.Mu<A, B, S>, T> p) {
      return (UnpackLens<A, B, S, T>) p;
    }

    static <A, B, S, T> UnpackLens<A, B, S, T> resolve(final A2<UnpackLens.Mu2<A, B>, S, T> p) {
      return (UnpackLens<A, B, S, T>) p;
    }

    static <A, B, S, T> UnpackLens<A, B, S, T> resolve(final A3<UnpackLens.Mu3<A>, B, S, T> p) {
      return (UnpackLens<A, B, S, T>) p;
    }

    static <A, B, S, T> UnpackLens<A, B, S, T> resolve(final A4<UnpackLens.Mu4, A, B, S, T> p) {
      return (UnpackLens<A, B, S, T>) p;
    }

    static final class Instances {
      public static <A, B> Profunctor<? extends Profunctor.Mu, UnpackLens.Mu2<A, B>> profunctor() {
        return new UnpackLens.Instances.StrongI<>();
      }

      public static <A, B> Strong<? extends Strong.Mu, UnpackLens.Mu2<A, B>> strong() {
        return new UnpackLens.Instances.StrongI<>();
      }

      private static class StrongI<Mu extends StrongI.Mu, A, B>
          implements Profunctor<Mu, UnpackLens.Mu2<A, B>>, Strong<Mu, UnpackLens.Mu2<A, B>> {
        public static class Mu implements Strong.Mu {}

        @Override
        public <S, T, S2, T2> A2<UnpackLens.Mu2<A, B>, S2, T2> dimap(
            final Function<? super S2, ? extends S> s2s,
            final Function<? super T, ? extends T2> tt2,
            final A2<UnpackLens.Mu2<A, B>, S, T> p) {
          return UnpackLens.resolve(p)
              .withUnpackLens(
                  (view, set) -> {
                    return new UnpackLens<A, B, S2, T2>() {
                      @Override
                      public <R> R withUnpackLens(
                          final BiFunction<
                                  ? super Function<S2, A>,
                                  ? super BiFunction<S2, B, T2>,
                                  ? extends R>
                              k) {
                        final Function<S2, A> view2 = s2 -> view.apply(s2s.apply(s2));
                        final BiFunction<S2, B, T2> set2 =
                            (s2, b) -> tt2.apply(set.apply(s2s.apply(s2), b));
                        return k.apply(view2, set2);
                      }
                    };
                  });
        }

        @Override
        public <S, T, C> A2<UnpackLens.Mu2<A, B>, Pair<S, C>, Pair<T, C>> first(
            final A2<UnpackLens.Mu2<A, B>, S, T> p) {
          return UnpackLens.resolve(p)
              .withUnpackLens(
                  (view, set) -> {
                    return new UnpackLens<A, B, Pair<S, C>, Pair<T, C>>() {
                      @Override
                      public <R> R withUnpackLens(
                          final BiFunction<
                                  ? super Function<Pair<S, C>, A>,
                                  ? super BiFunction<Pair<S, C>, B, Pair<T, C>>,
                                  ? extends R>
                              k) {
                        final Function<Pair<S, C>, A> view2 = pair -> view.apply(pair.fst());
                        final BiFunction<Pair<S, C>, B, Pair<T, C>> set2 =
                            (pair, b) -> Pair.of(set.apply(pair.fst(), b), pair.snd());
                        return k.apply(view2, set2);
                      }
                    };
                  });
        }

        @Override
        public <S, T, C> A2<UnpackLens.Mu2<A, B>, Pair<C, S>, Pair<C, T>> second(
            final A2<UnpackLens.Mu2<A, B>, S, T> p) {
          return UnpackLens.resolve(p)
              .withUnpackLens(
                  (view, set) -> {
                    return new UnpackLens<A, B, Pair<C, S>, Pair<C, T>>() {
                      @Override
                      public <R> R withUnpackLens(
                          final BiFunction<
                                  ? super Function<Pair<C, S>, A>,
                                  ? super BiFunction<Pair<C, S>, B, Pair<C, T>>,
                                  ? extends R>
                              k) {
                        final Function<Pair<C, S>, A> view2 = pair -> view.apply(pair.snd());
                        final BiFunction<Pair<C, S>, B, Pair<C, T>> set2 =
                            (pair, b) -> Pair.of(pair.fst(), set.apply(pair.snd(), b));
                        return k.apply(view2, set2);
                      }
                    };
                  });
        }
      }
    }
  }
}
