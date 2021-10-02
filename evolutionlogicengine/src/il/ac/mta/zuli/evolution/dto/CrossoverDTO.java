package il.ac.mta.zuli.evolution.dto;

import il.ac.mta.zuli.evolution.engine.TimetableSolution;
import il.ac.mta.zuli.evolution.engine.evolutionengine.crossover.CrossoverInterface;

public class CrossoverDTO {
    private final String name;
    private final int cuttingPoints;

    public CrossoverDTO(String name, int cuttingPoints) {
        this.name = name;
        this.cuttingPoints = cuttingPoints;
    }

    public CrossoverDTO(CrossoverInterface<TimetableSolution> crossover) {
        this.name = crossover.getClass().getSimpleName(); //TODO exactly as in Constants
        this.cuttingPoints = crossover.getNumOfCuttingPoints();
    }

    public String getName() {
        return name;
    }

    public int getCuttingPoints() {
        return cuttingPoints;
    }

    @Override
    public String toString() {
        return "Crossover: " + name +
                ", number of cutting points " + cuttingPoints;
    }
}
