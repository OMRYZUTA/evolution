package il.ac.mta.zuli.evolution.engine;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class EvolutionState {
    private final int generationNum;
    private final List<TimetableSolution> solutions;
    private final TimetableSolution bestSolutionSoFar;
    //the way we save the state (for pause/resume), this is the time elapsed since the beginning of the task-call
    private final long netRunTime;

    public EvolutionState(int generationNum,
                          long time,
                          List<TimetableSolution> generationSolutions,
                          TimetableSolution bestSolutionSoFar) {
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

    public List<TimetableSolution> getGenerationSolutions() {
        return solutions;
    }

    public TimetableSolution getGenerationBestSolution() {
        return solutions.stream().
                sorted(Collections.reverseOrder())
                .limit(1).collect(Collectors.toList()).get(0);
    }

    public TimetableSolution getBestSolutionSoFar() {
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
