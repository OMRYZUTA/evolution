package il.ac.mta.zuli.evolution.engine.evolutionengine.selection;

import il.ac.mta.zuli.evolution.engine.evolutionengine.Solution;

import java.util.Collection;

//TODO deceide what collection
public interface Selection {

    Collection<Solution> select(Collection<Solution> solutions);
}
