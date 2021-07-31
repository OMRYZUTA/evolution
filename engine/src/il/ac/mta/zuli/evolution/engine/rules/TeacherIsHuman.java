package il.ac.mta.zuli.evolution.engine.rules;

import il.ac.mta.zuli.evolution.engine.Quintet;
import il.ac.mta.zuli.evolution.engine.TimeTableSolution;

import java.util.HashSet;

public class TeacherIsHuman extends Rule {
    public TeacherIsHuman(String ruleType) {
        super(ruleType);
    }

    //returns score 0-100
    @Override
    public int fitnessEvaluation(TimeTableSolution solution) {
        int collisions = 0;
        int numOfQuintets = solution.getSolutionSize();
        int teacherID;
        String DHT;
        HashSet<String> tempSet = new HashSet<>();

        for (Quintet quintet : solution.getSolution()) {
            teacherID = (quintet.getTeacher()).getId();
            DHT = String.format("%s_%d_%d", quintet.getDay(), quintet.getHour(), teacherID);
            //if the set already contains the element, the call leaves the set unchanged and returns false.
            if (!tempSet.add(DHT)) {
                if (isHardRule()) {
                    return 0;
                } else {
                    collisions++;
                }
            }
        }

        return (100 * (numOfQuintets - collisions)) / numOfQuintets;
    }
}
