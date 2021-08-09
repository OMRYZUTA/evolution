package il.ac.mta.zuli.evolution.engine.evolutionengine;

import org.jetbrains.annotations.NotNull;

public interface Solution extends Comparable<Solution> {

    void calculateTotalScore();

    double getTotalFitnessScore();

    int compareTo(@NotNull Solution other);
}
