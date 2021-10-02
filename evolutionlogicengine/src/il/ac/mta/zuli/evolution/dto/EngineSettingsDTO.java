package il.ac.mta.zuli.evolution.dto;

import il.ac.mta.zuli.evolution.engine.TimetableSolution;
import il.ac.mta.zuli.evolution.engine.evolutionengine.EngineSettings;
import il.ac.mta.zuli.evolution.engine.evolutionengine.mutation.Mutation;

import java.util.ArrayList;
import java.util.List;

public class EngineSettingsDTO {
    private final int populationSize;
    private final SelectionDTO selection;
    private final CrossoverDTO crossover;
    private final List<MutationDTO> mutations;

    public EngineSettingsDTO(int initialPopulationSize, SelectionDTO selection, CrossoverDTO crossover, List<MutationDTO> mutations) {
        this.populationSize = initialPopulationSize;
        this.selection = selection;
        this.crossover = crossover;
        this.mutations = mutations;
    }

    public EngineSettingsDTO(EngineSettings<TimetableSolution> engineSettings) {
        this.populationSize = engineSettings.getPopulationSize();
        this.selection = new SelectionDTO(engineSettings.getSelection());
        this.crossover = new CrossoverDTO(engineSettings.getCrossover());
        this.mutations = new ArrayList<>();

        List<Mutation<TimetableSolution>> mutations = engineSettings.getMutations();
        for (Mutation<TimetableSolution> mutation : mutations) {
            this.mutations.add(new MutationDTO(mutation));
        }
    }

    public int getPopulationSize() {
        return populationSize;
    }

    public SelectionDTO getSelection() {
        return selection;
    }

    public CrossoverDTO getCrossover() {
        return crossover;
    }

    public List<MutationDTO> getMutations() {
        return mutations;
    }

    @Override
    public String toString() {
        return "EngineSettingsDTO{" +
                "initialPopulationSize=" + populationSize +
                ", selection=" + selection +
                ", crossover=" + crossover +
                ", mutations=" + mutations +
                '}';
    }
}
