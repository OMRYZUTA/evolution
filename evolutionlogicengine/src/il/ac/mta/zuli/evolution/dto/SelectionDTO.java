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
    private Double pte = null;

    public SelectionDTO(Selection<TimetableSolution> selection) {
        this.type = selection.getSelectionType();
        this.elitism = selection.getElitism();

        if (type.equals(Constants.TRUNCATION)) {
            Truncation<TimetableSolution> truncation = (Truncation<TimetableSolution>) selection;
            topPercent = truncation.getTopPercent();
        } else if (type.equals(Constants.TOURNAMENT)) {
            Tournament<TimetableSolution> tournament = (Tournament<TimetableSolution>) selection;
            pte = tournament.getPTE();
        }
    }

    @Override
    public String toString() {
        return "Selection: " + type +
                ", elitism: " + elitism;
    }
}
