package com.github.nprindle.refract.classes;

import com.github.nprindle.refract.d17n.A1;
import com.github.nprindle.refract.d17n.C1;
import com.github.nprindle.refract.d17n.K1;
import com.github.nprindle.refract.data.Unit;
import java.util.function.BiFunction;
import java.util.function.Function;

public interface Foldable<Mu extends Foldable.Mu, T extends K1> extends C1<Mu, T> {
  public static interface Mu extends C1.Mu {}

  public static <Mu extends Foldable.Mu, T extends K1> Foldable<Mu, T> resolve(final A1<Mu, T> p) {
    return (Foldable<Mu, T>) p;
  }

  <A, B> B foldr(
      final BiFunction<? super A, ? super B, ? extends B> f, final B z, final A1<T, A> x);

  default <M, A> M foldMap(
      final Monoid<? extends Monoid.Mu, M> monoid,
      final Function<? super A, ? extends M> f,
      final A1<T, A> x) {
    return this.foldr((a, b) -> monoid.append(f.apply(a), b), monoid.empty(), x);
  }

  default <M> M fold(final Monoid<? extends Monoid.Mu, M> monoid, final A1<T, M> x) {
    return this.foldr((a, b) -> monoid.append(a, b), monoid.empty(), x);
  }

  default <F extends K1, A, B> A1<F, Unit> traverse_(
      final Applicative<? extends Applicative.Mu, F> applicative,
      final Function<? super A, ? extends A1<F, B>> f,
      final A1<T, A> x) {
    return this.foldr((a, r) -> applicative.before(f.apply(a), r), applicative.pure(Unit.UNIT), x);
  }
}
