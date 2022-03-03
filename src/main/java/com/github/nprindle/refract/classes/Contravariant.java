package com.github.nprindle.refract.classes;

import com.github.nprindle.refract.d17n.A1;
import com.github.nprindle.refract.d17n.K1;
import java.util.function.Function;

public interface Contravariant<F extends K1> {
  <A, B> A1<F, B> cmap(final Function<? extends B, ? super A> f, final A1<F, A> x);
}
