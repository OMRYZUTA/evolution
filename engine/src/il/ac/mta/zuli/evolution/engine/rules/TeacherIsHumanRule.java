package il.ac.mta.zuli.evolution.engine.rules;

import il.ac.mta.zuli.evolution.engine.algorithm.Quintet;
import il.ac.mta.zuli.evolution.engine.data.RuleType;

import java.util.Collection;
import java.util.List;

public class TeacherIsHumanRule implements Rule {
    private RuleType ruleType; //hard or soft
    private String ruleID;
    private List<String> configuration;

    @Override
    public int appliesRule(Collection<Quintet> solution) {
        return 0;
    }

    //appliesRule
}
