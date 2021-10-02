package il.ac.mta.zuli.evolution.engine.evolutionengine.mutation;

import il.ac.mta.zuli.evolution.engine.evolutionengine.Solution;

public interface Mutation<T extends Solution> {
    T mutate(T solution);

    String getMutationType();

    double getProbability();

    String getConfiguration();
}
