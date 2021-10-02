package il.ac.mta.zuli.evolution.engine.rules;

import il.ac.mta.zuli.evolution.engine.exceptions.ValidationException;
import org.jetbrains.annotations.NotNull;

public abstract class Rule implements RuleInterface {
    private final RuleType ruleType; //hard or soft

    protected Rule(@NotNull String ruleType) {
        switch (ruleType.toLowerCase()) {
            case "soft":
                this.ruleType = RuleType.SOFT;
                break;
            case "hard":
                this.ruleType = RuleType.HARD;
                break;
            default:
                throw new ValidationException("Invalid rule type: " + ruleType);
        }
    }

    @Override
    public String getRuleName() {
        return getClass().getSimpleName();
    }

    public RuleType getRuleType() {
        return ruleType;
    }

    public boolean isHardRule() {
        return this.ruleType == RuleType.HARD;
    }

    public String getParams() {
        return "";
    }

    @Override
    public String toString() {
        return "Rule{RuleID: " + getClass().getSimpleName() +
                " ruleType=" + ruleType +
                '}';
    }
}
