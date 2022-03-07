package com.github.nprindle.refract.classes;

import com.github.nprindle.refract.d17n.A1;
import com.github.nprindle.refract.d17n.K2;

public interface Folding<Mu extends Folding.Mu, P extends K2>
    extends Traversing<Mu, P>, Getting<Mu, P> {
  public static interface Mu extends Traversing.Mu, Getting.Mu {}

  public static <Mu extends Folding.Mu, P extends K2> Folding<Mu, P> resolve(final A1<Mu, P> p) {
    return (Folding<Mu, P>) p;
  }
}
