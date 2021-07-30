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

public class EngineSettings {
    private int initialPopulationSize;
    private Selection selection;
    private Crossover crossover;
    private List<Mutation> mutations;

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

    public Selection getSelection() {
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

    public Crossover getCrossover() {
        return crossover;
    }

    private void setCrossover(@NotNull ETTCrossover ettCrossover) throws Exception {
        if ((ettCrossover.getName()).equalsIgnoreCase("daytimeoriented")) {
            this.crossover = new DayTimeOriented(ettCrossover.getCuttingPoints());
        } else {
            throw new Exception("invalid crossover type (for ex. 1)");
        }
        //TODO factory methods
    }

    public List<Mutation> getMutations() {
        return Collections.unmodifiableList(mutations);
    }

    private void setMutations(List<ETTMutation> ettMutations) throws Exception {
        mutations = new ArrayList<>();

        for (ETTMutation ettMutation : ettMutations) {
            if (ettMutation.getName().equalsIgnoreCase("flipping")) {
                int maxTupplesIndex = (ettMutation.getConfiguration()).indexOf("MaxTupples=");
                int maxTupples = Integer.parseInt((ettMutation.getConfiguration()).substring(maxTupplesIndex + "MaxTupples=".length() + 1));
                int componentIndex = (ettMutation.getConfiguration()).indexOf("Component=");
                String componentStr = (ettMutation.getConfiguration()).substring(componentIndex + "Component=".length() + 1);
                ComponentName component = ComponentName.valueOf(componentStr.toUpperCase());
                mutations.add(new Flipping(ettMutation.getProbability(), maxTupples, component));
            } else throw new Exception("invalid mutation name (for ex 1)");
        }
    }

    @Override
    public String toString() {
        return "EngineSettings{" +
                "initialPopulationSize=" + initialPopulationSize +
                ", selection='" + selection + '\'' +
                ", crossover=" + crossover +
                ", mutations=" + mutations +
                '}';
    }
}
