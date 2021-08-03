package il.ac.mta.zuli.evolution.engine.evolutionengine.crossover;

import il.ac.mta.zuli.evolution.engine.Quintet;
import il.ac.mta.zuli.evolution.engine.TimeTableSolution;
import il.ac.mta.zuli.evolution.engine.evolutionengine.Solution;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

//public class GenericClassFromGenericInterface<U> implements Moveable<U>
public class DayTimeOriented<S extends Solution> implements Crossover<S> {
    private int numOfCuttingPoints;

    public DayTimeOriented(int numOfCuttingPoints) throws Exception {
        setNumOfCuttingPoints(numOfCuttingPoints);
    }

    @Override
    public List<S> crossover(S s1, S s2) {
        if (!(s1 instanceof TimeTableSolution) || !(s2 instanceof TimeTableSolution)) {
            throw new RuntimeException("solution must be TimeTableSolution");
        }

        TimeTableSolution ttSolution1 = (TimeTableSolution) s1;
        TimeTableSolution ttSolution2 = (TimeTableSolution) s2;

        Collections.sort(ttSolution1.getSolution(), Comparator.comparing(Quintet::getDay)
                .thenComparing(Quintet::getHour));

        Collections.sort(ttSolution2.getSolution(), Comparator.comparing(Quintet::getDay)
                .thenComparing(Quintet::getHour));

        //TODO: need to move crossover implementation to interface, continue from here

        System.out.println("in daytime crossover, after sort: ");
        System.out.println(ttSolution1);

        return null;
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
    public int getCuttingPoints() {
        return numOfCuttingPoints;
    }
}
