package com.github.nprindle.refract.data;

import com.github.nprindle.refract.classes.Applicative;
import com.github.nprindle.refract.classes.Bifunctor;
import com.github.nprindle.refract.classes.Contravariant;
import com.github.nprindle.refract.classes.Functor;
import com.github.nprindle.refract.classes.Traversable;
import com.github.nprindle.refract.d17n.A1;
import com.github.nprindle.refract.d17n.A2;
import com.github.nprindle.refract.d17n.K1;
import com.github.nprindle.refract.d17n.K2;
import java.util.function.Function;

/**
 * The constant functor, which is isomorphic to its first type parameter (modulo subtyping in Java).
 * The second type parameter is phantom, which is useful in contexts in which a functor is required
 * but you want to ignore the functorial computation and maintain a constant value.
 */
public final class Const<A, B> implements A1<Const.Mu<A>, B>, A2<Const.Mu2, A, B> {
  public static final class Mu<A> implements K1 {}

  public static final class Mu2 implements K2 {}

  public static <A, B> Const<A, B> unbox(final A1<Const.Mu<A>, B> p) {
    return (Const<A, B>) p;
  }

  public static <A, B> Const<A, B> unbox(final A2<Const.Mu2, A, B> p) {
    return (Const<A, B>) p;
  }

  private final A value;

  public Const(final A value) {
    this.value = value;
  }

  public static <A, B> Const<A, B> of(final A value) {
    return new Const<>(value);
  }

  public A value() {
    return this.value;
  }

  /**
   * Extract the inner value of type {@code A} from a fully-applied one-argument defunctionalization
   * symbol.
   */
  public static <A, B> A get(final A1<Const.Mu<A>, B> p) {
    return Const.unbox(p).value();
  }

  /**
   * Extract the inner value of type {@code A} from a fully-applied two-argument defunctionalization
   * symbol.
   */
  public static <A, B> A get(final A2<Const.Mu2, A, B> p) {
    return Const.unbox(p).value();
  }

  public static final class Instances {
    public static <C> Functor<Const.Mu<C>> functor() {
      return new Const.Instances.FunctorI<>();
    }

    public static <C> Contravariant<Const.Mu<C>> contravariant() {
      return new Const.Instances.FunctorI<>();
    }

    public static <C> Bifunctor<Const.Mu2> bifunctor() {
      return Const.Instances.BifunctorI.INSTANCE;
    }

    private static class FunctorI<C>
        implements Functor<Const.Mu<C>>, Contravariant<Const.Mu<C>>, Traversable<Const.Mu<C>> {
      @Override
      public <A, B> A1<Const.Mu<C>, B> map(
          final Function<? super A, ? extends B> f, final A1<Const.Mu<C>, A> x) {
        return new Const<>(Const.get(x));
      }

      @Override
      public <A, B> A1<Const.Mu<C>, B> cmap(
          final Function<? extends B, ? super A> f, final A1<Const.Mu<C>, A> x) {
        return new Const<>(Const.get(x));
      }

      @Override
      public <F extends K1, A, B> A1<F, A1<Const.Mu<C>, B>> traverse(
          final Applicative<F> applicative,
          final Function<? super A, ? extends A1<F, B>> f,
          final A1<Const.Mu<C>, A> x) {
        return applicative.pure(Const.of(Const.get(x)));
      }
    }

    private static enum BifunctorI implements Bifunctor<Const.Mu2> {
      INSTANCE;

      @Override
      public <A, B, C, D> A2<Const.Mu2, C, D> bimap(
          final Function<? super A, ? extends C> f,
          final Function<? super B, ? extends D> g,
          final A2<Const.Mu2, A, B> x) {
        return new Const<>(f.apply(Const.get(x)));
      }
    }
  }
}
