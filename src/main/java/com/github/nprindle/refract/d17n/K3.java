package com.github.nprindle.refract.d17n;

/**
 * A three-argument HKT defunctionalization symbol.
 *
 * <p>By convention, three-argument defunctionalization symbols are named {@code Mu3}.
 *
 * <p>A three-argument defunctionalization symbol may be applied to three arguments using {@code
 * A3}, as in {@code A2<Forget.Mu3, R, A, B>}. Once a symbol is fully-applied (it carries as many
 * type arguments as the underlying concrete type), it may be run into the concrete type using
 * {@code unbox}.
 */
public interface K3 {}
