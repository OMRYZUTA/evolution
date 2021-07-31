package il.ac.mta.zuli.evolution.engine.rules;

import il.ac.mta.zuli.evolution.engine.TimeTableSolution;

public class Satisfactory extends Rule {
    public Satisfactory(String ruleType) {
        super(ruleType);
    }

    @Override
    public int fitnessEvaluation(TimeTableSolution solution) {
        return 0;
    }
}
