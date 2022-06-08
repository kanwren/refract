package com.github.nprindle.refract.optics;

import com.github.nprindle.refract.classes.Foldable;
import com.github.nprindle.refract.classes.Folding;
import com.github.nprindle.refract.classes.Monoid;
import com.github.nprindle.refract.classes.Traversing;
import com.github.nprindle.refract.d17n.A1;
import com.github.nprindle.refract.d17n.A2;
import com.github.nprindle.refract.d17n.K1;
import com.github.nprindle.refract.d17n.K2;
import com.github.nprindle.refract.data.Unit;
import com.github.nprindle.refract.profunctors.Forget;
import java.util.function.Function;

public interface Fold<S, A> extends Optic<Folding.Mu, S, S, A, A> {
  static <S, A> Fold<S, A> fromOptic(final Optic<? super Folding.Mu, S, S, A, A> optic) {
    return new Fold<S, A>() {
      @Override
      public <P extends K2> A2<P, S, S> runOptic(
          final A1<? extends Folding.Mu, P> dict, final A2<P, A, A> rel) {
        return optic.runOptic(dict, rel);
      }
    };
  }

  static <S, A> Fold<S, A> folding(final Traversing.Wander<S, Unit, A, A> wander) {
    return new Fold<S, A>() {
      @Override
      public <P extends K2> A2<P, S, S> runOptic(
          final A1<? extends Folding.Mu, P> dict, final A2<P, A, A> rel) {
        final Folding<? extends Folding.Mu, P> folding = Folding.resolve(dict);
        return folding.csecond(a -> Unit.UNIT, folding.wander(wander, rel));
      }
    };
  }

  static <F extends K1, A> Fold<A1<F, A>, A> folded(
      final Foldable<? extends Foldable.Mu, F> foldable) {
    return folding(Traversing.Wander.fromFoldable(foldable));
  }

  default <M> M foldMapOf(
      final Monoid<? extends Monoid.Mu, M> monoid,
      final Function<? super A, ? extends M> f,
      final S s) {
    final Forget<M, A, A> forget = f::apply;
    return Forget.resolve(this.runOptic(Forget.Instances.folding(monoid), forget)).apply(s);
  }

  // TODO:
  // traverseOf_ :: Applicative f => Fold s a -> (a -> f r) -> s -> f ()
  // preview :: Fold s a -> s -> Maybe a
  // has :: Fold s a -> s -> Bool
  // newtype Foldr a = Foldr { runCons :: forall r. (a -> r -> r) -> r -> r }
  // foldrOf :: Fold s a -> (a -> r -> r) -> r -> s -> r
  // toListOf :: Fold s a -> s -> [a]
  // lengthOf :: Fold s a -> s -> Int
  // foldBackwards :: Fold s a -> Fold s a
}
