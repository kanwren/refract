package com.github.nprindle.refract.optics;

import com.github.nprindle.refract.classes.Strong;
import com.github.nprindle.refract.d17n.A1;
import com.github.nprindle.refract.d17n.A2;
import com.github.nprindle.refract.d17n.K2;

public interface Lens<S, T, A, B> extends Optic<Strong.Mu, S, T, A, B> {
  static <S, T, A, B> Lens<S, T, A, B> fromOptic(final Optic<? super Strong.Mu, S, T, A, B> optic) {
    return new Lens<S, T, A, B>() {
      public <P extends K2> A2<P, S, T> runOptic(
          final A1<? extends Strong.Mu, P> dict, final A2<P, A, B> rel) {
        return optic.runOptic(dict, rel);
      }
    };
  }
}
