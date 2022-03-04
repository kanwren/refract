package com.github.nprindle.refract.d17n;

/**
 * Apply an HKT defunctionalization symbol {@code F} of four arguments to types {@code A}, {@code
 * B}, {@code C}, and {@code D}.
 *
 * <p>Once a symbol is fully-applied (it carries as many type arguments as the underlying concrete
 * type), it may be run into the concrete type using {@code unbox}.
 */
public interface A4<F extends K4, A, B, C, D> {}
