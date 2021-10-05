package il.ac.mta.zuli.evolution.dto;

public class GenerationProgressDTO {
    private final int generationNum;
    private final double bestScoreInGeneration;

    public GenerationProgressDTO(int generationNum, double bestScoreInGeneration) {
        this.generationNum = generationNum;
        this.bestScoreInGeneration = bestScoreInGeneration;
    }
}
