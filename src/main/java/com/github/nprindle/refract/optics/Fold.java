package com.github.nprindle.refract.optics;

import com.github.nprindle.refract.classes.Foldable;
import com.github.nprindle.refract.classes.Folding;
import com.github.nprindle.refract.classes.Monoid;
import com.github.nprindle.refract.classes.Semigroup;
import com.github.nprindle.refract.classes.Traversing;
import com.github.nprindle.refract.d17n.A1;
import com.github.nprindle.refract.d17n.A2;
import com.github.nprindle.refract.d17n.K1;
import com.github.nprindle.refract.d17n.K2;
import com.github.nprindle.refract.data.Lazy;
import com.github.nprindle.refract.data.Unit;
import com.github.nprindle.refract.profunctors.Forget;
import java.util.Optional;
import java.util.function.BiFunction;
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

  default <R> R foldrOf(
      final BiFunction<? super A, ? super R, ? extends R> f, final R z, final S s) {
    // TODO: optimize this
    return this.foldMapOf(Foldr.Instances.monoid(), Foldr::singleton, s).runFoldr(f::apply, z);
  }

  default Optional<A> preview(final S s) {
    return this.foldMapOf(Monoid.Instances.lazyFirst(), a -> Lazy.of(Optional.of(a)), s).get();
  }

  default boolean has(final S s) {
    return this.foldMapOf(Monoid.Instances.lazyOr(), a -> Lazy.of(true), s).get();
  }

  default int lengthOf(final S s) {
    return this.foldMapOf(Monoid.Instances.intSum(), a -> 1, s);
  }

  // TODO:
  // traverseOf_ :: Applicative f => Fold s a -> (a -> f r) -> s -> f ()
  // toListOf :: Fold s a -> s -> [a]
  // foldBackwards :: Fold s a -> Fold s a
}

// newtype Foldr a = Foldr { runCons :: forall r. (a -> r -> r) -> r -> r }
interface Foldr<A> extends A1<Foldr.Mu, A> {
  static final class Mu implements K1 {}

  static <A> Foldr<A> resolve(final A1<Foldr.Mu, A> p) {
    return (Foldr<A>) p;
  }

  <R> R runFoldr(final BiFunction<A, R, R> cons, final R nil);

  static <A> Foldr<A> singleton(final A a) {
    return new Foldr<A>() {
      @Override
      public <R> R runFoldr(final BiFunction<A, R, R> cons, final R nil) {
        return cons.apply(a, nil);
      }
    };
  }

  static final class Instances {
    private Instances() {}

    public static <A> Semigroup<? extends Semigroup.Mu, Foldr<A>> semigroup() {
      return new Foldr.Instances.MonoidI<>();
    }

    public static <A> Monoid<? extends Monoid.Mu, Foldr<A>> monoid() {
      return new Foldr.Instances.MonoidI<>();
    }

    private static class MonoidI<A> implements Monoid<MonoidI.Mu, Foldr<A>> {
      public static final class Mu implements Monoid.Mu {}

      @Override
      public Foldr<A> empty() {
        return new Foldr<A>() {
          @Override
          public <R> R runFoldr(final BiFunction<A, R, R> cons, final R nil) {
            return nil;
          }
        };
      }

      @Override
      public Foldr<A> append(final Foldr<A> a, final Foldr<A> b) {
        return new Foldr<A>() {
          @Override
          public <R> R runFoldr(final BiFunction<A, R, R> cons, final R nil) {
            return a.runFoldr(cons, b.runFoldr(cons, nil));
          }
        };
      }
    }
  }
}
