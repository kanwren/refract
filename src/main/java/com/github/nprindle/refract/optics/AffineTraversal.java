package com.github.nprindle.refract.optics;

import com.github.nprindle.refract.classes.AffineTraversing;
import com.github.nprindle.refract.d17n.A1;
import com.github.nprindle.refract.d17n.A2;
import com.github.nprindle.refract.d17n.K2;
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

  default Either<T, A> matching(final S s) {
    throw new Error("not implemented"); // TODO
  }

  default boolean is(final S s) {
    return this.matching(s).isRight();
  }

  static <S, A> AffineTraversal<S, S, A, A> ignored() {
    return AffineTraversal.simpleAffineTraversal(s -> Optional.empty(), (s, b) -> s);
  }

  // TODO:
  // withOptional :: Optional s t a b -> ((s -> Either t a) -> (s -> b -> t) -> r) -> r
  // matching :: Optional s t a b -> s -> Either t a
  // ignored :: Optional' s a
}
