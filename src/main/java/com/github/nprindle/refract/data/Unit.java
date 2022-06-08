package com.github.nprindle.refract.data;

import com.github.nprindle.refract.classes.Monoid;
import com.github.nprindle.refract.classes.Semigroup;

/** The unit type, with only one inhabitant (modulo nulls in Java). */
public enum Unit {
  UNIT;

  public static final class Instances {
    private Instances() {}

    public static Semigroup<? extends Semigroup.Mu, Unit> semigroup() {
      return Unit.Instances.MonoidI.INSTANCE;
    }

    public static Monoid<? extends Monoid.Mu, Unit> monoid() {
      return Unit.Instances.MonoidI.INSTANCE;
    }

    private static enum MonoidI implements Monoid<MonoidI.Mu, Unit> {
      INSTANCE;

      public static final class Mu implements Monoid.Mu {}

      @Override
      public Unit empty() {
        return Unit.UNIT;
      }

      @Override
      public Unit append(Unit a, Unit b) {
        return Unit.UNIT;
      }
    }
  }
}
