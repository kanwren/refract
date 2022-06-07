package com.github.nprindle.refract.optics;

import com.github.nprindle.refract.classes.AffineFolding;
import com.github.nprindle.refract.d17n.A1;
import com.github.nprindle.refract.d17n.A2;
import com.github.nprindle.refract.d17n.K2;
import com.github.nprindle.refract.data.Either;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

public interface AffineFold<S, A> extends Optic<AffineFolding.Mu, S, S, A, A> {
  static <S, A> AffineFold<S, A> fromOptic(
      final Optic<? super AffineFolding.Mu, S, S, A, A> optic) {
    return new AffineFold<S, A>() {
      @Override
      public <P extends K2> A2<P, S, S> runOptic(
          final A1<? extends AffineFolding.Mu, P> dict, final A2<P, A, A> rel) {
        return optic.runOptic(dict, rel);
      }
    };
  }

  static <S, A> AffineFold<S, A> folding(final Function<? super S, ? extends Optional<A>> preview) {
    return new AffineFold<S, A>() {
      @Override
      public <P extends K2> A2<P, S, S> runOptic(
          final A1<? extends AffineFolding.Mu, P> dict, final A2<P, A, A> rel) {
        final AffineFolding<? extends AffineFolding.Mu, P> af = AffineFolding.resolve(dict);
        return af.cimap(
            s -> preview.apply(s).map(a -> Either.<S, A>right(a)).orElseGet(() -> Either.left(s)),
            Either::left,
            af.right(rel));
      }
    };
  }

  static <A> AffineFold<A, A> filtered(final Predicate<? super A> pred) {
    return AffineFold.folding(a -> pred.test(a) ? Optional.of(a) : Optional.empty());
  }
}
