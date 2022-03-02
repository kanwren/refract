package com.github.nprindle.refract;

import java.util.function.Function;
import java.util.function.Consumer;
import java.util.Optional;

public abstract class Either<A, B> implements A1<Either.M1<A>, B>, A2<Either.M2, A, B> {
    public static final class M1<A> implements K1 {}
    public static final <A, B> Either<A, B> unbox(final A1<M1<A>, B> p) {
        return (Either<A, B>) p;
    }

    public static final class M2 implements K2 {}
    public static final <A, B> Either<A, B> unbox2(final A2<M2, A, B> p) {
        return (Either<A, B>) p;
    }

    private Either() {}

    public abstract boolean isLeft();
    public abstract boolean isRight();
    public abstract Optional<A> fromLeft();
    public abstract Optional<B> fromRight();
    public abstract void whenLeft(Consumer<? super A> f);
    public abstract void whenRight(Consumer<? super B> f);
    public abstract <C> Either<C, B> mapLeft(Function<? super A, ? extends C> f);
    public abstract <C> Either<A, C> mapRight(Function<? super B, ? extends C> f);
    public abstract <C> Either<A, C> flatMap(Function<? super B, ? extends Either<A, C>> k);
    public abstract <C> C either(final Function<? super A, ? extends C> onLeft, final Function<? super B, ? extends C> onRight);

    public static class Left<A, B> extends Either<A, B> {
        private final A value;

        public Left(A value) {
            this.value = value;
        }

        @Override
        public boolean isLeft() { return true; }

        @Override
        public boolean isRight() { return false; }

        @Override
        public Optional<A> fromLeft() { return Optional.of(this.value); }

        @Override
        public Optional<B> fromRight() { return Optional.empty(); }

        @Override
        public void whenLeft(Consumer<? super A> f) {
            f.accept(this.value);
        }

        @Override
        public void whenRight(Consumer<? super B> f) {
            return;
        }

        @Override
        public <C> Either<C, B> mapLeft(Function<? super A, ? extends C> f) {
            return new Left(f.apply(this.value));
        }

        @Override
        public <C> Either<A, C> mapRight(Function<? super B, ? extends C> f) {
            return new Left(this.value);
        }

        @Override
        public <C> C either(final Function<? super A, ? extends C> onLeft, final Function<? super B, ? extends C> onRight) {
            return onLeft.apply(this.value);
        }

        @Override
        public <C> Either<A, C> flatMap(Function<? super B, ? extends Either<A, C>> k) {
            return new Left(this.value);
        }
    }

    public static class Right<A, B> extends Either<A, B> {
        private final B value;

        public Right(B value) {
            this.value = value;
        }

        @Override
        public boolean isLeft() { return false; }

        @Override
        public boolean isRight() { return true; }

        @Override
        public Optional<A> fromLeft() { return Optional.empty(); }

        @Override
        public Optional<B> fromRight() { return Optional.of(this.value); }

        @Override
        public void whenLeft(Consumer<? super A> f) {
            return;
        }

        @Override
        public void whenRight(Consumer<? super B> f) {
            f.accept(this.value);
        }

        @Override
        public <C> Either<C, B> mapLeft(Function<? super A, ? extends C> f) {
            return new Right(this.value);
        }

        @Override
        public <C> Either<A, C> mapRight(Function<? super B, ? extends C> f) {
            return new Right(f.apply(this.value));
        }

        @Override
        public <C> C either(final Function<? super A, ? extends C> onLeft, final Function<? super B, ? extends C> onRight) {
            return onRight.apply(this.value);
        }

        @Override
        public <C> Either<A, C> flatMap(Function<? super B, ? extends Either<A, C>> k) {
            return k.apply(this.value);
        }
    }

    public static final class Instances {
        public static <A> Functor<Either.M1<A>> functor() {
            return new Either.Instances.ApplicativeI();
        }

        public static <A> Applicative<Either.M1<A>> applicative() {
            return new Either.Instances.ApplicativeI();
        }

        private static final class ApplicativeI<K> implements Applicative<Either.M1<K>>, Functor<Either.M1<K>> {
            @Override
            public <A, B> A1<Either.M1<K>, B> map(final Function<? super A, ? extends B> f, final A1<Either.M1<K>, A> x) {
                return Either.unbox(x).mapRight(f);
            }

            @Override
            public <A> A1<Either.M1<K>, A> pure(final A x) {
                return new Either.Right(x);
            }

            @Override
            public <A, B> A1<Either.M1<K>, B> ap(final A1<Either.M1<K>, Function<? super A, ? extends B>> f, final A1<Either.M1<K>, A> x) {
                return Either.unbox(f).flatMap(g -> Either.unbox(x).mapRight(y -> g.apply(y)));
            }
        }
    }
}
