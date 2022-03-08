package com.github.nprindle.refract.optics;

import com.github.nprindle.refract.classes.Choice;

interface Prism<S, T, A, B> extends Optic<Choice.Mu, S, T, A, B> {}
