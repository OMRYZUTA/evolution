package il.ac.mta.zuli.evolution.engine.rules;

import il.ac.mta.zuli.evolution.engine.algorithm.Quintet;

import java.util.Collection;


public interface RuleInterface {
    int fitness(Collection<Quintet> solution);
}
