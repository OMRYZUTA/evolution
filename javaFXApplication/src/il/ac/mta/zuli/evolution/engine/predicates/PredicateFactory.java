package il.ac.mta.zuli.evolution.engine.predicates;

import il.ac.mta.zuli.evolution.engine.exceptions.ValidationException;

public class PredicateFactory<T> {
    public PredicateClass<T> createPredicate(PredicateType type, T parameter) {
        PredicateClass<T> predicate = null;

        switch (type) {
            case FITNESS:
                predicate = new FitnessPredicate(type, (Double) parameter);
                break;
            case GENERATIONS:
                predicate = new GenerationsPredicate(type, (Integer) parameter);
                break;
            case TIME:
                predicate = new TimePredicate(type, (Integer) parameter);
                break;
            default:
                throw new ValidationException("Invalid finish-condition for ex. 2");
        }

        return predicate;
    }
}
