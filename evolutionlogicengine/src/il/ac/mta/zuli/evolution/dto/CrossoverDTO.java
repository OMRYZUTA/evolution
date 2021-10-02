package il.ac.mta.zuli.evolution.dto;

import il.ac.mta.zuli.evolution.Constants;
import il.ac.mta.zuli.evolution.engine.TimetableSolution;
import il.ac.mta.zuli.evolution.engine.evolutionengine.crossover.AspectOriented;
import il.ac.mta.zuli.evolution.engine.evolutionengine.crossover.CrossoverInterface;

public class CrossoverDTO {
    private final String type;
    private final int cuttingPoints;
    private String orientation = null;

    public CrossoverDTO(CrossoverInterface<TimetableSolution> crossover) {
        this.type = crossover.getCrossoverType(); //TODO exactly as in Constants
        this.cuttingPoints = crossover.getNumOfCuttingPoints();

        if (type.equals(Constants.ASPECT_ORIENTED)) {
            AspectOriented<TimetableSolution> aspectOriented = (AspectOriented<TimetableSolution>) crossover;
            this.orientation = aspectOriented.getOrientation().name().toLowerCase();
        }
    }

    public String getType() {
        return type;
    }

    public int getCuttingPoints() {
        return cuttingPoints;
    }

    public String getOrientation() {
        return orientation;
    }

    @Override
    public String toString() {
        return "Crossover: " + type +
                ", number of cutting points " + cuttingPoints;
    }
}
