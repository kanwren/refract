package com.github.nprindle.refract.d17n;

/**
 * A two-argument HKT defunctionalization symbol.
 *
 * <p>By convention, two-argument defunctionalization symbols are named {@code Mu2}.
 *
 * <p>A two-argument defunctionalization symbol may be applied to two arguments using {@code A2}, as
 * in {@code A2<Either.Mu2, A, B>}. Once a symbol is fully-applied (it carries as many type
 * arguments as the underlying concrete type), it may be run into the concrete type using {@code
 * unbox}.
 */
public interface K2 {}
