package com.github.nprindle.refract.optics;

import com.github.nprindle.refract.classes.Strong;

interface Lens<S, T, A, B> extends Optic<Strong.Mu, S, T, A, B> {}
