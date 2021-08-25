package il.ac.mta.zuli.evolution.engine.evolutionengine.crossover;

import il.ac.mta.zuli.evolution.engine.evolutionengine.Solution;
import il.ac.mta.zuli.evolution.engine.timetable.TimeTable;

import java.util.List;

public class AspectOriented<S extends Solution> extends Crossover<S> {
    private Orientation orientation;

    public AspectOriented(int cuttingPoints, Orientation orientation, TimeTable timeTable) {
        super(cuttingPoints, timeTable);
        this.orientation = orientation;
    }


    @Override
    public List<S> crossover(List<S> selectedParents) {
        //TODO implement

        return super.crossoverDH(selectedParents);
    }
}
