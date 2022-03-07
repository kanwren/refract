package com.github.nprindle.refract.classes;

import com.github.nprindle.refract.d17n.A1;
import com.github.nprindle.refract.d17n.K2;

public interface AffineTraversing<Mu extends AffineTraversing.Mu, P extends K2>
    extends Strong<Mu, P>, Choice<Mu, P> {
  public static interface Mu extends Strong.Mu, Choice.Mu {}

  public static <Mu extends AffineTraversing.Mu, P extends K2> AffineTraversing<Mu, P> resolve(
      final A1<Mu, P> p) {
    return (AffineTraversing<Mu, P>) p;
  }
}
