package com.github.nprindle.refract.classes;

import com.github.nprindle.refract.d17n.A2;
import com.github.nprindle.refract.d17n.K2;
import java.util.function.Function;

public interface Profunctor<P extends K2> {
  <A, B, C, D> A2<P, C, D> dimap(
      final Function<? extends C, ? super A> f,
      final Function<? super B, ? extends D> g,
      final A2<P, A, B> x);

  default <A, B, C> A2<P, C, B> lmap(
      final Function<? extends C, ? super A> f, final A2<P, A, B> x) {
    return dimap(f, y -> y, x);
  }

  default <A, B, D> A2<P, A, D> rmap(
      final Function<? super B, ? extends D> g, final A2<P, A, B> x) {
    return dimap(y -> y, g, x);
  }
}
