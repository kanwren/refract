package com.github.nprindle.refract.optics;

import com.github.nprindle.refract.classes.Getting;
import com.github.nprindle.refract.d17n.A1;
import com.github.nprindle.refract.d17n.A2;
import com.github.nprindle.refract.d17n.K2;
import com.github.nprindle.refract.profunctors.Forget;
import java.util.function.Function;

public interface Getter<S, A> extends Optic<Getting.Mu, S, S, A, A> {
  static <S, A> Getter<S, A> fromOptic(final Optic<? super Getting.Mu, S, S, A, A> optic) {
    return new Getter<S, A>() {
      public <P extends K2> A2<P, S, S> runOptic(
          final A1<? extends Getting.Mu, P> dict, final A2<P, A, A> rel) {
        return optic.runOptic(dict, rel);
      }
    };
  }

  static <S, A> Getter<S, A> to(Function<? super S, ? extends A> getter) {
    return new Getter<S, A>() {
      @Override
      public <P extends K2> A2<P, S, S> runOptic(
          final A1<? extends Getting.Mu, P> dict, final A2<P, A, A> rel) {
        final Getting<? extends Getting.Mu, P> getting = Getting.resolve(dict);
        return getting.rphantom(getting.lmap(getter, rel));
      }

      // use the function directly as an optimization, rather than reconstructing from Forget
      @Override
      public A view(final S s) {
        return getter.apply(s);
      }
    };
  }

  default A view(final S s) {
    final Forget<A, A, A> f = x -> x;
    return Forget.resolve(this.runOptic(Forget.Instances.getting(), f)).apply(s);
  }

  default Review<A, S> un() {
    return Review.unto(this::view);
  }
}
