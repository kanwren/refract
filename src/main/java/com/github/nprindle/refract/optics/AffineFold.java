package com.github.nprindle.refract.optics;

import com.github.nprindle.refract.classes.AffineFolding;
import com.github.nprindle.refract.d17n.A1;
import com.github.nprindle.refract.d17n.A2;
import com.github.nprindle.refract.d17n.K2;

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
}
