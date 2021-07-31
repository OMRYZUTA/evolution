package il.ac.mta.zuli.evolution.dto;

public class CrossoverDTO {
    String name;
    String cuttingPoints;
    public  CrossoverDTO(String name, String cuttingPoints){
        this.name = name;
        this.cuttingPoints = cuttingPoints;
    }

    public String getName() {
        return name;
    }

    public String getCuttingPoints() {
        return cuttingPoints;
    }

    @Override
    public String toString() {
        return "CrossoverDTO{" +
                "name='" + name + '\'' +
                ", cuttingPoints='" + cuttingPoints + '\'' +
                '}';
    }
}
