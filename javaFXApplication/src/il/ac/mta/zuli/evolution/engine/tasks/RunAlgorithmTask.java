package il.ac.mta.zuli.evolution.engine.tasks;

import il.ac.mta.zuli.evolution.engine.Descriptor;
import il.ac.mta.zuli.evolution.engine.StrideData;
import il.ac.mta.zuli.evolution.engine.TimeTableSolution;
import il.ac.mta.zuli.evolution.engine.evolutionengine.EvolutionEngine;
import il.ac.mta.zuli.evolution.engine.predicates.FinishPredicate;
import javafx.application.Platform;
import javafx.concurrent.Task;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class RunAlgorithmTask extends Task<TimeTableSolution> {
    private final Descriptor descriptor;
    private final List<FinishPredicate> finishPredicates;
    private final int generationsStride;
    private final Consumer<StrideData> reportStrideLater;
    private int currentGenerationNum;
    private long start;

    public RunAlgorithmTask(List<FinishPredicate> finishPredicates, int generationsStride, Descriptor descriptor, Consumer<StrideData> reportStride) {
        this.descriptor = descriptor;
        this.finishPredicates = finishPredicates;
        this.generationsStride = generationsStride;
        this.reportStrideLater = (StrideData strideData) -> {
            Platform.runLater(() -> {
                reportStride.accept(strideData);
            });
        };
    }

    @Override
    protected TimeTableSolution call() throws Exception {
        start = System.currentTimeMillis();
        List<TimeTableSolution> initialPopulation = getInitialGeneration();
        EvolutionEngine<TimeTableSolution> evolutionEngine = new EvolutionEngine<TimeTableSolution>(descriptor.getEngineSettings(), descriptor.getRules());

        List<TimeTableSolution> prevGeneration = initialPopulation;
        List<TimeTableSolution> currGeneration;
        double bestSolutionFitnessScore = 0;
        TimeTableSolution currBestSolution = null;
        TimeTableSolution bestSolutionEver = null;
        currentGenerationNum = 1;

        while (checkAllPredicates(bestSolutionFitnessScore)) {
            currGeneration = evolutionEngine.execute(prevGeneration);
            currBestSolution = currGeneration.stream().
                    sorted(Collections.reverseOrder()).limit(1).collect(Collectors.toList()).get(0);

            if (bestSolutionFitnessScore < currBestSolution.getTotalFitnessScore()) {
                bestSolutionEver = currBestSolution;
                bestSolutionFitnessScore = bestSolutionEver.getTotalFitnessScore();
            }

            //stride for purposes of info-display and to save a stride-generation history
            //with addition of first and last generation
            if (currentGenerationNum == 1 || (currentGenerationNum % generationsStride == 0)) {
                reportStrideLater.accept(new StrideData(currentGenerationNum, currBestSolution));
                System.out.println("current generation: " + currentGenerationNum); //TODO delete later
            }

            prevGeneration = currGeneration;
            currentGenerationNum++;
        } //end of for loop

        reportStrideLater.accept(new StrideData(currentGenerationNum - 1, currBestSolution));
        updateMessage("Evolution algorithm completed running successfully");
        return bestSolutionEver;
    }

    private boolean checkAllPredicates(double currentScore) {
        boolean result = true;
        boolean predicateResult = true;

        //we know (validation in header controller, that there's at least one predicate (max of 3)
        for (FinishPredicate predicate : finishPredicates) {
            switch (predicate.getType()) {
                case FITNESS:
                    predicateResult = predicate.test(currentScore);
                    break;
                case GENERATIONS:
                    predicateResult = predicate.test((double) currentGenerationNum);
                    break;
                case TIME:
                    long elapsedTimeMillis = System.currentTimeMillis() - start;
                    float elapsedTimeMin = elapsedTimeMillis / (60 * 1000F);
                    predicateResult = predicate.test((double) elapsedTimeMin);
                    break;
            }
//            System.out.println("predicate + result" + predicate.getType() + " " + predicateResult);
            result = result && predicateResult;
        }

        return result;
    }

    @NotNull
    private List<TimeTableSolution> getInitialGeneration() {
        int initialPopulationSize = descriptor.getPopulationSize();
        List<TimeTableSolution> initialPopulation = new ArrayList<>();

        for (int i = 0; i < initialPopulationSize; i++) {
            initialPopulation.add(new TimeTableSolution(descriptor.getTimeTable()));
        }

        return initialPopulation;
    }
}
