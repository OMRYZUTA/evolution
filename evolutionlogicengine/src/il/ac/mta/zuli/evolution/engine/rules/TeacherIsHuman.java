package il.ac.mta.zuli.evolution.engine.rules;

import il.ac.mta.zuli.evolution.engine.Quintet;
import il.ac.mta.zuli.evolution.engine.TimetableSolution;
import il.ac.mta.zuli.evolution.engine.evolutionengine.Solution;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Objects;

public class TeacherIsHuman extends Rule {
    private final String ruleName;
    public TeacherIsHuman(@NotNull String ruleType) {
        super(ruleType);
        this.ruleName="TeacherIsHuman";
    }

    @Override
    public void fitnessEvaluation(@NotNull Solution solution) {
        if (!(solution instanceof TimetableSolution)) {
            throw new RuntimeException("solution must be TimeTableSolution");
        }

        TimetableSolution timeTableSolution = (TimetableSolution) solution;

        int numOfQuintets = timeTableSolution.getSolutionSize();
        int collisions = 0;
        double score = 0; //if it's an empty solution

        if (numOfQuintets > 0) {
            HashSet<String> teacherDayHourSet = new HashSet<>();
            String DHT;
            int teacherID;

            for (Quintet quintet : timeTableSolution.getSolutionQuintets()) {
                teacherID = (quintet.getTeacher()).getId();
                DHT = String.format("%s_%d_%d", quintet.getDay(), quintet.getHour(), teacherID);
                //if the set already contains the element, the call leaves the set unchanged and returns false.
                if (!teacherDayHourSet.add(DHT)) {
                    collisions++;

                    if (this.isHardRule()) {
                        break;
                    }
                }
            }

            score = (100 * (numOfQuintets collisions)) /(double) numOfQuintets;
        }

        if (this.isHardRule() && collisions > 0) {
            score = 0;
        }

        timeTableSolution.addScoreToRule(this, score);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getClass());
    }

    @Override
    public boolean equals(Object obj) {
        return this.getClass() == obj.getClass();
    }
}
