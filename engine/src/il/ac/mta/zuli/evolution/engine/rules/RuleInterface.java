package il.ac.mta.zuli.evolution.engine.rules;

import il.ac.mta.zuli.evolution.engine.TimeTableSolution;


public interface RuleInterface {
    int fitnessEvaluation(TimeTableSolution solution);
}
