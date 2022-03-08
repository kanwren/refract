package com.github.nprindle.refract.optics;

import com.github.nprindle.refract.classes.Getting;
import com.github.nprindle.refract.profunctors.Forget;

interface Getter<S, A> extends Optic<Getting.Mu, S, S, A, A> {
  default A view(final S s) {
    final Forget<A, A, A> f = x -> x;
    return Forget.resolve(this.runOptic(Forget.Instances.getting(), f)).apply(s);
  }
}
