package il.ac.mta.zuli.evolution.engine.tasks;

import il.ac.mta.zuli.evolution.dto.StrideDataDTO;
import il.ac.mta.zuli.evolution.engine.Descriptor;
import il.ac.mta.zuli.evolution.engine.EvolutionState;
import il.ac.mta.zuli.evolution.engine.LogicalRunStatus;
import il.ac.mta.zuli.evolution.engine.TimetableSolution;
import il.ac.mta.zuli.evolution.engine.evolutionengine.EvolutionEngine;
import il.ac.mta.zuli.evolution.engine.predicates.EndPredicate;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class RunAlgorithmTask implements Runnable {
    private final Descriptor descriptor;
    private final List<EndPredicate> endPredicates;
    private final int generationsStride;
    private final EvolutionState inEvolutionState;
    private final Consumer<EvolutionState> reportState;
    private final Consumer<StrideDataDTO> reportStrideData;
    private TimetableSolution bestSolutionEver;
    private final EvolutionEngine<TimetableSolution> evolutionEngine;
    private boolean paused;
    private boolean stopped;

    public RunAlgorithmTask(
            Descriptor descriptor,
            List<EndPredicate> endPredicates,
            int generationsStride,
            EvolutionState evolutionState,
            Consumer<EvolutionState> reportState,
            Consumer<StrideDataDTO> reportStrideData) {
        this.descriptor = descriptor;
        this.endPredicates = endPredicates;
        this.generationsStride = generationsStride;
        // we either receive a generation to resume from, or create an initial one
        this.inEvolutionState = evolutionState;
        this.evolutionEngine = new EvolutionEngine<TimetableSolution>(
                descriptor.getEngineSettings(), descriptor.getRules());
        this.reportState = reportState;
        this.reportStrideData = reportStrideData;
        this.paused = false;
        this.stopped = false;
    }

    @Override
    public void run() {
        //we'll either report this state on successful exit from the loop or with an exception in the catch-block
        EvolutionState outEvolutionState = null;

        try {
            System.out.println("**********in Runnable******");

            //initialGeneration is either null or not, depending on if we're resuming from pause or starting
            long startTime = System.currentTimeMillis();
            EvolutionState prevEvolutionState = inEvolutionState; //our way to resume after pause
            EvolutionState currEvolutionState = null;

            if (prevEvolutionState == null) {
                //if we're just now starting the task (and not resuming after pause)
                prevEvolutionState = createFirstGenerationState();
                reportFirstGeneration(prevEvolutionState);
                bestSolutionEver = prevEvolutionState.getGenerationBestSolution();
            } else {
                //if we're resuming the task
                bestSolutionEver = prevEvolutionState.getBestSolutionSoFar();
            }

            prevEvolutionState.setStatus(LogicalRunStatus.RUNNING);
            int currGenerationNum = 0;
            TimetableSolution currBestSolution = null;

            //while the user didn't click Pause or Stop, and we haven't reached any of the end-conditions yet
            while (!isPaused() && !isStopped() && checkAllPredicates(prevEvolutionState)) {
                List<TimetableSolution> currSolutions = evolutionEngine.execute(prevEvolutionState.getGenerationSolutions());
                // building the current EvolutionState
                long timeFromStart = System.currentTimeMillis() - startTime;
                long elapsedTime = inEvolutionState == null ? timeFromStart : inEvolutionState.getNetRunTime() + timeFromStart;

                currEvolutionState = new EvolutionState(
                        prevEvolutionState.getGenerationNum() + 1,
                        elapsedTime, //the generation time param is when we started the 1st generation
                        currSolutions,
                        bestSolutionEver);

                currBestSolution = currEvolutionState.getGenerationBestSolution();

                if (currBestSolution.getFitnessScore() > bestSolutionEver.getFitnessScore()) {
                    bestSolutionEver = currBestSolution;
                }

                //stride for purposes of info-display and to save a stride-generation history
                //with addition of first generation
                currGenerationNum = currEvolutionState.getGenerationNum();

                if (currGenerationNum % generationsStride == 0) {
                    reportStrideData.accept(
                            new StrideDataDTO(currGenerationNum, currBestSolution.getFitnessScore()));
                }

                prevEvolutionState = currEvolutionState;
                outEvolutionState = currEvolutionState;
                reportState.accept(outEvolutionState); //essential in order to allow resume-after-pause
            } //end of for loop

            if (outEvolutionState != null) {
                if (isPaused()) {
                    outEvolutionState.setStatus(LogicalRunStatus.PAUSED);
                } else {
                    if (currGenerationNum % generationsStride != 0) {
                        reportStrideData.accept(
                                new StrideDataDTO(currGenerationNum, currBestSolution.getFitnessScore()));
                    }

                    if (isStopped()) {
                        outEvolutionState.setStatus(LogicalRunStatus.STOPPED);

                    } else {
                        outEvolutionState.setStatus(LogicalRunStatus.COMPLETED);
                    }
                }
            }
        } catch (Throwable e) {
            if (outEvolutionState != null) {
                outEvolutionState.setException(e);
                outEvolutionState.setStatus(LogicalRunStatus.ERROR);
            }

            System.out.println("**********Exception in Runnable******");
            System.out.println(e.getMessage());
        } finally {
            if (outEvolutionState != null) {
                outEvolutionState.setTaskDone();
                reportState.accept(outEvolutionState); // reporting the last state as "done"}
            }
        }
    }

    private void reportFirstGeneration(EvolutionState prevEvolutionState) {
        reportStrideData.accept(
                new StrideDataDTO(prevEvolutionState.getGenerationNum(), prevEvolutionState.getGenerationBestSolution().getFitnessScore()));
        reportState.accept(prevEvolutionState);
    }

    public synchronized void pause() {
        paused = true;
    }

    public synchronized boolean isPaused() {
        return paused;
    }

    public synchronized void stop() {
        stopped = true;
    }

    public synchronized boolean isStopped() {
        return stopped;
    }

    private boolean checkAllPredicates(EvolutionState evolutionState) {
        boolean result = true;
        boolean predicateResult = true;

        //we know (validation in header controller, that there's at least one predicate (max of 3)
        for (EndPredicate predicate : endPredicates) {
            switch (predicate.getType()) {
                case SCORE:
                    predicateResult = predicate.test(bestSolutionEver.getFitnessScore());
                    break;
                case GENERATIONS:
                    predicateResult = predicate.test((double) evolutionState.getGenerationNum());
                    break;
                case TIME:
                    //the state's getNetRunTime() method returns the time elapsed since the beginning of the call (not including paused time)
                    float elapsedTimeMin = evolutionState.getNetRunTime() / (60 * 1000F);
                    predicateResult = predicate.test((double) elapsedTimeMin);
                    break;
            }

            result = result && predicateResult;
        }

        return result;
    }

    private EvolutionState createFirstGenerationState() {
        return new EvolutionState(1, 0, getInitialPopulation(), bestSolutionEver);
    }

    @NotNull
    private List<TimetableSolution> getInitialPopulation() {
        int initialPopulationSize = descriptor.getPopulationSize();
        List<TimetableSolution> initialPopulation = new ArrayList<>();

        for (int i = 0; i < initialPopulationSize; i++) {
            initialPopulation.add(new TimetableSolution(descriptor.getTimeTable()));
        }
        evolutionEngine.evaluateSolutions(initialPopulation);
        return initialPopulation;
    }

}