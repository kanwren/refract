package com.github.nprindle.refract.classes;

import com.github.nprindle.refract.d17n.A1;
import com.github.nprindle.refract.d17n.A2;
import com.github.nprindle.refract.d17n.K2;
import com.github.nprindle.refract.data.Pair;

public interface Strong<Mu extends Strong.Mu, P extends K2> extends Profunctor<Mu, P> {
  public static interface Mu extends Profunctor.Mu {}

  public static <Mu extends Strong.Mu, P extends K2> Strong<Mu, P> resolve(final A1<Mu, P> p) {
    return (Strong<Mu, P>) p;
  }

  <A, B, C> A2<P, Pair<A, C>, Pair<B, C>> first(final A2<P, A, B> p);

  <A, B, C> A2<P, Pair<C, A>, Pair<C, B>> second(final A2<P, A, B> p);
}
