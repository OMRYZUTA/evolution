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

        double probability;
        try {
            probability = (double) mutationMap.get(Constants.PROBABILITY);
        } catch (Throwable e) {
            throw new ValidationException("Invalid Mutation parameter." + e.getMessage());
        }

        Map<String, Supplier<Mutation<T>>> mutationBuilder = new HashMap<>();

        mutationBuilder.put(Constants.SIZER, () -> {
            int totalTuples;

            try {
                totalTuples = (int) Math.ceil((double) mutationMap.get(Constants.TOTAL_TUPLES));
            } catch (Throwable e) {
                throw new ValidationException("Invalid Mutation parameter (for Sizer)." + e.getMessage());
            }

            return new Sizer<T>(probability, totalTuples, timeTable);
        });

        mutationBuilder.put(Constants.FLIPPING, () -> {
            int maxTuples;

            try {
                maxTuples = (int) Math.ceil((double) mutationMap.get(Constants.MAX_TUPLES));
            } catch (Throwable e) {
                throw new ValidationException("Invalid Mutation parameter (for Flipping)." + e.getMessage());
            }

            ComponentName component = parseComponent(mutationMap);

            return new Flipping<T>(probability, maxTuples, component, timeTable);
        });

        try {
            String mutationType = (String) mutationMap.get(Constants.TYPE);
            return mutationBuilder.get(mutationType).get();
        } catch (Throwable e) {
            throw new ValidationException("Invalid Mutation type. " + e.getMessage());
        }
    }

    private static ComponentName parseComponent(Map<String, Object> mutationMap) {
        ComponentName component;

        try {
            String ComponentStr = (String) mutationMap.get(Constants.COMPONENT);
            component = ComponentName.valueOf(ComponentStr);
        } catch (Throwable e) {
            throw new ValidationException("Invalid Mutation Component");
        }

        return component;
    }
}

