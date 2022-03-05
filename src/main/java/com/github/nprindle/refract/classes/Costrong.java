package com.github.nprindle.refract.classes;

import com.github.nprindle.refract.d17n.A1;
import com.github.nprindle.refract.d17n.A2;
import com.github.nprindle.refract.d17n.K2;
import com.github.nprindle.refract.data.Pair;

public interface Costrong<Mu extends Costrong.Mu, P extends K2> extends Profunctor<Mu, P> {
  public static interface Mu extends Profunctor.Mu {}

  public static <Mu extends Costrong.Mu, P extends K2> Costrong<Mu, P> resolve(final A1<Mu, P> p) {
    return (Costrong<Mu, P>) p;
  }

  <A, B, C> A2<P, A, B> unfirst(final A2<P, Pair<A, C>, Pair<B, C>> p);

  <A, B, C> A2<P, A, B> unsecond(final A2<P, Pair<C, A>, Pair<C, B>> p);
}
