package il.ac.mta.zuli.evolution.engine.rules;

import il.ac.mta.zuli.evolution.engine.evolutionengine.Solution;

public class Satisfactory extends Rule {
    public Satisfactory(String ruleType) {
        super(ruleType);
    }

    @Override
    public int fitnessEvaluation(Solution solution) {
        return 0;
    }
}
