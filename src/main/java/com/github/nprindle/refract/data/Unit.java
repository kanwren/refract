package com.github.nprindle.refract.data;

import com.github.nprindle.refract.classes.Semigroup;
import com.github.nprindle.refract.classes.Monoid;

public enum Unit {
    UNIT;

    public static final class Instances {
        public static Semigroup<Unit> semigroup() {
            return Unit.Instances.MonoidI.INSTANCE;
        }

        public static Monoid<Unit> monoid() {
            return Unit.Instances.MonoidI.INSTANCE;
        }

        private static enum MonoidI implements Monoid<Unit> {
            INSTANCE;

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
