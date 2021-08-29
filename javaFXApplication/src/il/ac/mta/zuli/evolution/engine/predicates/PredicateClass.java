package il.ac.mta.zuli.evolution.engine.predicates;

import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

public abstract class PredicateClass implements Predicate<Double> {
    private final PredicateType type;
    private final double benchmark;

    public PredicateClass(PredicateType type, double benchmark) {
        this.type = type;
        this.benchmark = benchmark;
    }

    public PredicateType getType() {
        return type;
    }

    public double getParameter() {
        return benchmark;
    }

    @Override
    public boolean test(double parameter) {
        //currMinutes <= totalMinutes, currGenerationNum <= numOfGenerations, score <= scoreGoal
        return parameter <= benchmark;
    }

    @NotNull
    @Override
    public Predicate<Double> and(@NotNull Predicate<? super Double> other) {
        return Predicate.super.and(other);
    }

    @NotNull
    @Override
    public Predicate<Double> negate() {
        return Predicate.super.negate();
    }

    @NotNull
    @Override
    public Predicate<Double> or(@NotNull Predicate<? super Double> other) {
        return Predicate.super.or(other);
    }
}
