package com.github.nprindle.refract.classes;

import com.github.nprindle.refract.d17n.A1;
import com.github.nprindle.refract.d17n.A2;
import com.github.nprindle.refract.d17n.K2;
import com.github.nprindle.refract.data.Void;
import java.util.function.Function;

public interface Reviewing<Mu extends Reviewing.Mu, P extends K2>
    extends Choice<Mu, P>, Costrong<Mu, P>, Bifunctor<Mu, P> {
  public static interface Mu extends Choice.Mu, Costrong.Mu, Bifunctor.Mu {}

  public static <Mu extends Reviewing.Mu, P extends K2> Reviewing<Mu, P> resolve(
      final A1<Mu, P> p) {
    return (Reviewing<Mu, P>) p;
  }

  /**
   * If {@code P} is both a {@code Profunctor} and {@code Bivariant}, then the left argument can be
   * eliminated, since it is both contravariant and covariant.
   */
  default <A, B, C> A2<P, C, B> lphantom(final A2<P, A, B> p) {
    final Function<Void, A> f = Void::absurd;
    final Function<Void, C> g = Void::absurd;
    return bfirst(g, lmap(f, p));
  }
}
