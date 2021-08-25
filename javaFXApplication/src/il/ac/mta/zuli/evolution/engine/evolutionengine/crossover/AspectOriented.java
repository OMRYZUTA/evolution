package il.ac.mta.zuli.evolution.engine.evolutionengine.crossover;

import il.ac.mta.zuli.evolution.engine.timetable.TimeTable;

import java.util.List;

public class AspectOriented implements CrossoverInterface {
    private  int cuttingPoints;
    private Orientation orientation;
    private TimeTable timeTable;

    public AspectOriented(int cuttingPoints, Orientation orientation, TimeTable timeTable) {
    }

    @Override
    public int getNumOfCuttingPoints() {
        return 0;
    }

    @Override
    public List crossover(List generation) {
        return null;
    }
}
