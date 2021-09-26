package il.ac.mta.zuli.evolution.engine.evolutionengine.selection;

import il.ac.mta.zuli.evolution.engine.evolutionengine.Solution;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class SelectionFactory<T extends Solution> {

    public static Selection createSelectionFromMap(Map<String, Object> selectionMap, int populationSize) {
        Map<String, Supplier<Selection>> selectionBuilder = new HashMap<>();

        selectionBuilder.put("truncation", () -> {
            return new Truncation((int) selectionMap.get("topPercent"), populationSize, (int) selectionMap.get("elitism"));
        });

        selectionBuilder.put("roulettewheel", () -> {
            return new RouletteWheel(populationSize, (int) selectionMap.get("elitism"));
        });

        selectionBuilder.put("tournament", () -> {
            return new Tournament((double) selectionMap.get("pte"), populationSize, (int) selectionMap.get("elitism"));
        });

        String selectionType = (String) selectionMap.get("name");

        return selectionBuilder.get(selectionType).get();
    }
}
