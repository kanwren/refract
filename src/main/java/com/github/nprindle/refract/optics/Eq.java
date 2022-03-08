package com.github.nprindle.refract.optics;

import com.github.nprindle.refract.d17n.A1;
import com.github.nprindle.refract.d17n.A2;
import com.github.nprindle.refract.d17n.C2;
import com.github.nprindle.refract.d17n.K2;

interface Eq<S, T, A, B> extends Optic<C2.Mu, S, T, A, B> {
  static <A, B> Eq<A, B, A, B> refl() {
    return new Eq<A, B, A, B>() {
      @Override
      public <P extends K2> A2<P, A, B> runOptic(
          final A1<? extends C2.Mu, P> dict, final A2<P, A, B> rel) {
        return rel;
      }
    };
  }
}
