package il.ac.mta.zuli.evolution.engine;

public class StrideData {
    private final int generationNum;
    private final TimeTableSolution solution;

    public StrideData(int generationNum, TimeTableSolution solution) {
        this.generationNum = generationNum;
        this.solution = solution;
    }

    public int getGenerationNum() {
        return generationNum;
    }

    public TimeTableSolution getSolution() {
        return solution;
    }
}
