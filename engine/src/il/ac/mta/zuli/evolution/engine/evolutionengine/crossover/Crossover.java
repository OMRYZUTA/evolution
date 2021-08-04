package il.ac.mta.zuli.evolution.engine.evolutionengine.crossover;

import il.ac.mta.zuli.evolution.engine.evolutionengine.Solution;

import java.util.List;


public interface Crossover<T extends Solution> {
    int getNumOfCuttingPoints();

    List<T> crossover(List<T> generation);

    List<T> crossoverBetween2Parents(T s1, T s2);
}
