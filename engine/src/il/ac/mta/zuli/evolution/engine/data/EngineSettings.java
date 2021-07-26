package il.ac.mta.zuli.evolution.engine.data;

import il.ac.mta.zuli.evolution.engine.data.generated.ETTEvolutionEngine;

import java.util.List;

public class EngineSettings {
    private final int initialPopulationSize;
    private String selection;
    private Crossover crossover;
    private List<Mutation> mutations;

    public EngineSettings(ETTEvolutionEngine ee) {
        this.initialPopulationSize = ee.getETTInitialPopulation().getSize();
    }
}
