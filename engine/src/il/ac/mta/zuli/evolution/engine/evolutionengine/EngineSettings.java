package il.ac.mta.zuli.evolution.engine.evolutionengine;

import il.ac.mta.zuli.evolution.engine.evolutionengine.crossover.Crossover;
import il.ac.mta.zuli.evolution.engine.evolutionengine.crossover.DayTimeOriented;
import il.ac.mta.zuli.evolution.engine.evolutionengine.mutation.ComponentName;
import il.ac.mta.zuli.evolution.engine.evolutionengine.mutation.Flipping;
import il.ac.mta.zuli.evolution.engine.evolutionengine.mutation.Mutation;
import il.ac.mta.zuli.evolution.engine.evolutionengine.selection.Selection;
import il.ac.mta.zuli.evolution.engine.evolutionengine.selection.Truncation;
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

    public EngineSettings(@NotNull ETTEvolutionEngine ee) throws Exception {
        setInitialPopulationSize(ee.getETTInitialPopulation().getSize());
        setSelection(ee.getETTSelection());
        setCrossover(ee.getETTCrossover());
        setMutations(ee.getETTMutations().getETTMutation());
    }

    //TODO make sure all setters are private
    private void setInitialPopulationSize(int size) {
        //TODO validate size
        this.initialPopulationSize = size;
    }

    public int getInitialPopulationSize() {
        return initialPopulationSize;
    }

    public Selection<T> getSelection() {
        return selection;
    }

    private void setSelection(@NotNull ETTSelection ettSelection) throws Exception {
        if ((ettSelection.getType()).equalsIgnoreCase("truncation")) {
            this.selection = new Truncation(ettSelection);
        } else {
            throw new Exception("invalid selection type (for ex. 1)");
        }
        //TODO factory methods
        //TODO different kind of Exception Classes
    }

    private void setCrossover(@NotNull ETTCrossover ettCrossover) throws Exception {
        if ((ettCrossover.getName()).equalsIgnoreCase("daytimeoriented")) {
            this.crossover = new DayTimeOriented(ettCrossover.getCuttingPoints());
        } else {
            throw new Exception("invalid crossover type (for ex. 1)");
        }
        //TODO factory methods
    }

    private void setMutations(List<ETTMutation> ettMutations) throws Exception {
        mutations = new ArrayList<>();

        for (ETTMutation ettMutation : ettMutations) {
            if (ettMutation.getName().equalsIgnoreCase("flipping")) {
                int maxTupplesIndex = (ettMutation.getConfiguration()).indexOf("MaxTupples=") + "MaxTupples=".length();
                int commaIndex = ettMutation.getConfiguration().indexOf(",");
                String maxTupplesStr = (ettMutation.getConfiguration()).substring(maxTupplesIndex, commaIndex);
                int maxTupples = Integer.parseInt(maxTupplesStr);
                int componentIndex = (ettMutation.getConfiguration()).indexOf("Component=") + "Component=".length();
                String componentStr = (ettMutation.getConfiguration()).substring(componentIndex, componentIndex + 1);
                ComponentName component = ComponentName.valueOf(componentStr.toUpperCase());
                mutations.add(new Flipping(ettMutation.getProbability(), maxTupples, component));
            } else throw new Exception("invalid mutation name (for ex 1)");
        }
    }

    public Crossover getCrossover() {
        return crossover;
    }

    public List<Mutation> getMutations() {
        return Collections.unmodifiableList(mutations);
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
