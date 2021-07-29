package il.ac.mta.zuli.evolution.engine.rules;

import il.ac.mta.zuli.evolution.engine.algorithm.Quintet;

import java.util.Collection;

public abstract class Rule {
    private final RuleType ruleType; //hard or soft

    protected boolean isHardRule() {
        return this.ruleType == RuleType.HARD;
    }

    protected Rule(String ruleType) {
        if (ruleType == "soft") {
            this.ruleType = RuleType.SOFT;
        } else {
            this.ruleType = RuleType.HARD;
        }
    }

    abstract public int fitness(Collection<Quintet> solution);

    public RuleType getRuleType() {
        return ruleType;
    }

    @Override
    public String toString() {
        return "Rule{RuleID: " + getClass().getSimpleName() +
                " ruleType=" + ruleType +
                '}';
    }
}
