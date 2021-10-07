package il.ac.mta.zuli.evolution.engine.evolutionengine.selection;

import il.ac.mta.zuli.evolution.Constants;
import il.ac.mta.zuli.evolution.engine.evolutionengine.Solution;
import il.ac.mta.zuli.evolution.engine.exceptions.ValidationException;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class SelectionFactory<T extends Solution> {

    public static <T extends Solution> Selection<T> createSelectionFromMap(Map<String, Object> selectionMap, int populationSize) {
        int elitism;
        try {
            elitism = (int) Math.ceil((double) selectionMap.get(Constants.ELITISM));
        } catch (Throwable e) {
            throw new ValidationException("Invalid Selection parameter. " + e.getMessage());
        }

        Map<String, Supplier<Selection<T>>> selectionBuilder = new HashMap<>();

        selectionBuilder.put(Constants.ROULETTE_WHEEL, () -> new RouletteWheel<T>(populationSize, elitism));

        selectionBuilder.put(Constants.TRUNCATION, () -> {
            int topPercent;

            try {
                topPercent = (int) Math.ceil((double) selectionMap.get(Constants.TOP_PERCENT));
            } catch (Throwable e) {
                throw new ValidationException("Invalid Selection parameter. " + e.getMessage());
            }
            return new Truncation<T>(topPercent, populationSize, elitism);
        });

        selectionBuilder.put(Constants.TOURNAMENT, () -> {
            double pte;

            try {
                pte = (double) selectionMap.get(Constants.PTE);
            } catch (Throwable e) {
                throw new ValidationException("Invalid Selection parameter. " + e.getMessage());
            }

            return new Tournament<T>(pte, populationSize, elitism);
        });

        try {
            String selectionType = (String) selectionMap.get(Constants.NAME);
            return selectionBuilder.get(selectionType).get();
        } catch (Throwable e) {
            throw new ValidationException("Invalid Selection type. " + e.getMessage());
        }
    }
}
