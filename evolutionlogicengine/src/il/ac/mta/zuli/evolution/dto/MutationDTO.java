package il.ac.mta.zuli.evolution.dto;

import il.ac.mta.zuli.evolution.engine.TimetableSolution;
import il.ac.mta.zuli.evolution.engine.evolutionengine.mutation.Mutation;

public class MutationDTO {
    private final String name;
    private final double probability;
    private String configuration;

    public MutationDTO(String name, double probability, String configuration) {
        this.name = name;
        this.probability = probability;
        this.configuration = configuration;
    }

    public MutationDTO(Mutation<TimetableSolution> mutation) {
        this.name = mutation.getClass().getSimpleName(); //TODO - needs to be exactly as in Constants
        this.probability = mutation.getProbability();
// maxTuples;
// totalTuples;
// orientation;
    }

    public String getName() {
        return name;
    }

    public double getProbability() {
        return probability;
    }

    public String getConfiguration() {
        return configuration;
    }

    @Override
    public String toString() {
        return "Mutation: " + name +
                ", probability " + probability +
                ", configuration: " + configuration;
    }
}
