package il.ac.mta.zuli.evolution.engine.evolutionengine.crossover;

import il.ac.mta.zuli.evolution.engine.evolutionengine.Solution;
import il.ac.mta.zuli.evolution.engine.timetable.TimeTable;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class DayTimeOriented<S extends Solution> extends Crossover<S> {

    public DayTimeOriented(int numOfCuttingPoints, @NotNull TimeTable timeTable) {
        super(numOfCuttingPoints, timeTable);
    }

    @Override
    public List<S> crossover(List<S> selectedParents) {
        return super.crossoverDH(selectedParents);
    }

    @Override
    public String toString() {
        return "crossover: " + this.getClass().getSimpleName() +
                "numOfCuttingPoints=" + super.getNumOfCuttingPoints();
    }
}
