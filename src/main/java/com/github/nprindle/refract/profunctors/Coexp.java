package com.github.nprindle.refract.profunctors;

import com.github.nprindle.refract.classes.Profunctor;
import com.github.nprindle.refract.d17n.A1;
import com.github.nprindle.refract.d17n.A2;
import com.github.nprindle.refract.d17n.A3;
import com.github.nprindle.refract.d17n.A4;
import com.github.nprindle.refract.d17n.K1;
import com.github.nprindle.refract.d17n.K2;
import com.github.nprindle.refract.d17n.K3;
import com.github.nprindle.refract.d17n.K4;
import java.util.function.BiFunction;
import java.util.function.Function;

@FunctionalInterface
public interface Coexp<S, T, B, A>
    extends A1<Coexp.Mu<S, T, B>, A>,
        A2<Coexp.Mu2<S, T>, B, A>,
        A3<Coexp.Mu3<S>, T, B, A>,
        A4<Coexp.Mu4, S, T, B, A> {
  static final class Mu<S, T, B> implements K1 {}

  static final class Mu2<S, T> implements K2 {}

  static final class Mu3<S> implements K3 {}

  static final class Mu4 implements K4 {}

  static <S, T, B, A> Coexp<S, T, B, A> unbox(final A1<Coexp.Mu<S, T, B>, A> p) {
    return (Coexp<S, T, B, A>) p;
  }

  static <S, T, B, A> Coexp<S, T, B, A> unbox(final A2<Coexp.Mu2<S, T>, B, A> p) {
    return (Coexp<S, T, B, A>) p;
  }

  static <S, T, B, A> Coexp<S, T, B, A> unbox(final A3<Coexp.Mu3<S>, T, B, A> p) {
    return (Coexp<S, T, B, A>) p;
  }

  static <S, T, B, A> Coexp<S, T, B, A> unbox(final A4<Coexp.Mu4, S, T, B, A> p) {
    return (Coexp<S, T, B, A>) p;
  }

  <R> R runCoexp(final BiFunction<? super Function<S, A>, ? super Function<B, T>, ? extends R> f);

  static final class Instances {
    public static <S, T> Profunctor<Coexp.Mu2<S, T>> profunctor() {
      return new Coexp.Instances.ProfunctorI<>();
    }

    private static class ProfunctorI<S, T> implements Profunctor<Coexp.Mu2<S, T>> {
      @Override
      public <A, B, C, D> A2<Coexp.Mu2<S, T>, C, D> dimap(
          final Function<? super C, ? extends A> f,
          final Function<? super B, ? extends D> g,
          final A2<Coexp.Mu2<S, T>, A, B> x) {
        return Coexp.unbox(x)
            .runCoexp(
                (recall, forget) -> {
                  return new Coexp<S, T, C, D>() {
                    @Override
                    public <R> R runCoexp(
                        final BiFunction<
                                ? super Function<S, D>, ? super Function<C, T>, ? extends R>
                            k) {
                      return k.apply(recall.andThen(g), forget.compose(f));
                    }
                  };
                });
      }
    }
  }
}
