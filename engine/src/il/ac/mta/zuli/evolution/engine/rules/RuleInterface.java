package il.ac.mta.zuli.evolution.engine.rules;

import il.ac.mta.zuli.evolution.engine.Quintet;

import java.util.Collection;


public interface RuleInterface {
    int fitnessEvaluation(Collection<Quintet> solution);
}
