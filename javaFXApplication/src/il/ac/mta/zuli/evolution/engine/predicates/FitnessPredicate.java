package il.ac.mta.zuli.evolution.engine.predicates;

import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

public class FitnessPredicate implements Predicate<Double> {
    private final PredicateType type;
    private final double scoreGoal;

    public FitnessPredicate(PredicateType type, Double score) {
        this.type = type;
        this.scoreGoal = score;
    }

    @Override
    public boolean test(Double score) {
        return score <= scoreGoal;
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

    public PredicateType getType() {
        return type;
    }

    public double getScoreGoal() {
        return scoreGoal;
    }
}
