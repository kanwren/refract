package com.github.nprindle.refract.optics;

import com.github.nprindle.refract.classes.Folding;
import com.github.nprindle.refract.d17n.A1;
import com.github.nprindle.refract.d17n.A2;
import com.github.nprindle.refract.d17n.K2;

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

  // TODO:
  // folded :: Foldable f => Fold (f a) a
  // folding :: (forall f. Applicative f => (a -> f a) -> s -> f ()) -> Fold s a
  // foldMapOf :: Fold s a -> Monoid m => (a -> m) -> s -> m
  // traverseOf_ :: Applicative f => Fold s a -> (a -> f r) -> s -> f ()
  // preview :: Fold s a -> s -> Maybe a
  // has :: Fold s a -> s -> Bool
  // newtype Foldr a = Foldr { runCons :: forall r. (a -> r -> r) -> r -> r }
  // foldrOf :: Fold s a -> (a -> r -> r) -> r -> s -> r
  // toListOf :: Fold s a -> s -> [a]
  // lengthOf :: Fold s a -> s -> Int
  // foldBackwards :: Fold s a -> Fold s a
}
