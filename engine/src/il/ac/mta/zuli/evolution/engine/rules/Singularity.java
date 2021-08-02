package il.ac.mta.zuli.evolution.engine.rules;

import il.ac.mta.zuli.evolution.engine.Quintet;
import il.ac.mta.zuli.evolution.engine.evolutionengine.Solution;

import java.util.HashSet;

public class Singularity extends Rule {
    public Singularity(String ruleType) {
        super(ruleType);
    }

    @Override
    public void fitnessEvaluation(Solution solution) {
        int collisions = 0;
        int numOfQuintets = solution.getSolutionSize();
        int classID;
        String DHC;
        HashSet<String> tempSet = new HashSet<>();

        for (Quintet quintet : solution.getSolution()) {
            classID = (quintet.getSchoolClass()).getId();
            DHC = String.format("%s_%d_%d", quintet.getDay(), quintet.getHour(), classID);
            //if the set already contains the element, the call leaves the set unchanged and returns false.
            if (!tempSet.add(DHC)) {
                if (isHardRule()) {
                    return HARDRULEFAILURE;
                } else {
                    collisions++;
                }
            }
        }

        return (100 * (numOfQuintets - collisions)) / numOfQuintets;
    }
}