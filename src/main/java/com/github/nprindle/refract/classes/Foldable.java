package com.github.nprindle.refract.classes;

import com.github.nprindle.refract.d17n.A1;
import com.github.nprindle.refract.d17n.K1;
import java.util.function.Function;

public interface Foldable<T extends K1> {
  <M, A> M foldMap(
      final Monoid<M> monoid, final Function<? super A, ? extends M> f, final A1<T, A> x);
}
