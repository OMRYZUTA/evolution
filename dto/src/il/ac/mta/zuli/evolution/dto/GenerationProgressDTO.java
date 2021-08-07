package il.ac.mta.zuli.evolution.dto;

public class GenerationProgressDTO {
    private int generationNum;
    private TimeTableSolutionDTO bestSolutionDTO;
    private double deltaFromPreviousBestSolution;

    public GenerationProgressDTO(int generationNum, TimeTableSolutionDTO bestSolutionDTO, double deltaFromPreviousBestSolution) {
        this.generationNum = generationNum;
        this.bestSolutionDTO = bestSolutionDTO;
        this.deltaFromPreviousBestSolution = deltaFromPreviousBestSolution;
    }

    public int getGenerationNum() {
        return generationNum;
    }

    public TimeTableSolutionDTO getBestSolutionDTO() {
        return bestSolutionDTO;
    }

    public double getDeltaFromPreviousBestSolution() {
        return deltaFromPreviousBestSolution;
    }
}
