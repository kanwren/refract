package com.github.nprindle.refract.optics;

import com.github.nprindle.refract.classes.Choice;
import com.github.nprindle.refract.d17n.A1;
import com.github.nprindle.refract.d17n.A2;
import com.github.nprindle.refract.d17n.K2;
import com.github.nprindle.refract.data.Either;
import java.util.function.Function;

public interface Prism<S, T, A, B> extends Optic<Choice.Mu, S, T, A, B> {
  static <S, T, A, B> Prism<S, T, A, B> fromOptic(
      final Optic<? super Choice.Mu, S, T, A, B> optic) {
    return new Prism<S, T, A, B>() {
      @Override
      public <P extends K2> A2<P, S, T> runOptic(
          final A1<? extends Choice.Mu, P> dict, final A2<P, A, B> rel) {
        return optic.runOptic(dict, rel);
      }
    };
  }

  static <S, T, A, B> Prism<S, T, A, B> prism(
      final Function<? super S, ? extends Either<T, A>> preview,
      final Function<? super B, ? extends T> review) {
    return new Prism<S, T, A, B>() {
      @Override
      public <P extends K2> A2<P, S, T> runOptic(
          final A1<? extends Choice.Mu, P> dict, final A2<P, A, B> rel) {
        final Choice<? extends Choice.Mu, P> choice = Choice.resolve(dict);
        final A2<P, Either<T, A>, Either<T, B>> r = choice.right(rel);
        return choice.dimap(preview, e -> e.either(t -> t, review), choice.right(rel));
      }
    };
  }

  // TODO:
  // prism' :: (s -> Maybe a) -> (b -> s) -> Prism s s a b,
  // withPrism :: Prism s t a b -> ((s -> Either t a) -> (b -> t) -> r) -> r
  // only :: Eq a => a -> Prism' a ()
  // below :: Traversable f => Prism' s a -> Prism' (f s) (f a)
  // prisms for Either, Optional, (_Void :: Prism s s a Void)
}
