package il.ac.mta.zuli.evolution.engine.rules;

import il.ac.mta.zuli.evolution.engine.Quintet;
import il.ac.mta.zuli.evolution.engine.TimeTableSolution;
import il.ac.mta.zuli.evolution.engine.evolutionengine.Solution;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;

public class TeacherIsHuman extends Rule {
    public TeacherIsHuman(@NotNull String ruleType) {
        super(ruleType);
    }

    @Override
    public void fitnessEvaluation(@NotNull Solution solution) {
        if (!(solution instanceof TimeTableSolution)) {
            throw new RuntimeException("solution must be TimeTableSolution");
        }

        TimeTableSolution timeTableSolution = (TimeTableSolution) solution;

        int numOfQuintets = timeTableSolution.getSolutionSize();
        double score = 0;

        if (numOfQuintets > 0) {
            HashSet<String> teacherDayHourSet = new HashSet<>();
            int collisions = 0;
            String DHT;
            int teacherID;

            for (Quintet quintet : timeTableSolution.getSolutionQuintets()) {
                teacherID = (quintet.getTeacher()).getId();
                DHT = String.format("%s_%d_%d", quintet.getDay(), quintet.getHour(), teacherID);
                //if the set already contains the element, the call leaves the set unchanged and returns false.
                if (!teacherDayHourSet.add(DHT)) {
                    collisions++;
                }
            }

            score = (100 * (numOfQuintets - collisions)) / (double) numOfQuintets;
        }

        timeTableSolution.addScoreToRule(this, score);
    }
}
