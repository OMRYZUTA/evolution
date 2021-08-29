package il.ac.mta.zuli.evolution.engine.predicates;

import java.util.function.Predicate;

public class PredicateFactory <T>{
    public  Predicate<T> createPredicate(PredicateType type, T parameter){
        Predicate<T> predicate;
        switch (type){
            case FITNESS:
                predicate = new FitnessPredicate(type, parameter);
        }
    }
}
