package il.ac.mta.zuli.evolution.engine.tasks;

import il.ac.mta.zuli.evolution.engine.Descriptor;
import il.ac.mta.zuli.evolution.engine.EvolutionState;
import il.ac.mta.zuli.evolution.engine.TimeTableSolution;
import il.ac.mta.zuli.evolution.engine.evolutionengine.EvolutionEngine;
import il.ac.mta.zuli.evolution.engine.predicates.EndPredicate;
import javafx.application.Platform;
import javafx.concurrent.Task;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class RunAlgorithmTask extends Task<EvolutionState> {
    private final Descriptor descriptor;
    private final List<EndPredicate> endPredicates;
    private final int generationsStride;
    private final EvolutionState inEvolutionState;
    private  EvolutionState outEvolutionState;
    private final Consumer<TimeTableSolution> reportBestSolution;
    private final Consumer<EvolutionState> reportState;
    private TimeTableSolution bestSolutionEver;
    private EvolutionEngine<TimeTableSolution> evolutionEngine;

    public RunAlgorithmTask(
            Descriptor descriptor,
            List<EndPredicate> endPredicates,
            int generationsStride,
            EvolutionState evolutionState,
            Consumer<EvolutionState> reportState,
            Consumer<TimeTableSolution> reportBestSolution) {
        this.descriptor = descriptor;
        this.endPredicates = endPredicates;
        this.generationsStride = generationsStride;
        // we either receive a generation to resume from, or create an initial one
        this.inEvolutionState = evolutionState;
        this.evolutionEngine = new EvolutionEngine<TimeTableSolution>(descriptor.getEngineSettings(), descriptor.getRules());
        this.reportState = (EvolutionState state)->{
            Platform.runLater(()->{
                reportState.accept(outEvolutionState);
            });
        }
        this.reportBestSolution = (TimeTableSolution bestSolution) -> {
            Platform.runLater(() -> {
                reportBestSolution.accept(bestSolution);
            });
        };
    }

    @Override
    protected EvolutionState call() throws Exception {
        //initialGeneration is either null or not, depending on if we're resuming from pause or starting
        long startTime = System.currentTimeMillis();
        EvolutionState prevEvolutionState = inEvolutionState;
        EvolutionState currEvolutionState = null;

        if (inEvolutionState == null) {
            //if we're just now starting the task, and not resuming after pause
            prevEvolutionState = createFirstGenerationState();
            bestSolutionEver = prevEvolutionState.getGenerationBestSolution();
        } else {
            bestSolutionEver = prevEvolutionState.getBestSolutionSoFar();
        }


        reportBestSolution.accept(bestSolutionEver);

        //while the user didn't click Pause or Stop, and we haven't reached any of the end-conditions yet
        while (!isCancelled() && checkAllPredicates(prevEvolutionState)) {
            List<TimeTableSolution> currSolutions = evolutionEngine.execute(prevEvolutionState.getGenerationSolutions());
            // building the current EvolutionState
            long timeFromStart = System.currentTimeMillis() - startTime;
            long elapsedTime = inEvolutionState == null ? timeFromStart : inEvolutionState.getNetRunTime() + timeFromStart;

            currEvolutionState = new EvolutionState(
                    prevEvolutionState.getGenerationNum() + 1,
                    elapsedTime, //the generation time param is when we started the 1st generation
                    currSolutions,
                    bestSolutionEver);

            TimeTableSolution currBestSolution = currEvolutionState.getGenerationBestSolution();

            if (currBestSolution.getTotalFitnessScore() > bestSolutionEver.getTotalFitnessScore()) {
                bestSolutionEver = currBestSolution;
                reportBestSolution.accept(bestSolutionEver);
            }

            //stride for purposes of info-display and to save a stride-generation history
            //with addition of first generation
            int currGenerationNum = currEvolutionState.getGenerationNum();

            if (currGenerationNum == 1 || (currGenerationNum % generationsStride == 0)) {
                String message = String.format(
                        "Generation: %d. Top Score: %f",
                        currGenerationNum,
                        currBestSolution.getTotalFitnessScore());
                updateMessage(message);
            }

            prevEvolutionState = currEvolutionState;
            outEvolutionState=currEvolutionState;
        } //end of for loop
        System.out.println("after while Loop");
        System.out.println("generation number" + currEvolutionState.getGenerationNum());

        //TODO updateMessage about last stride
        //updateValue(currEvolutionState);
//        reportStrideLater.accept(new StrideData(currentGenerationNum - 1, currBestSolution));

        return currEvolutionState;
    }

    private boolean checkAllPredicates(EvolutionState evolutionState) {
        boolean result = true;
        boolean predicateResult = true;

        //we know (validation in header controller, that there's at least one predicate (max of 3)
        for (EndPredicate predicate : endPredicates) {
            switch (predicate.getType()) {
                case FITNESS:
                    predicateResult = predicate.test(bestSolutionEver.getTotalFitnessScore());
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
    private List<TimeTableSolution> getInitialPopulation() {
        int initialPopulationSize = descriptor.getPopulationSize();
        List<TimeTableSolution> initialPopulation = new ArrayList<>();

        for (int i = 0; i < initialPopulationSize; i++) {
            initialPopulation.add(new TimeTableSolution(descriptor.getTimeTable()));
        }
        evolutionEngine.evaluateSolutions(initialPopulation);
        return initialPopulation;
    }


}
