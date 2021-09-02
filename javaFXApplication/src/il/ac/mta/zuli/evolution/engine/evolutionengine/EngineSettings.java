package il.ac.mta.zuli.evolution.engine.evolutionengine;

import il.ac.mta.zuli.evolution.engine.evolutionengine.crossover.Crossover;
import il.ac.mta.zuli.evolution.engine.evolutionengine.crossover.CrossoverFactory;
import il.ac.mta.zuli.evolution.engine.evolutionengine.crossover.CrossoverInterface;
import il.ac.mta.zuli.evolution.engine.evolutionengine.mutation.Mutation;
import il.ac.mta.zuli.evolution.engine.evolutionengine.mutation.MutationFactory;
import il.ac.mta.zuli.evolution.engine.evolutionengine.selection.Selection;
import il.ac.mta.zuli.evolution.engine.evolutionengine.selection.SelectionFactory;
import il.ac.mta.zuli.evolution.engine.exceptions.ValidationException;
import il.ac.mta.zuli.evolution.engine.timetable.TimeTable;
import il.ac.mta.zuli.evolution.engine.xmlparser.generated.ex2.ETTCrossover;
import il.ac.mta.zuli.evolution.engine.xmlparser.generated.ex2.ETTEvolutionEngine;
import il.ac.mta.zuli.evolution.engine.xmlparser.generated.ex2.ETTMutation;
import il.ac.mta.zuli.evolution.engine.xmlparser.generated.ex2.ETTSelection;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EngineSettings<T extends Solution> {
    private int initialPopulationSize;
    private Selection<T> selection;
    private CrossoverInterface<T> crossover;
    private List<Mutation<T>> mutations;


    public EngineSettings(@NotNull ETTEvolutionEngine ee, @NotNull TimeTable timeTable) {
        setInitialPopulationSize(ee.getETTInitialPopulation().getSize());
        setSelection(ee.getETTSelection(), initialPopulationSize);
        setCrossover(ee.getETTCrossover(), timeTable);
        setMutations(ee.getETTMutations().getETTMutation(), timeTable);
    }

    //TODO - use when updating settings after pause (or using public setters)
    public EngineSettings(@NotNull EngineSettings prevSettings,
                          @NotNull TimeTable timeTable,
                          Selection newSelection,
                          Crossover newCrossover,
                          List<Mutation<T>> newMutationList) {
        initialPopulationSize = prevSettings.getInitialPopulationSize();
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

    private void setSelection(@NotNull ETTSelection ettSelection, int populationSize) {
        this.selection = SelectionFactory.createSelectionFromETT(ettSelection, populationSize);
    }

    private void setCrossover(@NotNull ETTCrossover ettCrossover, TimeTable timeTable) {
        this.crossover = CrossoverFactory.createCrossover(ettCrossover, timeTable);
    }

    private void setMutations(List<ETTMutation> ettMutations, TimeTable timeTable) {
        mutations = new ArrayList<>();

        for (ETTMutation ettMutation : ettMutations) {
            mutations.add(MutationFactory.createMutation(ettMutation, timeTable));
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
