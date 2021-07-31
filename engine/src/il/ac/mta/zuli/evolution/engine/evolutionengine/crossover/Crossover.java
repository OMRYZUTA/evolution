package il.ac.mta.zuli.evolution.engine.evolutionengine.crossover;

import il.ac.mta.zuli.evolution.engine.evolutionengine.Solution;

import java.util.List;

public interface Crossover<T extends Solution> {
    List<T> crossover(T s1, T s2);
}
