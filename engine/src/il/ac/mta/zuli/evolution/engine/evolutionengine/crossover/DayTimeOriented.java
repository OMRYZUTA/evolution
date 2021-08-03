package il.ac.mta.zuli.evolution.engine.evolutionengine.crossover;

import il.ac.mta.zuli.evolution.engine.evolutionengine.Solution;

import java.util.List;

//public class GenericClassFromGenericInterface<U> implements Moveable<U>
public class DayTimeOriented<S extends Solution> implements Crossover<S> {
    private int numOfCuttingPoints;

    public DayTimeOriented(int numOfCuttingPoints) throws Exception {
        setNumOfCuttingPoints(numOfCuttingPoints);
    }

    @Override
    public List<S> crossover(S s1, S s2) {

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
