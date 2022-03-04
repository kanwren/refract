package com.github.nprindle.refract.classes;

import com.github.nprindle.refract.d17n.A2;
import com.github.nprindle.refract.d17n.K2;
import com.github.nprindle.refract.data.Either;

public interface Choice<P extends K2> extends Profunctor<P> {
  <A, B, C> A2<P, Either<A, C>, Either<B, C>> left(final A2<P, A, B> p);

  <A, B, C> A2<P, Either<C, A>, Either<C, B>> right(final A2<P, A, B> p);
}
