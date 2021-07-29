package il.ac.mta.zuli.evolution.engine.rules;

public abstract class Rule implements RuleInterface {
    private final RuleType ruleType; //hard or soft

    protected Rule(String ruleType) {
        if (ruleType == "soft") {
            this.ruleType = RuleType.SOFT;
        } else {
            this.ruleType = RuleType.HARD;
        }
    }

    public RuleType getRuleType() {
        return ruleType;
    }

    protected boolean isHardRule() {
        return this.ruleType == RuleType.HARD;
    }

    @Override
    public String toString() {
        return "Rule{RuleID: " + getClass().getSimpleName() +
                " ruleType=" + ruleType +
                '}';
    }
}
