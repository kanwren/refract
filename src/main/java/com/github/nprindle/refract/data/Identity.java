package com.github.nprindle.refract.data;

import com.github.nprindle.refract.classes.Applicative;
import com.github.nprindle.refract.classes.Functor;
import com.github.nprindle.refract.classes.Traversable;
import com.github.nprindle.refract.d17n.A1;
import com.github.nprindle.refract.d17n.K1;
import java.util.function.Function;

/** The identity functor, which is isomorphic to the underlying type (modulo subtyping in Java). */
public final class Identity<A> implements A1<Identity.Mu, A> {
  public static final class Mu implements K1 {}

  public static <A> Identity<A> unbox(final A1<Identity.Mu, A> p) {
    return (Identity<A>) p;
  }

  private final A value;

  public Identity(final A value) {
    this.value = value;
  }

  public static <A> Identity<A> of(final A a) {
    return new Identity<>(a);
  }

  public A value() {
    return this.value;
  }

  /** Extract the value from a fully-applied defunctionalization symbol. */
  public static <A> A get(final A1<Identity.Mu, A> p) {
    return Identity.unbox(p).value();
  }

  public static final class Instances {
    public static final Functor<Identity.Mu> functor() {
      return Identity.Instances.ApplicativeI.INSTANCE;
    }

    public static final Applicative<Identity.Mu> applicative() {
      return Identity.Instances.ApplicativeI.INSTANCE;
    }

    public static final Traversable<Identity.Mu> traversable() {
      return Identity.Instances.ApplicativeI.INSTANCE;
    }

    private static enum ApplicativeI
        implements Applicative<Identity.Mu>, Functor<Identity.Mu>, Traversable<Identity.Mu> {
      INSTANCE;

      @Override
      public <A, B> A1<Identity.Mu, B> map(
          final Function<? super A, ? extends B> f, final A1<Identity.Mu, A> x) {
        return new Identity<>(f.apply(Identity.get(x)));
      }

      @Override
      public <A> A1<Identity.Mu, A> pure(final A x) {
        return new Identity<>(x);
      }

      @Override
      public <A, B> A1<Identity.Mu, B> ap(
          final A1<Identity.Mu, Function<? super A, ? extends B>> f, final A1<Identity.Mu, A> x) {
        return new Identity<>(Identity.get(f).apply(Identity.get(x)));
      }

      @Override
      public <F extends K1, A, B> A1<F, A1<Identity.Mu, B>> traverse(
          final Applicative<F> applicative,
          final Function<? super A, ? extends A1<F, B>> f,
          final A1<Identity.Mu, A> x) {
        return applicative.map(Identity::of, f.apply(Identity.get(x)));
      }
    }
  }
}
