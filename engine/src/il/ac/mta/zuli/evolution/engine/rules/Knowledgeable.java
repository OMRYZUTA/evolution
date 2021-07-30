package il.ac.mta.zuli.evolution.engine.rules;

import il.ac.mta.zuli.evolution.engine.Quintet;

import java.util.Collection;

public class Knowledgeable extends Rule {
    public Knowledgeable(String ruleType) {
        super(ruleType);
    }

    @Override
    public int fitnessEvaluation(Collection<Quintet> solution) {
        return 0;
    }
}
