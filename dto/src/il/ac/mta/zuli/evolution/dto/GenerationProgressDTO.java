package il.ac.mta.zuli.evolution.dto;

public class GenerationProgressDTO {
    private final int generationNum;
    private final TimeTableSolutionDTO bestSolutionDTO;
    private final double deltaFromPreviousBestSolution;

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

    @Override
    public String toString() {
        return "generation " + generationNum +
                ", score " + bestSolutionDTO.getTotalFitnessScore() +
                ", delta " + deltaFromPreviousBestSolution;
    }
}
