package il.ac.mta.zuli.evolution.dto;

import il.ac.mta.zuli.evolution.engine.predicates.EndPredicate;
import il.ac.mta.zuli.evolution.engine.predicates.EndPredicateType;

import java.util.List;

public class EndPredicatesDTO {
    private Double numOfGenerations = null;
    private Double fitnessScore = null;
    private Double time = null;

    public EndPredicatesDTO(List<EndPredicate> endPredicates) {
        for (EndPredicate predicate : endPredicates) {
            EndPredicateType type = predicate.getType();
            double value = predicate.getBenchmark();

            switch (type) {
                case GENERATIONS:
                    numOfGenerations = value;
                    break;
                case SCORE:
                    fitnessScore = value;
                    break;
                case TIME:
                    time = value;
            }
        }
    }
}
