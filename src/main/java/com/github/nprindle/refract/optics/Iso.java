package com.github.nprindle.refract.optics;

import com.github.nprindle.refract.classes.Profunctor;
import com.github.nprindle.refract.d17n.A1;
import com.github.nprindle.refract.d17n.A2;
import com.github.nprindle.refract.d17n.A3;
import com.github.nprindle.refract.d17n.A4;
import com.github.nprindle.refract.d17n.K1;
import com.github.nprindle.refract.d17n.K2;
import com.github.nprindle.refract.d17n.K3;
import com.github.nprindle.refract.d17n.K4;
import com.github.nprindle.refract.profunctors.Coexp;
import java.util.function.BiFunction;
import java.util.function.Function;

public interface Iso<S, T, A, B> extends Optic<Profunctor.Mu, S, T, A, B> {
  static <S, T, A, B> Iso<S, T, A, B> fromOptic(
      final Optic<? super Profunctor.Mu, S, T, A, B> optic) {
    return new Iso<S, T, A, B>() {
      @Override
      public <P extends K2> A2<P, S, T> runOptic(
          final A1<? extends Profunctor.Mu, P> dict, final A2<P, A, B> rel) {
        return optic.runOptic(dict, rel);
      }
    };
  }

  static <S, T, A, B> Iso<S, T, A, B> iso(
      final Function<? super S, ? extends A> fw, final Function<? super B, ? extends T> bw) {
    return new Iso<S, T, A, B>() {
      @Override
      public <P extends K2> A2<P, S, T> runOptic(
          final A1<? extends Profunctor.Mu, P> dict, final A2<P, A, B> rel) {
        final Profunctor<? extends Profunctor.Mu, P> profunctor = Profunctor.resolve(dict);
        return profunctor.dimap(fw, bw, rel);
      }
    };
  }

  default <R> R withIso(
      final BiFunction<? super Function<S, A>, ? super Function<B, T>, ? extends R> k) {
    final A2<Coexp.Mu2<B, A>, A, B> c = Coexp.identity();
    return Coexp.resolve(this.runOptic(Coexp.Instances.profunctor(), c))
        .runCoexp((bt, sa) -> k.apply(sa, bt));
  }

  default Iso.Bundle<S, T, A, B> bundle() {
    final Iso.Bundle.FlippedBundle<A, B, A, B> trivialFlippedBundle =
        new Iso.Bundle.FlippedBundle<>(a -> a, b -> b);
    final Iso.Bundle.FlippedBundle<A, B, S, T> flippedBundle =
        Iso.Bundle.FlippedBundle.resolve(
            this.runOptic(Iso.Bundle.FlippedBundle.Instances.profunctor(), trivialFlippedBundle));
    return flippedBundle.flip();
  }

  default Iso<B, A, T, S> from() {
    // TODO: optimize this
    return this.withIso((sa, bt) -> Iso.iso(bt, sa));
  }

  static <A> Iso<A, A, A, A> involuted(final Function<A, A> fun) {
    return Iso.iso(fun, fun);
  }

  /**
   * A bundle of functions given by an {@code Iso}. This can also be seen as {@code Coexp} without a
   * CPS transform.
   */
  public static final class Bundle<S, T, A, B> {
    public final Function<S, A> view;
    public final Function<B, T> review;

    public Bundle(Function<S, A> view, Function<B, T> review) {
      this.view = view;
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

      public final Function<S, A> view;
      public final Function<B, T> review;

      public FlippedBundle(Function<S, A> view, Function<B, T> review) {
        this.view = view;
        this.review = review;
      }

      public Bundle<S, T, A, B> flip() {
        return new Bundle<>(this.view, this.review);
      }

      public static final class Instances {
        public static <A, B>
            Profunctor<? extends Profunctor.Mu, FlippedBundle.Mu2<A, B>> profunctor() {
          return new FlippedBundle.Instances.ProfunctorI<>();
        }

        private static class ProfunctorI<Mu extends ProfunctorI.Mu, A, B>
            implements Profunctor<Mu, FlippedBundle.Mu2<A, B>> {
          public static class Mu implements Profunctor.Mu {}

          @Override
          public <S, T, S2, T2> A2<FlippedBundle.Mu2<A, B>, S2, T2> dimap(
              final Function<? super S2, ? extends S> s2s,
              final Function<? super T, ? extends T2> tt2,
              final A2<FlippedBundle.Mu2<A, B>, S, T> p) {
            final FlippedBundle<A, B, S, T> fb = FlippedBundle.resolve(p);
            return new FlippedBundle<>(
                s2 -> fb.view.apply(s2s.apply(s2)), b -> tt2.apply(fb.review.apply(b)));
          }
        }
      }
    }
  }
}
