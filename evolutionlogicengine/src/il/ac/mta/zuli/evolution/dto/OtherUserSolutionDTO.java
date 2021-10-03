package il.ac.mta.zuli.evolution.dto;

public class OtherUserSolutionDTO {
    String userName;
    Double bestScore;
    Integer currentGeneration;
    boolean doneRunning;

    public OtherUserSolutionDTO(
            String userName,
            Double bestScore,
            Integer currentGeneration,
            boolean doneRunning) {
        this.userName = userName;
        this.bestScore = bestScore;
        this.currentGeneration = currentGeneration;
        this.doneRunning = doneRunning;
    }

    public String getUserName() {
        return userName;
    }

    public Double getBestScore() {
        return bestScore;
    }

    public Integer getCurrentGeneration() {
        return currentGeneration;
    }

    public boolean isDoneRunning() {
        return doneRunning;
    }
}
