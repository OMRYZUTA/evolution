package il.ac.mta.zuli.evolution.engine.predicates;

import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

public class GenerationsPredicate implements Predicate<Integer> {
    private final PredicateType type;
    private final int numOfGenerations;

    public GenerationsPredicate(PredicateType type, int numOfGenerations) {
        this.type = type;
        this.numOfGenerations = numOfGenerations;
    }

    @Override
    public boolean test(Integer currGenerationNum) {
        return currGenerationNum <= numOfGenerations;
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

    public PredicateType getType() {
        return type;
    }

    public int getNumOfGenerations() {
        return numOfGenerations;
    }
}
