package com.github.nprindle.refract.optics;

import com.github.nprindle.refract.classes.Strong;
import com.github.nprindle.refract.d17n.A1;
import com.github.nprindle.refract.d17n.A2;
import com.github.nprindle.refract.d17n.K2;
import com.github.nprindle.refract.data.Pair;
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

  // TODO:
  // withLens :: Lens s t a b -> ((s -> a) -> (s -> b -> t) -> r) -> r
  // united :: Lens' a ()
  // lenses for pair
}
