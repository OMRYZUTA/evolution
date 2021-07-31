package il.ac.mta.zuli.evolution.engine.rules;

import il.ac.mta.zuli.evolution.engine.TimeTableSolution;

public class Singularity extends Rule {
    public Singularity(String ruleType) {
        super(ruleType);
    }

    @Override
    public int fitnessEvaluation(TimeTableSolution solution) {

        return 0;
    }
}