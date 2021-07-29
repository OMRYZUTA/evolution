package il.ac.mta.zuli.evolution.engine.rules;

import il.ac.mta.zuli.evolution.engine.Quintet;

import java.util.Collection;

public class Satisfactory extends Rule {
    public Satisfactory(String ruleType) {
        super(ruleType);
    }

    @Override
    public int fitness(Collection<Quintet> solution) {
        return 0;
    }
}
