package com.github.nprindle.refract.optics;

import com.github.nprindle.refract.d17n.A1;
import com.github.nprindle.refract.d17n.A2;
import com.github.nprindle.refract.d17n.C2;
import com.github.nprindle.refract.d17n.K2;

public interface Optic<Constraint extends C2.Mu, S, T, A, B> {
  /**
   * An optic transforms relations {@code P<A, B>} on part of a structure into relations {@code P<S,
   * T>} on the whole structure, assuming some {@code Constraint} on {@code P}. Note that this is
   * universally quantified in {@code P}.
   *
   * <p>All optics are of the form {@code type Optic (c :: (Type -> Type -> Type) -> Constraint) s t
   * a b = forall (p :: (Type -> Type -> Type)). (c p) => p a b -> p s t}. Here, the kind {@code
   * Type -> Type -> Type} is denoted {@code K2} and the kind {@code (Type -> Type -> Type) ->
   * Constraint} is denoted {@code C2}. In the type parameters, {@code Constraint} is the symbol
   * representing a constraint of kind {@code C2} on some type {@code P} of kind {@code K2}, usually
   * some sort of profunctor.
   */
  <P extends K2> A2<P, S, T> runOptic(
      final A1<? extends Constraint, P> dict, final A2<P, A, B> rel);
}
