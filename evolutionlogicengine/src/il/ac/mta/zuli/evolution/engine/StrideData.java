package il.ac.mta.zuli.evolution.engine;

public class StrideData {
    private final int generationNum;
    private final double bestScoreInGeneration;

    public StrideData(int generationNum, double solution) {
        this.generationNum = generationNum;
        this.bestScoreInGeneration = solution;
    }

    public int getGenerationNum() {
        return generationNum;
    }

    public double getBestScoreInGeneration() {
        return bestScoreInGeneration;
    }

    @Override
    public String toString() {
        return "StrideData{" +
                "generationNum=" + generationNum +
                ", bestScoreInGeneration=" + bestScoreInGeneration +
                '}';
    }
}
