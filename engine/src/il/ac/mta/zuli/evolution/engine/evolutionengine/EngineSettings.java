package il.ac.mta.zuli.evolution.engine.evolutionengine;

import il.ac.mta.zuli.evolution.engine.evolutionengine.crossover.Crossover;
import il.ac.mta.zuli.evolution.engine.evolutionengine.crossover.CrossoverFactory;
import il.ac.mta.zuli.evolution.engine.evolutionengine.mutation.ComponentName;
import il.ac.mta.zuli.evolution.engine.evolutionengine.mutation.Flipping;
import il.ac.mta.zuli.evolution.engine.evolutionengine.mutation.Mutation;
import il.ac.mta.zuli.evolution.engine.evolutionengine.selection.Selection;
import il.ac.mta.zuli.evolution.engine.evolutionengine.selection.SelectionFactory;
import il.ac.mta.zuli.evolution.engine.exceptions.ValidationException;
import il.ac.mta.zuli.evolution.engine.timetable.TimeTable;
import il.ac.mta.zuli.evolution.engine.xmlparser.generated.ETTCrossover;
import il.ac.mta.zuli.evolution.engine.xmlparser.generated.ETTEvolutionEngine;
import il.ac.mta.zuli.evolution.engine.xmlparser.generated.ETTMutation;
import il.ac.mta.zuli.evolution.engine.xmlparser.generated.ETTSelection;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EngineSettings<T extends Solution> {
    private int initialPopulationSize;
    private Selection<T> selection;
    private Crossover<T> crossover;
    private List<Mutation<T>> mutations;

    public EngineSettings(@NotNull ETTEvolutionEngine ee, TimeTable timeTable)  {
        setInitialPopulationSize(ee.getETTInitialPopulation().getSize());
        setSelection(ee.getETTSelection());
        setCrossover(ee.getETTCrossover(), timeTable);
        setMutations(ee.getETTMutations().getETTMutation(), timeTable);
    }

    //#region setters
    private void setInitialPopulationSize(int size) {
        //TODO validate size
        this.initialPopulationSize = size;
    }

    private void setSelection(@NotNull ETTSelection ettSelection)   {

            this.selection = SelectionFactory.createSelection(ettSelection);

        //TODO factory methods
        //TODO different kind of Exception Classes
    }

    private void setCrossover(@NotNull ETTCrossover ettCrossover, TimeTable timeTable)  {
        this.crossover = CrossoverFactory.createCrossover( ettCrossover,  timeTable);
    }

    private void setMutations(List<ETTMutation> ettMutations, TimeTable timeTable)  {
        mutations = new ArrayList<>();

        for (ETTMutation ettMutation : ettMutations) {
            if (ettMutation.getName().equalsIgnoreCase("flipping")) {
                int maxTuples = extractMaxTuplesFromString(ettMutation);
                if (maxTuples < 0) {
                    throw new RuntimeException("max tuples must be >=0");
                }
                ComponentName component = extractComponentNameFromString(ettMutation);
                mutations.add(new Flipping(ettMutation.getProbability(), maxTuples, component, timeTable));
            } else {
                throw new ValidationException("invalid mutation name (for ex 1)");
            }
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

    public Crossover<T> getCrossover() {
        return crossover;
    }

    public List<Mutation<T>> getMutations() {
        return Collections.unmodifiableList(mutations);
    }
    //#endregion


    @NotNull
    private ComponentName extractComponentNameFromString(ETTMutation ettMutation) {
        int componentIndex = (ettMutation.getConfiguration()).indexOf("Component=") + "Component=".length();
        String componentStr = (ettMutation.getConfiguration()).substring(componentIndex, componentIndex + 1);
        return ComponentName.valueOf(componentStr.toUpperCase());
    }

    private int extractMaxTuplesFromString(ETTMutation ettMutation) {
        //this is how it's spelled in the xml: MaxTupples=3
        int maxTuplesIndex = (ettMutation.getConfiguration()).indexOf("MaxTupples=") + "MaxTupples=".length();
        int commaIndex = ettMutation.getConfiguration().indexOf(",");
        String maxTuplesStr = (ettMutation.getConfiguration()).substring(maxTuplesIndex, commaIndex);

        return Integer.parseInt(maxTuplesStr);
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
