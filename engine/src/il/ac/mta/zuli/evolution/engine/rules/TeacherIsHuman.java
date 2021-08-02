package il.ac.mta.zuli.evolution.engine.rules;

import il.ac.mta.zuli.evolution.engine.Quintet;
import il.ac.mta.zuli.evolution.engine.TimeTableSolution;
import il.ac.mta.zuli.evolution.engine.evolutionengine.Solution;

import java.util.HashSet;

public class TeacherIsHuman extends Rule {
    public TeacherIsHuman(String ruleType) {
        super(ruleType);
    }

    //returns score 0-100
    @Override
    public void fitnessEvaluation(Solution solution) {
        if (!(solution instanceof TimeTableSolution)) {
            throw new RuntimeException("solution must be time table solution");
        }
        TimeTableSolution timeTableSolution = (TimeTableSolution) solution;
        int collisions = 0;
        int numOfQuintets = timeTableSolution.getSolutionSize();
        int teacherID;
        String DHT;
        HashSet<String> tempSet = new HashSet<>();
        int score=-1;
        for (Quintet quintet : timeTableSolution.getSolution()) {
            teacherID = (quintet.getTeacher()).getId();
            DHT = String.format("%s_%d_%d", quintet.getDay(), quintet.getHour(), teacherID);
            //if the set already contains the element, the call leaves the set unchanged and returns false.
            if (!tempSet.add(DHT)) {
                if (isHardRule()) {
                    score= HARDRULEFAILURE;
                    break;
                } else {
                    collisions++;
                }
            }
        }

        if(score !=HARDRULEFAILURE){
            score =(100 * (numOfQuintets - collisions)) / numOfQuintets;
        }

        timeTableSolution.addScoreToRule(this.getClass().getSimpleName(),score);
    }
}
