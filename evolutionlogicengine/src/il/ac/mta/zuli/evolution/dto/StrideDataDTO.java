package il.ac.mta.zuli.evolution.dto;

public class StrideDataDTO {
    private final int generationNum;
    private final double bestScoreInGeneration;

    public StrideDataDTO(int generationNum, double solution) {
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
