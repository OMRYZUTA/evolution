package il.ac.mta.zuli.evolution.dto;

import il.ac.mta.zuli.evolution.engine.Double;
import il.ac.mta.zuli.evolution.engine.evolutionengine.selection.Selection;

public class SelectionDTO {
    private final String type;
    private final String configuration;
    private final int elitism;

    public SelectionDTO(Selection<Double> selection) {
        this.type = selection.getClass().getSimpleName();
        this.configuration = selection.getConfiguration();
        this.elitism = selection.getElitism();
    }

    public String getType() {
        return type;
    }

    public String getConfiguration() {
        return configuration;
    }

    public int getElitism() {
        return elitism;
    }

    @Override
    public String toString() {
        return "Selection: " + type +
                ", elitism: " + elitism +
                ", configuration: " + configuration;
    }
}
