package il.ac.mta.zuli.evolution.engine.evolutionengine.selection;

import il.ac.mta.zuli.evolution.engine.evolutionengine.Solution;
import il.ac.mta.zuli.evolution.engine.exceptions.ValidationException;
import il.ac.mta.zuli.evolution.engine.xmlparser.generated.ETTSelection;

public  class SelectionFactory<T extends Solution> {

    public static Selection createSelection(ETTSelection ettSelection) {
        switch (ettSelection.getType().toLowerCase()) {
            case "truncation":
                return new Truncation(ettSelection);
            default:
                throw new ValidationException("invalid selection type (for ex. 1)");
        }
    }
}
