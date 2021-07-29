package il.ac.mta.zuli.evolution.engine.evolutionengine;

import il.ac.mta.zuli.evolution.engine.Quintet;

import java.util.Collection;

public interface Operator {
    boolean executeOperator(Collection<Quintet> solution);
}
