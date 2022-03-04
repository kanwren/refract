package com.github.nprindle.refract.classes;

import com.github.nprindle.refract.d17n.A1;
import com.github.nprindle.refract.d17n.K1;
import java.util.function.Function;

public interface Traversable<T extends K1> extends Functor<T> {
  <F extends K1, A, B> A1<F, A1<T, B>> traverse(
      final Applicative<F> applicative,
      final Function<? super A, ? extends A1<F, B>> f,
      final A1<T, A> x);

  default <F extends K1, A> A1<F, A1<T, A>> sequence(
      final Applicative<F> applicative, final A1<T, A1<F, A>> x) {
    return traverse(applicative, y -> y, x);
  }
}
