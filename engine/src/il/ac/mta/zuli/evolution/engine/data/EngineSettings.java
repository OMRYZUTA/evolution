package il.ac.mta.zuli.evolution.engine.data;

import il.ac.mta.zuli.evolution.engine.data.generated.ETTEvolutionEngine;
import il.ac.mta.zuli.evolution.engine.data.generated.ETTSelection;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class EngineSettings {
    private int initialPopulationSize;
    private Selection selection;
    private Crossover crossover;
    private List<Mutation> mutations;
    //Configuration

    public EngineSettings(@NotNull ETTEvolutionEngine ee) {
        setInitialPopulationSize(ee.getETTInitialPopulation().getSize());
        setSelection(ee.getETTSelection());
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

    private void setSelection(@NotNull ETTSelection ettSelection) {
        this.selection = new Selection(ettSelection);
    }

    public Crossover getCrossover() {
        return crossover;
    }

    private void setCrossover(Crossover crossover) {
        this.crossover = crossover;
    }

    public List<Mutation> getMutations() {
        return Collections.unmodifiableList(mutations);
    }

    private void setMutations(List<Mutation> mutations) {
        this.mutations = mutations;
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
