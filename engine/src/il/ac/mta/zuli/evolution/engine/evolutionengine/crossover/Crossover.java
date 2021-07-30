package il.ac.mta.zuli.evolution.engine.evolutionengine.crossover;

import il.ac.mta.zuli.evolution.engine.evolutionengine.Solution;

import java.util.List;

public interface Crossover {
    List<Solution> crossover(List<Solution> parentSolutions);
}
