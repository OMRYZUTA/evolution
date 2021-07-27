package il.ac.mta.zuli.evolution.engine.algorithm;

import java.util.Collection;

public interface Operator {
    boolean executeOperator(Collection<Quintet> solution);
}
