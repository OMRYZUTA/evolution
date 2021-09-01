package il.ac.mta.zuli.evolution.engine;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class EvolutionState {
    private final int generationNum;
    private final List<TimeTableSolution> solutions;
    private final TimeTableSolution bestSolutionSoFar;
    //the way we save the state (for pause/resume), this is the time elapsed since the beginning of the task-call
    private final long netRunTime;

    public EvolutionState(int generationNum,
                          long time,
                          List<TimeTableSolution> generationSolutions,
                          TimeTableSolution bestSolutionSoFar) {
        this.generationNum = generationNum;
        this.solutions = generationSolutions;
        this.netRunTime = time;
        this.bestSolutionSoFar = bestSolutionSoFar;
    }

    public int getGenerationNum() {
        return generationNum;
    }

    public long getNetRunTime() {
        return netRunTime;
    }

    public List<TimeTableSolution> getGenerationSolutions() {
        return Collections.unmodifiableList(solutions);
    }

    public TimeTableSolution getGenerationBestSolution() {
        //when this method is called the 1st generation wasn't evaluated yet, so there's no best solution
        if (generationNum == 1) {
            return null;
        }

        return solutions.stream().
                sorted(Collections.reverseOrder())
                .limit(1).collect(Collectors.toList()).get(0);
    }

    public TimeTableSolution getBestSolutionSoFar() {
        return bestSolutionSoFar;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EvolutionState that = (EvolutionState) o;
        return generationNum == that.generationNum && Objects.equals(solutions, that.solutions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(generationNum, solutions);
    }
}
