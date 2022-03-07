package com.github.nprindle.refract.classes;

import com.github.nprindle.refract.d17n.A1;
import com.github.nprindle.refract.d17n.K2;

public interface AffineFolding<Mu extends AffineFolding.Mu, P extends K2>
    extends AffineTraversing<Mu, P>, Getting<Mu, P> {
  public static interface Mu extends AffineTraversing.Mu, Getting.Mu {}

  public static <Mu extends AffineFolding.Mu, P extends K2> AffineFolding<Mu, P> resolve(
      final A1<Mu, P> p) {
    return (AffineFolding<Mu, P>) p;
  }
}
