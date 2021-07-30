package il.ac.mta.zuli.evolution.engine.evolutionengine.selection;

import il.ac.mta.zuli.evolution.engine.evolutionengine.Solution;

import java.util.Collection;

public interface Selection {
    Collection<Solution> selection(Collection<Solution>);
}
