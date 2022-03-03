package com.github.nprindle.refract.classes;

public interface Monoid<T> extends Semigroup<T> {
  T empty();
}
