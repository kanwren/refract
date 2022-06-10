package com.github.nprindle.refract.classes;

import com.github.nprindle.refract.d17n.A1;
import com.github.nprindle.refract.d17n.C1;
import com.github.nprindle.refract.d17n.K1;
import com.github.nprindle.refract.data.Unit;
import java.util.function.Function;

// F is the Mu of the type the Functor is being implemented for
public interface Functor<Mu extends Functor.Mu, F extends K1> extends C1<Mu, F> {
  public static interface Mu extends C1.Mu {}

  public static <Mu extends Functor.Mu, F extends K1> Functor<Mu, F> resolve(final A1<Mu, F> p) {
    return (Functor<Mu, F>) p;
  }

  <A, B> A1<F, B> map(final Function<? super A, ? extends B> f, final A1<F, A> x);

  default <A> A1<F, Unit> ignore(final A1<F, A> x) {
    return this.map(a -> Unit.UNIT, x);
  }
}
