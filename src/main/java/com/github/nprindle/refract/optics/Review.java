package com.github.nprindle.refract.optics;

import com.github.nprindle.refract.classes.Reviewing;
import com.github.nprindle.refract.profunctors.Recall;

interface Review<T, B> extends Optic<Reviewing.Mu, T, T, B, B> {
  default T review(final B b) {
    return Recall.resolve(this.runOptic(Recall.Instances.reviewing(), Recall.identity())).apply(b);
  }
}
