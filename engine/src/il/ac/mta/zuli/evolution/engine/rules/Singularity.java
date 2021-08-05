package il.ac.mta.zuli.evolution.engine.rules;

import il.ac.mta.zuli.evolution.engine.Quintet;
import il.ac.mta.zuli.evolution.engine.TimeTableSolution;
import il.ac.mta.zuli.evolution.engine.evolutionengine.Solution;

import java.util.HashSet;

public class Singularity extends Rule {
    public Singularity(String ruleType) {
        super(ruleType);
    }

    @Override
    public void fitnessEvaluation(Solution solution) {
        if (!(solution instanceof TimeTableSolution)) {
            throw new RuntimeException("solution must be TimeTableSolution");
        }

        TimeTableSolution timeTableSolution = (TimeTableSolution) solution;

        int numOfQuintets = timeTableSolution.getSolutionSize();
        double score = INVALIDSCORE;

        if (numOfQuintets > 0) {
            HashSet<String> classDayHourSet = new HashSet<>();
            int collisions = 0;
            String DHC;
            int classID;

            for (Quintet quintet : timeTableSolution.getSolutionQuintets()) {
                classID = (quintet.getSchoolClass()).getId();
                DHC = String.format("%s_%d_%d", quintet.getDay(), quintet.getHour(), classID);
                //if the set already contains the element, the call leaves the set unchanged and returns false.
                if (!classDayHourSet.add(DHC)) {
                    collisions++;
                }
            }

            score = (100 * (numOfQuintets - collisions)) / (double) numOfQuintets;
        }

        timeTableSolution.addScoreToRule(this, score);
    }
}