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
    //Ex 3 additions:
    private Throwable exception; // for exception from Runnable
    //for when Runnable completed its run (either if successfully, unsuccessfully with exception, paused or stopped by user)
    private boolean taskDone;
    private LogicalRunStatus status; //if running, paused or stopped

    public EvolutionState(int generationNum,
                          long time,
                          List<TimetableSolution> generationSolutions,
                          TimetableSolution bestSolutionSoFar) {
        this.generationNum = generationNum;
        this.solutions = generationSolutions;
        this.netRunTime = time;
        this.bestSolutionSoFar = bestSolutionSoFar;
        this.taskDone = false;
    }

    public void setTaskDone() {
        taskDone = true;
    }

    public boolean isTaskDone() {
        return taskDone;
    }

    public synchronized void setStatus(LogicalRunStatus status) {
        this.status = status;
    }

    public LogicalRunStatus getStatus() {
        return status;
    }

    public void setException(Throwable exception) {
        this.exception = exception;
    }

    public Throwable getException() {
        return exception;
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

    public double getGenerationBestScore() {
        return getGenerationBestSolution().getFitnessScore();
    }

    public TimetableSolution getBestSolutionSoFar() {
        return bestSolutionSoFar;
    }

    public double getBestScoreSoFar() {
        return bestSolutionSoFar.getFitnessScore();
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
