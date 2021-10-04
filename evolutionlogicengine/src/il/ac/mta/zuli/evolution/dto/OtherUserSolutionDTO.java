package il.ac.mta.zuli.evolution.dto;

public class OtherUserSolutionDTO {
    String userName;
    Double bestScore;
    Integer currentGeneration;

    public OtherUserSolutionDTO(
            String userName,
            Double bestScore,
            Integer currentGeneration) {
        this.userName = userName;
        this.bestScore = bestScore;
        this.currentGeneration = currentGeneration;
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
}
