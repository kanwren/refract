package com.github.nprindle.refract.d17n;

/**
 * A one-argument HKT defunctionalization symbol
 *
 * By convention, one-argument defunctionalization symbols are named {@code Mu}.
 *
 * A one-argument defunctionalization symbol may be applied to an argument using
 * {@code A1}, as in {@A2<Identity.Mu, A>}. Once a symbol is fully-applied (it
 * carries as many type arguments as the underlying concrete type), it may be
 * run into the concrete type using {@code unbox}.
 */
public interface K1 {}
