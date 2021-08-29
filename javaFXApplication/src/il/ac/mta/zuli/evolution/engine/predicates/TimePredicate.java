package il.ac.mta.zuli.evolution.engine.predicates;

import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

public class TimePredicate implements Predicate<Integer> {
    private final int totalMinutes;
    private final String name;

    public TimePredicate(int totalMinutes, String name) {
        this.totalMinutes = totalMinutes;
        this.name = name;
    }

    @Override
    public boolean test(Integer currMinutes) {
        return currMinutes<=totalMinutes;
    }

    @NotNull
    @Override
    public Predicate<Integer> and(@NotNull Predicate<? super Integer> other) {
        return Predicate.super.and(other);
    }

    @NotNull
    @Override
    public Predicate<Integer> negate() {
        return Predicate.super.negate();
    }

    @NotNull
    @Override
    public Predicate<Integer> or(@NotNull Predicate<? super Integer> other) {
        return Predicate.super.or(other);
    }

    public int getTotalMinutes() {
        return totalMinutes;
    }

    public String getName() {
        return name;
    }
}
