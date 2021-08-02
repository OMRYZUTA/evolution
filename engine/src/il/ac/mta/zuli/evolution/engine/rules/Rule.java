package il.ac.mta.zuli.evolution.engine.rules;

//TODO - ask omry why we need a ruleInterface?
public abstract class Rule implements RuleInterface {
    private final RuleType ruleType; //hard or soft
    static final int HARDRULEFAILURE = 0;

    protected Rule(String ruleType) {
        switch (ruleType.toLowerCase()) {
            case "soft":
                this.ruleType = RuleType.SOFT;
                break;
            case "hard":
                this.ruleType = RuleType.HARD;
                break;
            default:
                throw new RuntimeException("invalid rule type");
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
