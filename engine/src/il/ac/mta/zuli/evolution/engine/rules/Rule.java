package il.ac.mta.zuli.evolution.engine.rules;

import il.ac.mta.zuli.evolution.engine.exceptions.ValidationException;
import org.jetbrains.annotations.NotNull;

//TODO - ask omry why we need a ruleInterface?
public abstract class Rule implements RuleInterface {
    private final RuleType ruleType; //hard or soft

    protected Rule(@NotNull String ruleType) { // @NotNull return ?
        switch (ruleType.toLowerCase()) {
            case "soft":
                this.ruleType = RuleType.SOFT;
                break;
            case "hard":
                this.ruleType = RuleType.HARD;
                break;
            default:
                throw new ValidationException("invalid rule type"+ruleType);
        }
    }

    public RuleType getRuleType() {
        return ruleType;
    }

    public boolean isHardRule() {
        return this.ruleType == RuleType.HARD;
    }

    @Override
    public String toString() {
        return "Rule{RuleID: " + getClass().getSimpleName() +
                " ruleType=" + ruleType +
                '}';
    }
}
