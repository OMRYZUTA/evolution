package il.ac.mta.zuli.evolution.engine.evolutionengine;

import il.ac.mta.zuli.evolution.engine.evolutionengine.crossover.CrossoverInterface;
import il.ac.mta.zuli.evolution.engine.evolutionengine.mutation.Mutation;
import il.ac.mta.zuli.evolution.engine.evolutionengine.selection.Selection;
import il.ac.mta.zuli.evolution.engine.exceptions.ValidationException;

import java.util.Collections;
import java.util.List;

public class EngineSettings<T extends Solution> {
    private int initialPopulationSize;
    private final Selection<T> selection;
    private final CrossoverInterface<T> crossover; //why isn't the interface name simply crossover?
    private final List<Mutation<T>> mutations;

    public EngineSettings(Selection<T> newSelection,
                          CrossoverInterface<T> newCrossover,
                          List<Mutation<T>> newMutationList, int initialPopulationSize) {
        this.initialPopulationSize = initialPopulationSize;
        selection = newSelection;
        crossover = newCrossover;
        mutations = newMutationList;
    }

    //#region setters
    private void setInitialPopulationSize(int size) {
        if (size > 0) {
            this.initialPopulationSize = size;
        } else {
            throw new ValidationException("Initial population size: " + size + ". Where's the fun in that?");
        }
    }
    //#endregion

    //#region getters
    public int getInitialPopulationSize() {
        return initialPopulationSize;
    }

    public Selection<T> getSelection() {
        return selection;
    }

    public CrossoverInterface<T> getCrossover() {
        return crossover;
    }

    public List<Mutation<T>> getMutations() {
        return Collections.unmodifiableList(mutations);
    }
    //#endregion


    @Override
    public String toString() {
        return "EngineSettings{" +
                "initialPopulationSize=" + initialPopulationSize +
                ", selection='" + selection + System.lineSeparator() +
                ", crossover=" + crossover + System.lineSeparator() +
                ", mutations=" + mutations +
                '}';
    }

    public int getEliteNumber() {
        return selection.getElitism();
    }
}
