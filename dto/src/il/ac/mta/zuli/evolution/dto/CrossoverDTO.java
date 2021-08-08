package il.ac.mta.zuli.evolution.dto;

public class CrossoverDTO {
    String name;
    int cuttingPoints;

    public CrossoverDTO(String name, int cuttingPoints) {
        this.name = name;
        this.cuttingPoints = cuttingPoints;
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
