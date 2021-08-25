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
        //for each solution, in selected parents, organize as map of teacher per dh matrix  map<teacher, list <list<q>>>
        //while there are more than 1 solution in general collection
            //randomly select 2 parents
            //crossover between 2 parents
            //for each teacher, crossover between parent 1 and parent 2. so we have 2 new children per teacher
            //combine to create 2 solutions (without conflicts)
            //flatten both matrixes to return 2 solutions

        //return newGeneration
    }
}
