package il.ac.mta.zuli.evolution.engine.evolutionengine.crossover;

import il.ac.mta.zuli.evolution.engine.evolutionengine.Solution;

import java.util.List;


public interface CrossoverInterface<T extends Solution> {
    int getNumOfCuttingPoints();

    List<T> crossover(List<T> generation);

    String getCrossoverType();
}
