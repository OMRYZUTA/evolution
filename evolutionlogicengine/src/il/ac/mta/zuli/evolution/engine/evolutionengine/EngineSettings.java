package il.ac.mta.zuli.evolution.engine.evolutionengine;

import il.ac.mta.zuli.evolution.Constants;
import il.ac.mta.zuli.evolution.engine.evolutionengine.crossover.CrossoverFactory;
import il.ac.mta.zuli.evolution.engine.evolutionengine.crossover.CrossoverInterface;
import il.ac.mta.zuli.evolution.engine.evolutionengine.mutation.Mutation;
import il.ac.mta.zuli.evolution.engine.evolutionengine.mutation.MutationFactory;
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

    public EngineSettings(Map<String, Object> engineSettingsMap, TimeTable timetable) {
        setInitialPopulationSize(engineSettingsMap.get(Constants.POPULATION_SIZE));

        Map<String, Object> selectionMap = (Map<String, Object>) engineSettingsMap.get(Constants.SELECTION);
        Map<String, Object> crossoverMap = (Map<String, Object>) engineSettingsMap.get(Constants.CROSSOVER);
        List<Map<String, Object>> mutationsMap = (List<Map<String, Object>>) engineSettingsMap.get(Constants.MUTATIONS);

        this.selection = SelectionFactory.createSelectionFromMap(selectionMap, initialPopulationSize);
        this.crossover = CrossoverFactory.createCrossoverFromMap(crossoverMap, timetable);
        this.mutations = generateMutationList(mutationsMap, timetable);
    }

    private List<Mutation<T>> generateMutationList(List<Map<String, Object>> mutationsMap, TimeTable timetable) {
        List<Mutation<T>> mutationList = new ArrayList<>();
        //TODO - is it required to have at least 1 mutation?
        if (mutationsMap.size() > 0) {
            for (Map<String, Object> mutationMap : mutationsMap) {
                MutationFactory.createMutationFromMap(mutationMap, timetable);
            }
        }

        return null;
    }

    private void setInitialPopulationSize(Object objectSize) {
        int size;

        try {
            size = (int) objectSize;
        } catch (Throwable e) {
            throw new ValidationException("Population size must be a positive number");
        }

        if (size > 0) {
            this.initialPopulationSize = size;
        } else {
            throw new ValidationException("Population size: " + size + ". Where's the fun in that?");
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

    public int getNumOfElite() {
        return selection.getElitism();
    }

    @Override
    public String toString() {
        return "EngineSettings{" +
                "initialPopulationSize=" + initialPopulationSize +
                ", selection='" + selection + System.lineSeparator() +
                ", crossover=" + crossover + System.lineSeparator() +
                ", mutations=" + mutations +
                '}';
    }
}
