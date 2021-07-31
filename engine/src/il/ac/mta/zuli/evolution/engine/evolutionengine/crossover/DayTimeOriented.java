package il.ac.mta.zuli.evolution.engine.evolutionengine.crossover;

import il.ac.mta.zuli.evolution.engine.TimeTableSolution;

import java.util.List;

//specific crossover for the timetable solution
public class DayTimeOriented implements Crossover<TimeTableSolution> {
    private int numOfCuttingPoints;

    public DayTimeOriented(int numOfCuttingPoints) throws Exception {
        setNumOfCuttingPoints(numOfCuttingPoints);
    }

    public int getNumOfCuttingPoints() {
        return numOfCuttingPoints;
    }

    private void setNumOfCuttingPoints(int numOfCuttingPoints) throws Exception {
        if (numOfCuttingPoints > 0) {
            this.numOfCuttingPoints = numOfCuttingPoints;
        } else {
            throw new Exception("number of cutting points must be a positive integer");
        }
    }

    @Override
    public String toString() {
        return "crossover: " + this.getClass().getSimpleName() +
                "numOfCuttingPoints=" + numOfCuttingPoints;
    }

    @Override
    public List<TimeTableSolution> crossover(TimeTableSolution s1, TimeTableSolution s2) {

        return null;
    }

    @Override
    public String getConfiguration() {
        return null;
    }

}
