package il.ac.mta.zuli.evolution.engine.evolutionengine.selection;

import il.ac.mta.zuli.evolution.engine.evolutionengine.Solution;

import java.util.List;

//TODO deceide what collection
public interface Selection<T extends Solution> {

    List<T> select(List<T> solutions);

    String getConfiguration();
}
