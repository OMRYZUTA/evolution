package il.ac.mta.zuli.evolution.dto;

public class MutationDTO {
    String name;
    String probability;
    String configuration;
    public MutationDTO(String name, String probability,String configuration){
        this.name = name;
        this.probability = probability;
        this.configuration =configuration;
    }

    public String getProbability() {
        return probability;
    }

    public String getConfiguration() {
        return configuration;
    }

    @Override
    public String toString() {
        return "MutationDTO{" +
                "probability='" + probability + '\'' +
                ", configuration='" + configuration + '\'' +
                '}';
    }
}
