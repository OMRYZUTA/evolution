package il.ac.mta.zuli.evolution.engine.evolutionengine.selection;

import il.ac.mta.zuli.evolution.engine.evolutionengine.Solution;

import java.util.List;

public interface Selection<T extends Solution> {

    List<T> select(List<T> solutions);

    String getConfiguration();

    int getElitism();
}
