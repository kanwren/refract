package com.github.nprindle.refract.optics;

import com.github.nprindle.refract.classes.Reviewing;
import com.github.nprindle.refract.d17n.A1;
import com.github.nprindle.refract.d17n.A2;
import com.github.nprindle.refract.d17n.K2;
import com.github.nprindle.refract.profunctors.Recall;
import java.util.function.Function;

public interface Review<T, B> extends Optic<Reviewing.Mu, T, T, B, B> {
  static <T, B> Review<T, B> fromOptic(final Optic<? super Reviewing.Mu, T, T, B, B> optic) {
    return new Review<T, B>() {
      public <P extends K2> A2<P, T, T> runOptic(
          final A1<? extends Reviewing.Mu, P> dict, final A2<P, B, B> rel) {
        return optic.runOptic(dict, rel);
      }
    };
  }

  static <T, B> Review<T, B> unto(final Function<? super B, ? extends T> reviewer) {
    return new Review<T, B>() {
      @Override
      public <P extends K2> A2<P, T, T> runOptic(
          final A1<? extends Reviewing.Mu, P> dict, final A2<P, B, B> rel) {
        final Reviewing<? extends Reviewing.Mu, P> reviewing = Reviewing.resolve(dict);
        return reviewing.lphantom(reviewing.rmap(reviewer, rel));
      }

      // use the function directly as an optimization, rather than reconstructing from Recall
      @Override
      public T review(final B b) {
        return reviewer.apply(b);
      }
    };
  }

  default T review(final B b) {
    return Recall.resolve(this.runOptic(Recall.Instances.reviewing(), Recall.identity())).apply(b);
  }

  default Getter<B, T> re() {
    return Getter.to(this::review);
  }
}
