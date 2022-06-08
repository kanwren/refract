package com.github.nprindle.refract.optics;

import com.github.nprindle.refract.d17n.A1;
import com.github.nprindle.refract.d17n.A2;
import com.github.nprindle.refract.d17n.C2;
import com.github.nprindle.refract.d17n.K2;
import java.util.function.Function;

// TODO:
// - castWith in either direction on either parameter set

public interface Eq<S, T, A, B> extends Optic<C2.Mu, S, T, A, B> {
  static <S, T, A, B> Eq<S, T, A, B> fromOptic(final Optic<? super C2.Mu, S, T, A, B> optic) {
    return new Eq<S, T, A, B>() {
      @Override
      public <P extends K2> A2<P, S, T> runOptic(
          final A1<? extends C2.Mu, P> dict, final A2<P, A, B> rel) {
        return optic.runOptic(dict, rel);
      }
    };
  }

  static <A, B> Eq<A, B, A, B> refl() {
    return new Eq<A, B, A, B>() {
      @Override
      public <P extends K2> A2<P, A, B> runOptic(
          final A1<? extends C2.Mu, P> dict, final A2<P, A, B> rel) {
        return rel;
      }
    };
  }

  default Eq<B, A, T, S> sym() {
    final Eq<S, T, A, B> eq = this;
    return new Eq<B, A, T, S>() {
      @Override
      public <P extends K2> A2<P, B, A> runOptic(
          final A1<? extends C2.Mu, P> dict, final A2<P, T, S> rel) {
        final Eq.ForgetP<A2<P, B, A>, P, A, B> f = Eq.ForgetP.identity();
        return ForgetP.resolve(eq.runOptic(C2.dict(), f)).apply(rel);
      }
    };
  }

  @FunctionalInterface
  static interface ForgetP<R, P extends K2, A, B>
      extends Function<A2<P, B, A>, R>, A2<ForgetP.Mu2<R, P>, A, B> {
    static final class Mu2<R, P extends K2> implements K2 {}

    static <R, P extends K2, A, B> ForgetP<R, P, A, B> resolve(
        final A2<ForgetP.Mu2<R, P>, A, B> p) {
      return (ForgetP<R, P, A, B>) p;
    }

    static <P extends K2, A, B> ForgetP<A2<P, B, A>, P, A, B> identity() {
      return x -> x;
    }
  }
}
