package com.github.nprindle.refract.classes;

import com.github.nprindle.refract.d17n.A1;
import com.github.nprindle.refract.d17n.A2;
import com.github.nprindle.refract.d17n.K2;
import com.github.nprindle.refract.data.Unit;

public interface Getting<Mu extends Getting.Mu, P extends K2>
    extends Strong<Mu, P>, Cochoice<Mu, P>, Bicontravariant<Mu, P> {
  public static interface Mu extends Strong.Mu, Cochoice.Mu, Bicontravariant.Mu {}

  public static <Mu extends Getting.Mu, P extends K2> Getting<Mu, P> resolve(final A1<Mu, P> p) {
    return (Getting<Mu, P>) p;
  }

  /**
   * If {@code P} is both a {@code Profunctor} and {@code Bicontravariant}, then the right argument
   * can be eliminated, since it is both contravariant and covariant.
   */
  default <A, B, C> A2<P, A, C> rphantom(final A2<P, A, B> p) {
    return csecond(x -> Unit.UNIT, rmap(x -> Unit.UNIT, p));
  }
}
