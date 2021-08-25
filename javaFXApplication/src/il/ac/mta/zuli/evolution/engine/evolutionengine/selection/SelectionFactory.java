package il.ac.mta.zuli.evolution.engine.evolutionengine.selection;

import il.ac.mta.zuli.evolution.engine.evolutionengine.Solution;
import il.ac.mta.zuli.evolution.engine.exceptions.ValidationException;
import il.ac.mta.zuli.evolution.engine.xmlparser.generated.ex2.ETTSelection;

public class SelectionFactory<T extends Solution> {
    //TODO - createselectionfrom ett vs from user(make sure all references to ett only happen in this level.
    //reomve all ett from selction constructor.
    public static Selection createSelection(ETTSelection ettSelection) {
        switch (ettSelection.getType().toLowerCase()) {
            case "truncation":
                return new Truncation(ettSelection);
            case "roulettewheel":
                return new RouletteWheel();
            default:
                throw new ValidationException("Invalid selection type (for ex. 1)");
        }
    }
}
