package il.ac.mta.zuli.evolution.dto;

import il.ac.mta.zuli.evolution.engine.LogicalRunStatus;

public class ProgressDTO {
    private final int generationNum;
    private final double bestScoreInGeneration;
    private final double bestScoreSoFar;
    private final String status;

    public ProgressDTO(int generationNum, double bestScoreInGeneration, double bestScoreSoFar, LogicalRunStatus status) {
        this.generationNum = generationNum;
        this.bestScoreInGeneration = bestScoreInGeneration;
        this.bestScoreSoFar = bestScoreSoFar;
        this.status = status.toString();
    }
}
