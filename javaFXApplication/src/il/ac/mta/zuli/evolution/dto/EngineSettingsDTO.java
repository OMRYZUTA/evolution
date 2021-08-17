package il.ac.mta.zuli.evolution.dto;

import java.util.List;

public class EngineSettingsDTO {
    private final int initialPopulationSize;
    private final SelectionDTO selection;
    private final CrossoverDTO crossover;
    private final List<MutationDTO> mutations;

    public EngineSettingsDTO(int initialPopulationSize, SelectionDTO selection, CrossoverDTO crossover, List<MutationDTO> mutations) {
        this.initialPopulationSize = initialPopulationSize;
        this.selection = selection;
        this.crossover = crossover;
        this.mutations = mutations;
    }

    public int getInitialPopulationSize() {
        return initialPopulationSize;
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
                "initialPopulationSize=" + initialPopulationSize +
                ", selection=" + selection +
                ", crossover=" + crossover +
                ", mutations=" + mutations +
                '}';
    }
}
