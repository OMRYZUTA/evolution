package il.ac.mta.zuli.evolution.dto;

public class GenerationStrideScoreDTO {
    private final int generationNum;
    private final double bestScore;

    public GenerationStrideScoreDTO(int generationNum, double bestScore) {
        this.generationNum = generationNum;
        this.bestScore = bestScore;
    }

    public int getGenerationNum() {
        return generationNum;
    }

    public double getBestScore() {
        return bestScore;
    }

    @Override
    public String toString() {
        return "generationStrideScore{" +
                "generationNum=" + generationNum +
                ", bestScore=" + bestScore +
                '}';
    }
}
