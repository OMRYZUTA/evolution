package il.ac.mta.zuli.evolution.engine.rules;

import il.ac.mta.zuli.evolution.engine.Quintet;
import il.ac.mta.zuli.evolution.engine.evolutionengine.Solution;

import java.util.HashSet;

public class TeacherIsHuman extends Rule {
    public TeacherIsHuman(String ruleType) {
        super(ruleType);
    }

    //returns score 0-100
    @Override
    public int fitnessEvaluation(Solution solution) {
        int collision = 0;
        HashSet<String> tempSet = new HashSet<>();

        for (Quintet quintet : solution) {
            String DHT = String.format("%s_%d_%d", quintet.getDay(), quintet.getHour(), (quintet.getTeacher().getId()));
            System.out.println("quintet day,hour,teacherID: " + DHT); //delete later
            //add(): if this set already contains the element, the call leaves the set unchanged and returns false.
            if (!tempSet.add(DHT)) {
                collision++;
            }
        }

        return collision; //TODO figure out score
    }
}
