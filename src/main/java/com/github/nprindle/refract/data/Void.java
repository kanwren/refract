package com.github.nprindle.refract.data;

import com.github.nprindle.refract.classes.Functor;
import com.github.nprindle.refract.classes.Semigroup;
import com.github.nprindle.refract.d17n.A1;
import com.github.nprindle.refract.d17n.K1;
import com.github.nprindle.refract.optics.Prism;

/** The empty type, with no inhabitants (modulo nulls in Java). */
public final class Void {
  private Void() {
    throw new IllegalStateException("Should not have an instance of Void");
  }

  /** Given an actual instance of the class, produce any type. */
  public <T> T absurd() {
    // SAFETY: this should not be callable, as without shenanigans an
    // instance should never be created.
    throw new IllegalStateException("Should not have an instance of Void");
  }

  /**
   * Since {@code Void} is uninhabited, any {@code Functor} containing it holds no values; since the
   * type parameter is phantom, {@code vacuous} can be used to transform it arbitrarily.
   */
  public static <F extends K1, T> A1<F, T> vacuous(
      final Functor<? extends Functor.Mu, F> functor, final A1<F, Void> v) {
    return functor.map(Void::absurd, v);
  }

  public static final class Instances {
    private Instances() {}

    public static Semigroup<? extends Semigroup.Mu, Void> semigroup() {
      return Void.Instances.SemigroupI.INSTANCE;
    }

    private static enum SemigroupI implements Semigroup<SemigroupI.Mu, Void> {
      INSTANCE;

      public static final class Mu implements Semigroup.Mu {}

      @Override
      public Void append(Void a, Void b) {
        return a;
      }
    }
  }

  public static final class Optics {
    public static <S, A> Prism<S, S, A, Void> prism() {
      return Prism.prism(Either::left, Void::absurd);
    }
  }
}
