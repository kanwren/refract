package com.github.nprindle.refract.classes;

import com.github.nprindle.refract.d17n.A2;
import com.github.nprindle.refract.d17n.K2;
import com.github.nprindle.refract.data.Pair;

import java.util.function.Function;

public interface Strong<P extends K2> {
    <A, B, C> A2<P, Pair<A, C>, Pair<B, C>> first(final A2<P, A, B> p);
    <A, B, C> A2<P, Pair<C, A>, Pair<C, B>> second(final A2<P, A, B> p);
}
