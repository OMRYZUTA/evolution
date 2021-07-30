package il.ac.mta.zuli.evolution.engine;

import il.ac.mta.zuli.evolution.engine.evolutionengine.Solution;
import il.ac.mta.zuli.evolution.engine.rules.Rule;

import java.util.Collection;
import java.util.Map;

public class TimeTableSolution implements Solution {
    Collection<Quintet> solution;
    int totalFitnessScore;
    Map<Rule, Integer> fitnessScorePerRole;

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
