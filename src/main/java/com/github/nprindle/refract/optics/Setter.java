package com.github.nprindle.refract.optics;

import com.github.nprindle.refract.classes.Contravariant;
import com.github.nprindle.refract.classes.Functor;
import com.github.nprindle.refract.classes.Mapping;
import com.github.nprindle.refract.d17n.A1;
import com.github.nprindle.refract.d17n.A2;
import com.github.nprindle.refract.d17n.K1;
import com.github.nprindle.refract.d17n.K2;
import com.github.nprindle.refract.profunctors.Func;
import java.util.function.BiFunction;
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

  // `BiFunction<Function<A, B>, S, T>` is equivalent to `Roam<S, T, A, B>`, but
  // `BiFunction` is exposed here for convenience
  static <S, T, A, B> Setter<S, T, A, B> sets(
      final BiFunction<? super Function<? super A, ? extends B>, ? super S, ? extends T> setter) {
    return new Setter<S, T, A, B>() {
      @Override
      public <P extends K2> A2<P, S, T> runOptic(
          final A1<? extends Mapping.Mu, P> dict, final A2<P, A, B> rel) {
        final Mapping<? extends Mapping.Mu, P> mapping = Mapping.resolve(dict);
        return mapping.roam((f, s) -> setter.apply(f, s), rel);
      }
    };
  }

  static <F extends K1, A, B> Setter<A1<F, A>, A1<F, B>, A, B> mapped(
      final Functor<? extends Functor.Mu, F> functor) {
    return new Setter<A1<F, A>, A1<F, B>, A, B>() {
      @Override
      public <P extends K2> A2<P, A1<F, A>, A1<F, B>> runOptic(
          final A1<? extends Mapping.Mu, P> dict, final A2<P, A, B> rel) {
        final Mapping<? extends Mapping.Mu, P> mapping = Mapping.resolve(dict);
        return mapping.roam((f, s) -> functor.map(f, s), rel);
      }
    };
  }

  static <F extends K1, A, B> Setter<A1<F, A>, A1<F, B>, B, A> cmapped(
      final Contravariant<? extends Contravariant.Mu, F> contravariant) {
    return new Setter<A1<F, A>, A1<F, B>, B, A>() {
      @Override
      public <P extends K2> A2<P, A1<F, A>, A1<F, B>> runOptic(
          final A1<? extends Mapping.Mu, P> dict, final A2<P, B, A> rel) {
        final Mapping<? extends Mapping.Mu, P> mapping = Mapping.resolve(dict);
        return mapping.roam((f, s) -> contravariant.cmap(f, s), rel);
      }
    };
  }

  default T over(final Function<? super A, ? extends B> f, final S s) {
    return Func.resolve(this.runOptic(Func.Instances.mapping(), Func.from(f))).apply(s);
  }

  default T set(final B b, final S s) {
    return this.over(a -> b, s);
  }
}
