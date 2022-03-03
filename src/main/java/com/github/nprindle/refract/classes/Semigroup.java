package com.github.nprindle.refract.classes;

public interface Semigroup<T> {
    T append(final T a, final T b);
}
