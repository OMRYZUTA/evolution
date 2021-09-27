package il.ac.mta.zuli.evolution.engine.evolutionengine.mutation;

import il.ac.mta.zuli.evolution.Constants;
import il.ac.mta.zuli.evolution.engine.evolutionengine.Solution;
import il.ac.mta.zuli.evolution.engine.exceptions.ValidationException;
import il.ac.mta.zuli.evolution.engine.timetable.TimeTable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class MutationFactory {
    public static <T extends Solution> Mutation<T> createMutationFromMap(
            Map<String, Object> mutationMap,
            TimeTable timeTable) {

        Map<String, Supplier<Mutation<T>>> mutationBuilder = new HashMap<>();

        mutationBuilder.put(Constants.SIZER, () -> {
            //TODO add try-catch to casting like in SelectionFactory
            int totalTuples = (int) mutationMap.get(Constants.TOTAL_TUPLES);

            if (totalTuples > timeTable.getHours() * timeTable.getDays()) {
                throw new ValidationException("positive-total-tuples must be < (days * hours), invalid value: " + totalTuples);
            }

            if (totalTuples < (-1) * timeTable.getHours() * timeTable.getDays()) {
                throw new ValidationException("negative-total-tuples must be > -(days * hours), invalid value: " + totalTuples);
            }

            return new Sizer<T>(
                    (double) mutationMap.get(Constants.PROBABILITY),
                    totalTuples,
                    timeTable);
        });

        mutationBuilder.put(Constants.FLIPPING, () -> {
            int maxTuples = (int) mutationMap.get(Constants.MAX_TUPLES);

            if (maxTuples < 0) {
                throw new ValidationException("max tuples must be >=0, invalid value: " + maxTuples);
            }

            ComponentName component = parseComponent(mutationMap);

            return new Flipping<T>(
                    (double) mutationMap.get(Constants.PROBABILITY),
                    maxTuples,
                    component,
                    timeTable);
        });

        String mutationType = (String) mutationMap.get(Constants.NAME);

        return mutationBuilder.get(mutationType).get();
    }

    private static ComponentName parseComponent(Map<String, Object> mutationMap) {
        String ComponentStr = (String) mutationMap.get(Constants.COMPONENT);
        ComponentName component;

        try {
            component = ComponentName.valueOf(ComponentStr);
        } catch (Throwable e) {
            throw new ValidationException("invalid component");
        }

        return component;
    }
}

