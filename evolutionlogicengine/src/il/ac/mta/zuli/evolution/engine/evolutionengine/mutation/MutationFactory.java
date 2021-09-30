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
            int totalTuples;
            double probability;

            try {
                totalTuples = Integer.parseInt((String) mutationMap.get(Constants.TOTAL_TUPLES));
                probability = Double.parseDouble((String) mutationMap.get(Constants.PROBABILITY));
            } catch (Throwable e) {
                throw new ValidationException("Invalid Mutation parameter." + e.getMessage());
            }

            if (totalTuples > timeTable.getHours() * timeTable.getDays()) {
                throw new ValidationException("Positive-totalTuples must be less than (days * hours), invalid value: " + totalTuples);
            }

            if (totalTuples < (-1) * timeTable.getHours() * timeTable.getDays()) {
                throw new ValidationException("Negative--totalTuples must be greater than -(days * hours), invalid value: " + totalTuples);
            }

            return new Sizer<T>(probability, totalTuples, timeTable);
        });

        mutationBuilder.put(Constants.FLIPPING, () -> {
            int maxTuples;
            double probability;

            try {
                maxTuples = Integer.parseInt((String) mutationMap.get(Constants.MAX_TUPLES));
                probability = Double.parseDouble((String) mutationMap.get(Constants.PROBABILITY));
            } catch (Throwable e) {
                throw new ValidationException("Invalid Mutation parameter." + e.getMessage());
            }

            if (maxTuples < 0) {
                throw new ValidationException("MaxTuples must be >=0, invalid value: " + maxTuples);
            }

            ComponentName component = parseComponent(mutationMap);

            return new Flipping<T>(probability, maxTuples, component, timeTable);
        });

        try {
            String mutationType = (String) mutationMap.get(Constants.NAME);
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
            throw new ValidationException("Invalid component");
        }

        return component;
    }
}

