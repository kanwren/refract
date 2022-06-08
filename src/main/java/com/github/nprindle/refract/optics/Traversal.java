package com.github.nprindle.refract.optics;

import com.github.nprindle.refract.classes.Applicative;
import com.github.nprindle.refract.classes.Traversable;
import com.github.nprindle.refract.classes.Traversing;
import com.github.nprindle.refract.d17n.A1;
import com.github.nprindle.refract.d17n.A2;
import com.github.nprindle.refract.d17n.K1;
import com.github.nprindle.refract.d17n.K2;
import com.github.nprindle.refract.profunctors.Star;
import java.util.function.Function;

public interface Traversal<S, T, A, B> extends Optic<Traversing.Mu, S, T, A, B> {
  static <S, T, A, B> Traversal<S, T, A, B> fromOptic(
      final Optic<? super Traversing.Mu, S, T, A, B> optic) {
    return new Traversal<S, T, A, B>() {
      @Override
      public <P extends K2> A2<P, S, T> runOptic(
          final A1<? extends Traversing.Mu, P> dict, final A2<P, A, B> rel) {
        return optic.runOptic(dict, rel);
      }
    };
  }

  static <F extends K1, S, T, A, B> Traversal<S, T, A, B> traversing(
      final Traversing.Wander<S, T, A, B> wander) {
    return new Traversal<S, T, A, B>() {
      @Override
      public <P extends K2> A2<P, S, T> runOptic(
          final A1<? extends Traversing.Mu, P> dict, final A2<P, A, B> rel) {
        final Traversing<? extends Traversing.Mu, P> traversing = Traversing.resolve(dict);
        return traversing.wander(wander, rel);
      }
    };
  }

  static <F extends K1, A, B> Traversal<A1<F, A>, A1<F, B>, A, B> traversed(
      final Traversable<? extends Traversable.Mu, F> traversable) {
    return traversing(Traversing.Wander.fromTraversable(traversable));
  }

  default Traversing.Wander<S, T, A, B> toWander() {
    final Traversal<S, T, A, B> base = this;
    return new Traversing.Wander<S, T, A, B>() {
      @Override
      public <F extends K1> A1<F, T> runWander(
          final Applicative<? extends Applicative.Mu, F> applicative,
          final Function<A, A1<F, B>> f,
          final S source) {
        return Star.resolve(base.runOptic(Star.Instances.traversing(applicative), Star.from(f)))
            .apply(source);
      }
    };
  }

  default <F extends K1> A1<F, T> traverseOf(
      final Applicative<? extends Applicative.Mu, F> applicative,
      final Function<A, A1<F, B>> f,
      final S s) {
    return Star.resolve(this.runOptic(Star.Instances.traversing(applicative), Star.from(f)))
        .apply(s);
  }

  default Traversal<S, T, A, B> backwards() {
    return Traversal.traversing(this.toWander().backwards());
  }
}
