package il.ac.mta.zuli.evolution.engine.predicates;

import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

public class FinishPredicate implements Predicate<Double> {
    private final EndConditionType type;
    private final double benchmark;

    public FinishPredicate(EndConditionType type, double benchmark) {
        this.type = type;
        this.benchmark = benchmark;
    }

    @Override
    public boolean test(Double aDouble) {
        //currMinutes <= totalMinutes, currGenerationNum <= numOfGenerations, score <= scoreGoal
        return aDouble <= benchmark;
    }

    public EndConditionType getType() {
        return type;
    }

    public double getParameter() {
        return benchmark;
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
