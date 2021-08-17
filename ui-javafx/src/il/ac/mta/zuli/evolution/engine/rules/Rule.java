package il.ac.mta.zuli.evolution.engine.rules;

import il.ac.mta.zuli.evolution.engine.exceptions.ValidationException;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Rule)) return false;
        Rule rule = (Rule) o;
        return getRuleType() == rule.getRuleType();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getRuleType());
    }
}
