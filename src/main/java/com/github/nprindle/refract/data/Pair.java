package com.github.nprindle.refract.data;

import com.github.nprindle.refract.classes.Applicative;
import com.github.nprindle.refract.classes.Bifunctor;
import com.github.nprindle.refract.classes.Foldable;
import com.github.nprindle.refract.classes.Functor;
import com.github.nprindle.refract.classes.Monoid;
import com.github.nprindle.refract.classes.Traversable;
import com.github.nprindle.refract.d17n.A1;
import com.github.nprindle.refract.d17n.A2;
import com.github.nprindle.refract.d17n.K1;
import com.github.nprindle.refract.d17n.K2;
import java.util.function.Function;

public final class Pair<A, B> implements A1<Pair.Mu<A>, B>, A2<Pair.Mu2, A, B> {
  public static final class Mu<A> implements K1 {}

  public static final <A, B> Pair<A, B> resolve(final A1<Pair.Mu<A>, B> p) {
    return (Pair<A, B>) p;
  }

  public static final class Mu2 implements K2 {}

  public static final <A, B> Pair<A, B> resolve(final A2<Pair.Mu2, A, B> p) {
    return (Pair<A, B>) p;
  }

  private final A fst;
  private final B snd;

  public Pair(final A fst, final B snd) {
    this.fst = fst;
    this.snd = snd;
  }

  public static <A, B> Pair<A, B> of(final A a, final B b) {
    return new Pair<>(a, b);
  }

  public A fst() {
    return this.fst;
  }

  public static <A, B> A fst(final A1<Pair.Mu<A>, B> p) {
    return ((Pair<A, B>) p).fst;
  }

  public static <A, B> A fst(final A2<Pair.Mu2, A, B> p) {
    return ((Pair<A, B>) p).fst;
  }

  public B snd() {
    return this.snd;
  }

  public static <A, B> B snd(final A1<Pair.Mu<A>, B> p) {
    return ((Pair<A, B>) p).snd;
  }

  public static <A, B> B snd(final A2<Pair.Mu2, A, B> p) {
    return ((Pair<A, B>) p).snd;
  }

  public <C> Pair<C, B> mapFst(final Function<? super A, ? extends C> f) {
    return new Pair<>(f.apply(this.fst), this.snd);
  }

  public <C> Pair<A, C> mapSnd(final Function<? super B, ? extends C> f) {
    return new Pair<>(this.fst, f.apply(this.snd));
  }

  public <C, D> Pair<C, D> mapBoth(
      final Function<? super A, ? extends C> f, final Function<? super B, ? extends D> g) {
    return new Pair<>(f.apply(this.fst), g.apply(this.snd));
  }

  public Pair<B, A> swap() {
    return new Pair<>(this.snd, this.fst);
  }

  public static final class Instances {
    public static <A> Functor<? extends Functor.Mu, Pair.Mu<A>> functor() {
      return new Pair.Instances.FunctorI<>();
    }

    public static <A> Foldable<? extends Foldable.Mu, Pair.Mu<A>> foldable() {
      return new Pair.Instances.FunctorI<>();
    }

    public static <A> Traversable<? extends Traversable.Mu, Pair.Mu<A>> traversable() {
      return new Pair.Instances.FunctorI<>();
    }

    public static <A> Bifunctor<? extends Bifunctor.Mu, Pair.Mu2> bifunctor() {
      return Pair.Instances.BifunctorI.INSTANCE;
    }

    private static final class FunctorI<K>
        implements Functor<FunctorI.Mu, Pair.Mu<K>>,
            Foldable<FunctorI.Mu, Pair.Mu<K>>,
            Traversable<FunctorI.Mu, Pair.Mu<K>> {
      public static final class Mu implements Traversable.Mu {}

      @Override
      public <A, B> A1<Pair.Mu<K>, B> map(
          final Function<? super A, ? extends B> f, final A1<Pair.Mu<K>, A> x) {
        return Pair.resolve(x).mapSnd(f);
      }

      @Override
      public <M, A> M foldMap(
          final Monoid<? extends Monoid.Mu, M> monoid,
          final Function<? super A, ? extends M> f,
          final A1<Pair.Mu<K>, A> x) {
        return f.apply(Pair.snd(x));
      }

      @Override
      public <F extends K1, A, B> A1<F, A1<Pair.Mu<K>, B>> traverse(
          final Applicative<? extends Applicative.Mu, F> applicative,
          final Function<? super A, ? extends A1<F, B>> f,
          final A1<Pair.Mu<K>, A> x) {
        final Pair<K, A> p = Pair.resolve(x);
        final K k = p.fst();
        final A a = p.snd();
        return applicative.map(b -> Pair.of(k, b), f.apply(a));
      }
    }

    private static enum BifunctorI implements Bifunctor<BifunctorI.Mu, Pair.Mu2> {
      INSTANCE;

      public static final class Mu implements Bifunctor.Mu {}

      @Override
      public <A, B, C, D> A2<Pair.Mu2, C, D> bimap(
          final Function<? super A, ? extends C> f,
          final Function<? super B, ? extends D> g,
          final A2<Pair.Mu2, A, B> x) {
        return Pair.resolve(x).mapBoth(f, g);
      }
    }
  }
}
