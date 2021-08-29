package il.ac.mta.zuli.evolution.engine.predicates;

public class FitnessPredicate extends PredicateClass<Double> {
    private final double scoreGoal;

    public FitnessPredicate(PredicateType type, Double score) {
        super(type);
        this.scoreGoal = score;
    }

    @Override
    public boolean test(Double score) {
        return score <= scoreGoal;
    }

    public double getScoreGoal() {
        return scoreGoal;
    }
}
