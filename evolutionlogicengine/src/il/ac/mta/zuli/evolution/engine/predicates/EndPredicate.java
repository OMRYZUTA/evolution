package il.ac.mta.zuli.evolution.engine.predicates;

import il.ac.mta.zuli.evolution.engine.exceptions.ValidationException;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

public class EndPredicate implements Predicate<Double> {
    private EndPredicateType type;
    private double benchmark;

    public EndPredicate(EndPredicateType predicateType, double value, int generationStride) {
        setType(predicateType);
        setBenchmark(value, generationStride);
    }

    public void setType(EndPredicateType type) {
        this.type = type;
    }

    public void setBenchmark(double value, int stride) {
        //validating the double received fits the constraints of relevant conditionType
        switch (type) {
            case GENERATIONS:
                setGenerationNum(value, stride);
                break;
            case SCORE:
                setFitnessScore(value);
                break;
            case TIME:
                setMinutes(value);
        }
    }

    public double getBenchmark() {
        return benchmark;
    }

    private void setGenerationNum(double value, int stride) {
        if (value <= 100) {
            throw new ValidationException("Generation number must be greater than 100");
        } else if (stride > value) {
            throw new ValidationException("Stride must be less than total generations ");
        } else {
            this.benchmark = value;
        }
    }

    private void setFitnessScore(double value) {
        if (value < 1f || value > 100f) {
            throw new ValidationException("Fitness must be between 1.0 to 100.0 (including)");
        } else {
            this.benchmark = value;
        }
    }

    private void setMinutes(double value) {
        if (value <= 0) {
            throw new ValidationException("Minutes must be a positive number");
        } else {
            this.benchmark = value;
        }
    }

    public EndPredicateType getType() {
        return type;
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

    @Override
    public boolean test(Double aDouble) {
        //currMinutes < totalMinutes, currGenerationNum < numOfGenerations, score < scoreGoal
        return aDouble < benchmark;
    }
}
