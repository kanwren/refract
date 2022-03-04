package com.github.nprindle.refract.classes;

import com.github.nprindle.refract.d17n.A2;
import com.github.nprindle.refract.d17n.K2;
import com.github.nprindle.refract.data.Pair;

public interface Costrong<P extends K2> extends Profunctor<P> {
  <A, B, C> A2<P, A, B> unfirst(final A2<P, Pair<A, C>, Pair<B, C>> p);

  <A, B, C> A2<P, A, B> unsecond(final A2<P, Pair<C, A>, Pair<C, B>> p);
}
