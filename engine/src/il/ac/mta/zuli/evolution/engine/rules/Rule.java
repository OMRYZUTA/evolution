package il.ac.mta.zuli.evolution.engine.rules;

import il.ac.mta.zuli.evolution.engine.algorithm.Quintet;

import java.util.Collection;

public interface Rule {
    int appliesRule(Collection<Quintet> solution);
    //single solution - a bunch of quintets
}
