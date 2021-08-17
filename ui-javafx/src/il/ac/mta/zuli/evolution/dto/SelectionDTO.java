package il.ac.mta.zuli.evolution.dto;

public class SelectionDTO {
    String type;
    String configuration;

    public SelectionDTO(String type, String configuration) {
        this.type = type;
        this.configuration = configuration;
    }

    public String getType() {
        return type;
    }

    public String getConfiguration() {
        return configuration;
    }

    @Override
    public String toString() {
        return "Selection: " + type +
                ", configuration: " + configuration;
    }
}