package il.ac.mta.zuli.evolution.engine.rules;

import il.ac.mta.zuli.evolution.engine.evolutionengine.Solution;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class DayOffTeacher extends Rule {
    public DayOffTeacher(@NotNull String ruleType) {
        super(ruleType);
    }

    @Override
    public void fitnessEvaluation(Solution solution) {

    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getClass());
    }

    @Override
    public boolean equals(Object obj) {
        return this.getClass() == obj.getClass();
    }
}
