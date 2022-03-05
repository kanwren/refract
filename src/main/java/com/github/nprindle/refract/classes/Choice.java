package com.github.nprindle.refract.classes;

import com.github.nprindle.refract.d17n.A1;
import com.github.nprindle.refract.d17n.A2;
import com.github.nprindle.refract.d17n.K2;
import com.github.nprindle.refract.data.Either;

public interface Choice<Mu extends Choice.Mu, P extends K2> extends Profunctor<Mu, P> {
  public static interface Mu extends Profunctor.Mu {}

  public static <Mu extends Choice.Mu, P extends K2> Choice<Mu, P> resolve(final A1<Mu, P> p) {
    return (Choice<Mu, P>) p;
  }

  <A, B, C> A2<P, Either<A, C>, Either<B, C>> left(final A2<P, A, B> p);

  <A, B, C> A2<P, Either<C, A>, Either<C, B>> right(final A2<P, A, B> p);
}
