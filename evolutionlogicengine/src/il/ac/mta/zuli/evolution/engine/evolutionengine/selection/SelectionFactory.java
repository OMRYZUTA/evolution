package il.ac.mta.zuli.evolution.engine.evolutionengine.selection;

import il.ac.mta.zuli.evolution.Constants;
import il.ac.mta.zuli.evolution.engine.evolutionengine.Solution;
import il.ac.mta.zuli.evolution.engine.exceptions.ValidationException;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class SelectionFactory<T extends Solution> {

    public static <T extends Solution> Selection<T> createSelectionFromMap(Map<String, Object> selectionMap, int populationSize) {

        Map<String, Supplier<Selection<T>>> selectionBuilder = new HashMap<>();

        selectionBuilder.put(Constants.TRUNCATION, () -> {
            int topPercent, elitism;

            try {
                topPercent = (int) selectionMap.get(Constants.TOP_PERCENT);
                elitism = (int) selectionMap.get(Constants.ELITISM);
            } catch (Throwable e) {
                throw new ValidationException("Invalid Selection parameters (must be Integers)." + e.getMessage());
            }

            return new Truncation<T>(topPercent, populationSize, elitism);
        });

        selectionBuilder.put(Constants.ROULETTE_WHEEL, () -> {
            int elitism;

            try {
                elitism = (int) selectionMap.get(Constants.ELITISM);
            } catch (Throwable e) {
                throw new ValidationException("Invalid Selection elitism (must be Integer). " + e.getMessage());
            }

            return new RouletteWheel<T>(populationSize, elitism);
        });

        selectionBuilder.put(Constants.TOURNAMENT, () -> {
            double pte;
            int elitism;

            try {
                pte = (double) selectionMap.get(Constants.PTE);
                elitism = (int) selectionMap.get(Constants.ELITISM);
            } catch (Throwable e) {
                throw new ValidationException("Invalid Selection parameters. " + e.getMessage());
            }

            return new Tournament<T>(pte, populationSize, elitism);
        });

        String selectionType;

        try {
            selectionType = (String) selectionMap.get(Constants.NAME);
        } catch (Throwable e) {
            throw new ValidationException("Invalid Selection type. " + e.getMessage());
        }

        return selectionBuilder.get(selectionType).get();
    }
}
