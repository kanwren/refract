package com.github.nprindle.refract.d17n;

/**
 * Apply an HKT defunctionalization symbol {@code F} of three arguments to types {@code A}, {@code
 * B}, and {@code C}.
 *
 * <p>Once a symbol is fully-applied (it carries as many type arguments as the underlying concrete
 * type), it may be run into the concrete type using {@code unbox}.
 */
public interface A3<F extends K3, A, B, C> {}
