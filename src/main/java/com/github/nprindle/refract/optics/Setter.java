package com.github.nprindle.refract.optics;

import com.github.nprindle.refract.classes.Mapping;
import com.github.nprindle.refract.d17n.A1;
import com.github.nprindle.refract.d17n.A2;
import com.github.nprindle.refract.d17n.K2;
import com.github.nprindle.refract.profunctors.Func;
import java.util.function.Function;

public interface Setter<S, T, A, B> extends Optic<Mapping.Mu, S, T, A, B> {
  static <S, T, A, B> Setter<S, T, A, B> fromOptic(
      final Optic<? super Mapping.Mu, S, T, A, B> optic) {
    return new Setter<S, T, A, B>() {
      @Override
      public <P extends K2> A2<P, S, T> runOptic(
          final A1<? extends Mapping.Mu, P> dict, final A2<P, A, B> rel) {
        return optic.runOptic(dict, rel);
      }
    };
  }

  default T over(final Function<? super A, ? extends B> f, final S s) {
    return Func.resolve(this.runOptic(Func.Instances.mapping(), Func.from(f))).apply(s);
  }

  // TODO:
  // sets :: ((a -> b) -> s -> t) -> Setter s t a b
  // mapped :: Functor f => Setter (f a) (f b) a b
  // contramapped :: Contravariant f => Setter (f a) (f b) b a
  // over :: Setter s t a b -> (a -> b) -> s -> t
  // set :: Setter s t a b -> b -> s -> t
}
