package de.leon_lp9.challengePlugin.management;

import java.util.Objects;
import java.util.function.Function;
import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class Triple<F, S, T> implements Pair {
    protected F first;
    protected S second;
    protected T third;

    public Triple() {
    }

    public Triple(@Nullable F first, @Nullable S second, @Nullable T third) {
        this.first = first;
        this.second = second;
        this.third = third;
    }

    public final int amount() {
        return 3;
    }

    @Nonnull
    public final Object[] values() {
        return new Object[]{this.first, this.second, this.third};
    }

    public F getFirst() {
        return this.first;
    }

    public S getSecond() {
        return this.second;
    }

    public T getThird() {
        return this.third;
    }

    public void setFirst(@Nullable F first) {
        this.first = first;
    }

    public void setSecond(@Nullable S second) {
        this.second = second;
    }

    public void setThird(@Nullable T third) {
        this.third = third;
    }

    @Nonnull
    @CheckReturnValue
    public <ToF, ToS, ToT> Triple<ToF, ToS, ToT> map(@Nonnull Function<? super F, ? extends ToF> firstMapper, @Nonnull Function<? super S, ? extends ToS> secondMapper, @Nonnull Function<? super T, ? extends ToT> thirdMapper) {
        return of(firstMapper.apply(this.first), secondMapper.apply(this.second), thirdMapper.apply(this.third));
    }

    public boolean noneNull() {
        return this.first != null && this.second != null && this.third != null;
    }

    public boolean allNull() {
        return this.first == null && this.second == null && this.third == null;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            Triple<?, ?, ?> triple = (Triple<?, ?, ?>) o;
            return Objects.equals(this.first, triple.first) && Objects.equals(this.second, triple.second) && Objects.equals(this.third, triple.third);
        } else {
            return false;
        }
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.first, this.second, this.third});
    }

    public String toString() {
        return "Triple[" + this.first + ", " + this.second + ", " + this.third + "]";
    }

    @Nonnull
    public static <F, S, T> Triple<F, Object, Object> ofFirst(@Nullable F first) {
        return of(first, (Object)null, (Object)null);
    }

    @Nonnull
    public static <F, S, T> Triple<Object, S, Object> ofSecond(@Nullable S second) {
        return of((Object)null, second, (Object)null);
    }

    @Nonnull
    public static <F, S, T> Triple<Object, Object, T> ofThird(@Nullable T third) {
        return of((Object)null, (Object)null, third);
    }

    @Nonnull
    public static <F, S, T> Triple<F, S, T> of(@Nullable F first, @Nullable S second, @Nullable T third) {
        return new Triple(first, second, third);
    }

    @Nonnull
    public static <F, S, T> Triple<F, S, T> empty() {
        return new Triple();
    }
}
