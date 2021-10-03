package il.ac.mta.zuli.evolution.dto;

import il.ac.mta.zuli.evolution.Constants;
import il.ac.mta.zuli.evolution.engine.TimetableSolution;
import il.ac.mta.zuli.evolution.engine.evolutionengine.selection.Selection;
import il.ac.mta.zuli.evolution.engine.evolutionengine.selection.Tournament;
import il.ac.mta.zuli.evolution.engine.evolutionengine.selection.Truncation;

public class SelectionDTO {
    private final String type;
    private final Integer elitism;
    private Integer topPercent = null;

    public SelectionDTO(Selection<TimetableSolution> selection) {
        this.type = selection.getSelectionType();
        this.elitism = selection.getElitism();

        if (type.equals(Constants.TRUNCATION)) {
            Truncation truncation = (Truncation) selection;
            topPercent = truncation.getTopPercent();
        } else if (type.equals(Constants.TOURNAMENT)) {
            Tournament<TimetableSolution> tournament = (Tournament) selection;
        }
    }

    public String getType() {
        return type;
    }


    public Integer getElitism() {
        return elitism;
    }

    public Integer getTopPercent() {
        return topPercent;
    }

    @Override
    public String toString() {
        return "Selection: " + type +
                ", elitism: " + elitism;
    }
}
