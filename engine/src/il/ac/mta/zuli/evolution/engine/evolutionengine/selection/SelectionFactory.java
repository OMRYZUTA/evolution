package il.ac.mta.zuli.evolution.engine.evolutionengine.selection;

import il.ac.mta.zuli.evolution.engine.TimeTableSolution;
import il.ac.mta.zuli.evolution.engine.exceptions.ValidationException;
import il.ac.mta.zuli.evolution.engine.xmlparser.generated.ETTSelection;

public class SelectionFactory {

    public Selection<TimeTableSolution> createSelection(ETTSelection ettSelection) {
        switch (ettSelection.getType().toLowerCase()) {
            case "truncation":
                return new Truncation(ettSelection);
            default:
                throw new ValidationException("invalid selection type (for ex. 1)");
        }
    }
}
