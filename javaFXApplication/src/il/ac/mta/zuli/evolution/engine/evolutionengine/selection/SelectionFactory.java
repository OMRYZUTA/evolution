package il.ac.mta.zuli.evolution.engine.evolutionengine.selection;

import il.ac.mta.zuli.evolution.engine.evolutionengine.Solution;
import il.ac.mta.zuli.evolution.engine.exceptions.ValidationException;
import il.ac.mta.zuli.evolution.engine.xmlparser.generated.ex2.ETTSelection;

public class SelectionFactory<T extends Solution> {
    public static Selection createSelectionFromETT(ETTSelection ettSelection, int populationSize) {
        switch (ettSelection.getType().toLowerCase()) {
            case "truncation":
                return new Truncation(ettSelection, populationSize);
            case "roulettewheel":
                return new RouletteWheel(ettSelection, populationSize);
            default:
                throw new ValidationException("Invalid selection type (for ex. 3)");
        }
    }

    public static Selection createSelectionFromInput(String selectionType, int populationSize, int elitism, int topPercent) {
        switch (selectionType.toLowerCase()) {
            case "truncation":
                return new Truncation(topPercent, populationSize, elitism);
            case "roulettewheel":
                return new RouletteWheel(populationSize, elitism);
            default:
                throw new ValidationException("Invalid selection type (for ex. 3)");
        }
    }


}
