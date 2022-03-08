package com.github.nprindle.refract.optics;

import com.github.nprindle.refract.classes.Mapping;
import com.github.nprindle.refract.profunctors.Func;
import java.util.function.Function;

public interface Setter<S, T, A, B> extends Optic<Mapping.Mu, S, T, A, B> {
  default T over(Function<? super A, ? extends B> f, S s) {
    return Func.resolve(this.runOptic(Func.Instances.mapping(), Func.from(f))).apply(s);
  }
}
