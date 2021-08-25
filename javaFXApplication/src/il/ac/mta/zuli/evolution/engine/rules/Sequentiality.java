package il.ac.mta.zuli.evolution.engine.rules;

import il.ac.mta.zuli.evolution.engine.evolutionengine.Solution;
import il.ac.mta.zuli.evolution.engine.exceptions.ValidationException;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class Sequentiality extends Rule {
    private int totalHours;

    public Sequentiality(@NotNull String ruleType, int totalHours) {
        super(ruleType);
        setTotalHours(totalHours);
    }

    @Override
    public void fitnessEvaluation(Solution solution) {
        //TODO implement
        System.out.println("in dayOffTeacher fitnessevaluation");
    }

    private void setTotalHours(int totalHours) {
        if (totalHours > 0) {
            this.totalHours = totalHours;
        } else {
            throw new ValidationException("Total hours of Sequential Rule must be a positive integer, but received: " + totalHours);
        }

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
