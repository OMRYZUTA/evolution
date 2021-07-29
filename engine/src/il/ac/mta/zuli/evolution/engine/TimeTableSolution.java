package il.ac.mta.zuli.evolution.engine;

import il.ac.mta.zuli.evolution.engine.evolutionengine.Solution;

import java.util.Collection;

public class TimeTableSolution implements Solution {
    Collection<Quintet> solution;

    public TimeTableSolution() {
        int numOfQuintets = setRandNumOfQuintets();
        //loop to randomly get that number of quintes
        //inside the loop : randomly/getQuintet
    }

    public Collection<Quintet> getSolution() {
        return solution;
    }

    public int setRandNumOfQuintets() {
        return 0;
    }

}
