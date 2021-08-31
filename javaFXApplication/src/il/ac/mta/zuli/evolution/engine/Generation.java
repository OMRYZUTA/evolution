package il.ac.mta.zuli.evolution.engine;

import java.util.List;
import java.util.Objects;

public class Generation {
    private int generationNum;
    private List<TimeTableSolution> generationSolutions;

    public Generation(int generationNum, List<TimeTableSolution> generationSolutions) {
        this.generationNum = generationNum;
        this.generationSolutions = generationSolutions;
    }

    public int getGenerationNum() {
        return generationNum;
    }

    public void setGenerationNum(int generationNum) {
        this.generationNum = generationNum;
    }

    public List<TimeTableSolution> getGenerationSolutions() {
        return generationSolutions;
    }

    public void setGenerationSolutions(List<TimeTableSolution> generationSolutions) {
        this.generationSolutions = generationSolutions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Generation that = (Generation) o;
        return generationNum == that.generationNum && Objects.equals(generationSolutions, that.generationSolutions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(generationNum, generationSolutions);
    }
}
