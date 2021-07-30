package il.ac.mta.zuli.evolution.engine.evolutionengine;

import il.ac.mta.zuli.evolution.engine.xmlparser.generated.ETTCrossover;

public class Crossover {
    private CrossoverType type;
    private int cuttingPoint;

    public Crossover(ETTCrossover ettCrossover) {

    }

    public CrossoverType getType() {
        return type;
    }

    private void setType(String ettType) {
        switch (ettType.toLowerCase()) {
            case "daytimeoriented":
                this.type = CrossoverType.DAYTIMEORIENTED;
                break;
            case "aspectoriented":
                this.type = CrossoverType.ASPECTORIENTED;
                break;
            default:
                //TODO throw exception
                System.out.println("invalid crossover type");
        }
    }

    public int getCuttingPoint() {
        return cuttingPoint;
    }

    public void setCuttingPoint(int cuttingPoint) {
        this.cuttingPoint = cuttingPoint;
    }
}
