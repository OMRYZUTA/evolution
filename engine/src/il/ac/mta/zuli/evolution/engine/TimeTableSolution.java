package il.ac.mta.zuli.evolution.engine;

import il.ac.mta.zuli.evolution.engine.evolutionengine.Solution;
import il.ac.mta.zuli.evolution.engine.rules.Rule;
import il.ac.mta.zuli.evolution.engine.timetable.SchoolClass;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

public class TimeTableSolution implements Solution {
    private Collection<Quintet> solution;
    private int solutionSize; //number of quintets
    private int totalFitnessScore;
    private Map<Rule, Integer> fitnessScorePerRole;

    public TimeTableSolution() {
        int numOfQuintets = setRandNumOfQuintets();
        //loop to randomly get that number of quintets
        //inside the loop : randomly/getQuintet
    }

    public int getSolutionSize() {
        return solutionSize;
    }

    private int setRandNumOfQuintets() {
        return 0;
    }

    private void setSolutionSize(int solutionSize) {
        this.solutionSize = solutionSize;
    }

    public Collection<Quintet> getSolution() {
        return Collections.unmodifiableCollection(solution);
    }

    public TimeTableSolution getSolutionforClass(SchoolClass sc) {
        TimeTableSolution classSolution = new TimeTableSolution();

        //get only the quintets for sc
        // solution.stream().

        return classSolution;
    }
}
