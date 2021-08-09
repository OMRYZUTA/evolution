package il.ac.mta.zuli.evolution.engine.rules;

import il.ac.mta.zuli.evolution.engine.Quintet;
import il.ac.mta.zuli.evolution.engine.TimeTableSolution;
import il.ac.mta.zuli.evolution.engine.evolutionengine.Solution;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;

public class Singularity extends Rule {
    public Singularity(@NotNull String ruleType) {
        super(ruleType);
    }

    @Override
    public void fitnessEvaluation(@NotNull Solution solution) {
        if (!(solution instanceof TimeTableSolution)) {
            throw new RuntimeException("solution must be TimeTableSolution");
        }

        TimeTableSolution timeTableSolution = (TimeTableSolution) solution;

        int numOfQuintets = timeTableSolution.getSolutionSize();
        int collisions = 0;
        double score = 0;

        if (numOfQuintets > 0) {
            HashSet<String> classDayHourSet = new HashSet<>();
            String DHC;
            int classID;

            for (Quintet quintet : timeTableSolution.getSolutionQuintets()) {
                classID = (quintet.getSchoolClass()).getId();
                DHC = String.format("%s_%d_%d", quintet.getDay(), quintet.getHour(), classID);
                //if the set already contains the element, the call leaves the set unchanged and returns false.
                if (!classDayHourSet.add(DHC)) {
                    collisions++;
                    if (this.isHardRule()) {
                        break;
                    }
                }
            }
            if (this.isHardRule() && 0 < collisions) {
                score = 0;
            }

            score = (100 * (numOfQuintets - collisions)) / (double) numOfQuintets;
        }

        if (this.isHardRule() && collisions > 0) {
//            score = 0;
            score /= 2;
        }

        timeTableSolution.addScoreToRule(this, score);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}