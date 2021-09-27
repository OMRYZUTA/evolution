package il.ac.mta.zuli.evolution.engine.evolutionengine;

import il.ac.mta.zuli.evolution.Constants;
import il.ac.mta.zuli.evolution.engine.evolutionengine.crossover.CrossoverFactory;
import il.ac.mta.zuli.evolution.engine.evolutionengine.crossover.CrossoverInterface;
import il.ac.mta.zuli.evolution.engine.evolutionengine.mutation.Mutation;
import il.ac.mta.zuli.evolution.engine.evolutionengine.selection.Selection;
import il.ac.mta.zuli.evolution.engine.evolutionengine.selection.SelectionFactory;
import il.ac.mta.zuli.evolution.engine.exceptions.ValidationException;
import il.ac.mta.zuli.evolution.engine.timetable.TimeTable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class EngineSettings<T extends Solution> {
    private int initialPopulationSize;
    private final Selection<T> selection;
    private final CrossoverInterface<T> crossover; //why isn't the interface name simply crossover?
    private final List<Mutation<T>> mutations;

    public EngineSettings(Map<String, Object> engineSettingsMap, TimeTable timeTable) {
        setInitialPopulationSize((int) engineSettingsMap.get(Constants.POPULATION_SIZE));

        Map<String, Object> selectionMap = (Map<String, Object>) engineSettingsMap.get(Constants.SELECTION);
        Map<String, Object> crossoverMap = (Map<String, Object>) engineSettingsMap.get(Constants.CROSSOVER);
        Map<String, Object> mutationMap = (Map<String, Object>) engineSettingsMap.get(Constants.MUTATIONS);

        selection = SelectionFactory.createSelectionFromMap(selectionMap, initialPopulationSize);
        crossover = CrossoverFactory.createCrossoverFromMap(crossoverMap, timeTable);
        mutations = generateMutationList(mutationMap, timeTable);

    }

    private List<Mutation<T>> generateMutationList(Map<String, Object> mutationMap, TimeTable timeTable) {
        List<Mutation<T>> mutationList = new ArrayList<>();
        //        MutationFactory.createCrossoverFromMap(mutationMap, timetable);
        //TODO implement: for each "name" in map we need to add another Mutation
        //same for predicates
        return null;
    }

    private void setInitialPopulationSize(int size) {
        if (size > 0) {
            this.initialPopulationSize = size;
        } else {
            throw new ValidationException("Initial population size: " + size + ". Where's the fun in that?");
        }
    }

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

    public int getNumOfElite() {
        return selection.getElitism();
    }
}
