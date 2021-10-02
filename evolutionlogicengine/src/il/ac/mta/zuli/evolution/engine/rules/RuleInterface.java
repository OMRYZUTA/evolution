package il.ac.mta.zuli.evolution.engine.rules;

import il.ac.mta.zuli.evolution.engine.evolutionengine.Solution;

public interface RuleInterface {
    void fitnessEvaluation(Solution solution);

    String getRuleName();
}
