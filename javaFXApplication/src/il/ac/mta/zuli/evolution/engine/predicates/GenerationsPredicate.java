package il.ac.mta.zuli.evolution.engine.predicates;

public class GenerationsPredicate extends PredicateClass<Double> {
    private final double numOfGenerations;

    public GenerationsPredicate(PredicateType type, double numOfGenerations) {
        super(type);
        this.numOfGenerations = numOfGenerations;
    }

    @Override
    public boolean test(double currGenerationNum) {
        return currGenerationNum <= numOfGenerations;
    }

    public double getNumOfGenerations() {
        return numOfGenerations;
    }
}
