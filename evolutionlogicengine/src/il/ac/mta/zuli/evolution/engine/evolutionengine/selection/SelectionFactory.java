package il.ac.mta.zuli.evolution.engine.evolutionengine.selection;

import il.ac.mta.zuli.evolution.Constants;
import il.ac.mta.zuli.evolution.engine.evolutionengine.Solution;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class SelectionFactory<T extends Solution> {

    public static <T extends Solution> Selection<T> createSelectionFromMap(Map<String, Object> selectionMap, int populationSize) {

        Map<String, Supplier<Selection<T>>> selectionBuilder = new HashMap<>();

        selectionBuilder.put(Constants.TRUNCATION, () -> {
            return new Truncation<T>(
                    (int) selectionMap.get(Constants.TOP_PERCENT),
                    populationSize,
                    (int) selectionMap.get(Constants.ELITISM));
        });

        selectionBuilder.put(Constants.ROULETTE_WHEEL, () -> {
            return new RouletteWheel<T>(populationSize, (int) selectionMap.get(Constants.ELITISM));
        });

        selectionBuilder.put(Constants.TOURNAMENT, () -> {
            return new Tournament<T>(
                    (double) selectionMap.get(Constants.PTE),
                    populationSize,
                    (int) selectionMap.get(Constants.ELITISM));
        });

        String selectionType = (String) selectionMap.get(Constants.NAME);

        return selectionBuilder.get(selectionType).get();
    }
}
