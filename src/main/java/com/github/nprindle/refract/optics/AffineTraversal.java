package com.github.nprindle.refract.optics;

import com.github.nprindle.refract.classes.AffineTraversing;
import com.github.nprindle.refract.d17n.A1;
import com.github.nprindle.refract.d17n.A2;
import com.github.nprindle.refract.d17n.K2;

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
}
