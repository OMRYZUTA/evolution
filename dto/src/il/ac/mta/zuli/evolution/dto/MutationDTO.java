package il.ac.mta.zuli.evolution.dto;

public class MutationDTO {
    String name;
    double probability;
    String configuration;

    public MutationDTO(String name, double probability, String configuration) {
        this.name = name;
        this.probability = probability;
        this.configuration = configuration;
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
