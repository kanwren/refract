package com.github.nprindle.refract.classes;

import com.github.nprindle.refract.d17n.A1;
import com.github.nprindle.refract.d17n.A2;
import com.github.nprindle.refract.d17n.K1;
import com.github.nprindle.refract.d17n.K2;
import com.github.nprindle.refract.data.Unit;

public interface Folding<Mu extends Folding.Mu, P extends K2>
    extends Traversing<Mu, P>, Getting<Mu, P> {
  public static interface Mu extends Traversing.Mu, Getting.Mu {}

  public static <Mu extends Folding.Mu, P extends K2> Folding<Mu, P> resolve(final A1<Mu, P> p) {
    return (Folding<Mu, P>) p;
  }

  default <F extends K1, A, B> A2<P, A1<F, A>, Unit> traverseP_(
      final Foldable<? extends Foldable.Mu, F> foldable, final A2<P, A, A> p) {
    return wander(foldable::traverse_, p);
  }
}
