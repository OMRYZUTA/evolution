package il.ac.mta.zuli.evolution.dto;

public class StrideDataDTO {
    private final int generationNum;
    private final TimeTableSolutionDTO solution;

    public StrideDataDTO(int generationNum, TimeTableSolutionDTO solution) {
        this.generationNum = generationNum;
        this.solution = solution;
    }

    public int getGenerationNum() {
        return generationNum;
    }

    public TimeTableSolutionDTO getSolution() {
        return solution;
    }
}
