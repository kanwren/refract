package com.github.nprindle.refract.optics;

import com.github.nprindle.refract.classes.Profunctor;
import com.github.nprindle.refract.d17n.A1;
import com.github.nprindle.refract.d17n.A2;
import com.github.nprindle.refract.d17n.K2;
import com.github.nprindle.refract.profunctors.Coexp;
import java.util.function.BiFunction;
import java.util.function.Function;

public interface Iso<S, T, A, B> extends Optic<Profunctor.Mu, S, T, A, B> {
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

  default Iso<B, A, T, S> from() {
    // TODO: optimize this
    return this.withIso((sa, bt) -> Iso.iso(bt, sa));
  }

  static <A> Iso<A, A, A, A> involuted(final Function<A, A> fun) {
    return Iso.iso(fun, fun);
  }
}
