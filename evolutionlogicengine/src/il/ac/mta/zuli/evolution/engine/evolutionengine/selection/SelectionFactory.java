package il.ac.mta.zuli.evolution.engine.evolutionengine.selection;

import il.ac.mta.zuli.evolution.engine.evolutionengine.Solution;
import il.ac.mta.zuli.evolution.engine.exceptions.ValidationException;

public class SelectionFactory<T extends Solution> {
    public static Selection createSelectionFromInput(String selectionType, int populationSize, int elitism, int topPercent, int pte) {
        switch (selectionType.toLowerCase()) {
            case "truncation":
                return new Truncation(topPercent, populationSize, elitism);
            case "roulettewheel":
                return new RouletteWheel(populationSize, elitism);
            case "tournament":
                return  new Tournament(pte,populationSize,elitism);
            default:
                throw new ValidationException("Invalid selection type (for ex. 3)");
        }
    }


}
