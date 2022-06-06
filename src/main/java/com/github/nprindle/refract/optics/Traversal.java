package com.github.nprindle.refract.optics;

import com.github.nprindle.refract.classes.Traversing;
import com.github.nprindle.refract.d17n.A1;
import com.github.nprindle.refract.d17n.A2;
import com.github.nprindle.refract.d17n.K2;

public interface Traversal<S, T, A, B> extends Optic<Traversing.Mu, S, T, A, B> {
  static <S, T, A, B> Traversal<S, T, A, B> fromOptic(
      final Optic<? super Traversing.Mu, S, T, A, B> optic) {
    return new Traversal<S, T, A, B>() {
      public <P extends K2> A2<P, S, T> runOptic(
          final A1<? extends Traversing.Mu, P> dict, final A2<P, A, B> rel) {
        return optic.runOptic(dict, rel);
      }
    };
  }
}
