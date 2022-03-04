package com.github.nprindle.refract.d17n;

/**
 * A one-argument HKT defunctionalization symbol
 *
 * <p>By convention, one-argument defunctionalization symbols are named {@code Mu}.
 *
 * <p>A one-argument defunctionalization symbol may be applied to an argument using {@code A1}, as
 * in {@code A2<Identity.Mu, A>}. Once a symbol is fully-applied (it carries as many type arguments
 * as the underlying concrete type), it may be run into the concrete type using {@code resolve}.
 */
public interface K1 {}
