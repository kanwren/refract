package com.github.nprindle.refract.optics;

import com.github.nprindle.refract.classes.Profunctor;

interface Iso<S, T, A, B> extends Optic<Profunctor.Mu, S, T, A, B> {}
