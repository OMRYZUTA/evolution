package il.ac.mta.zuli.evolution.engine.evolutionengine.selection;

import il.ac.mta.zuli.evolution.Constants;
import il.ac.mta.zuli.evolution.engine.evolutionengine.Solution;
import il.ac.mta.zuli.evolution.engine.exceptions.ValidationException;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class SelectionFactory<T extends Solution> {

    public static <T extends Solution> Selection<T> createSelectionFromMap(Map<String, Object> selectionMap, int populationSize) {
        int elitism = 0;

        if (selectionMap.containsKey(Constants.ELITISM)) {
            try {
                elitism = (int) Math.ceil((double) selectionMap.get(Constants.ELITISM));
            } catch (Throwable e) {
                throw new ValidationException("Invalid Selection parameter. " + e.getMessage());
            }
        }

        Map<String, Supplier<Selection<T>>> selectionBuilder = new HashMap<>();

        int finalElitism = elitism;

        selectionBuilder.put(Constants.ROULETTE_WHEEL, () -> new RouletteWheel<T>(populationSize, finalElitism));


        selectionBuilder.put(Constants.TRUNCATION, () -> {
            int topPercent;

            try {
                topPercent = (int) Math.ceil((double) selectionMap.get(Constants.TOP_PERCENT));
            } catch (Throwable e) {
                throw new ValidationException("Invalid Selection parameter. " + e.getMessage());
            }
            return new Truncation<T>(topPercent, populationSize, finalElitism);
        });

        selectionBuilder.put(Constants.TOURNAMENT, () -> {
            double pte;

            try {
                pte = (double) selectionMap.get(Constants.PTE);
            } catch (Throwable e) {
                throw new ValidationException("Invalid Selection parameter. " + e.getMessage());
            }

            return new Tournament<T>(pte, populationSize, finalElitism);
        });

        try {
            String selectionType = (String) selectionMap.get(Constants.TYPE);
            return selectionBuilder.get(selectionType).get();
        } catch (Throwable e) {
            throw new ValidationException("Invalid Selection type. " + e.getMessage());
        }
    }
}
