package il.ac.mta.zuli.evolution.engine.tasks;

import il.ac.mta.zuli.evolution.engine.Descriptor;
import il.ac.mta.zuli.evolution.engine.TimeTableSolution;
import il.ac.mta.zuli.evolution.engine.evolutionengine.EvolutionEngine;
import javafx.concurrent.Task;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

public class RunAlgorithmTask extends Task<TimeTableSolution> {
    private final Descriptor descriptor;
    private final int numOfGenerations;
    private final int generationsStride;
    private TimeTableSolution bestSolutionEver = null;
    //TODO later change to updateProgress and send one by one upwards
    private Map<Integer, TimeTableSolution> bestSolutionsInGenerationPerStride;


    public RunAlgorithmTask(int numOfGenerations, int generationsStride, Descriptor descriptor) {
        this.descriptor = descriptor;
        this.numOfGenerations = numOfGenerations;
        this.generationsStride = generationsStride;
    }

    @Override
    protected TimeTableSolution call() throws Exception {
        EvolutionEngine<TimeTableSolution> evolutionEngine;
        System.out.println("in RunAlgoTask call()"); //TODO delete later
        updateMessage("starting to run evolution algorithm");

        try {
            bestSolutionsInGenerationPerStride = new TreeMap<>();
            List<TimeTableSolution> initialPopulation = getInitialGeneration();

            evolutionEngine = new EvolutionEngine(descriptor.getEngineSettings(), descriptor.getRules());

            List<TimeTableSolution> prevGeneration = initialPopulation;
            List<TimeTableSolution> currGeneration;
            double bestSolutionFitnessScore = 0;

            for (int i = 1; i <= this.numOfGenerations; i++) {
                System.out.println("in RunAlgoTask call(), start of loop " + i); //TODO delete later
                currGeneration = evolutionEngine.execute(prevGeneration);
                System.out.println("in RunAlgoTask call(), after exectue"); //TODO delete later
                TimeTableSolution currBestSolution = currGeneration.stream().
                        sorted(Collections.reverseOrder()).limit(1).collect(Collectors.toList()).get(0);
                System.out.println("in RunAlgoTask call(), after currBestSolution assignment"); //TODO delete later
                if (bestSolutionFitnessScore < currBestSolution.getTotalFitnessScore()) {
                    System.out.println("in RunAlgoTask call(), inside if 50"); //TODO delete later
                    this.bestSolutionEver = currBestSolution;
                    bestSolutionFitnessScore = this.bestSolutionEver.getTotalFitnessScore();
                }

                //stride for purposes of info-display and to save a stride-generation history
                //with addition of first and last generation
                if (i == 1 || (i % generationsStride == 0) || (i == numOfGenerations)) {
                    bestSolutionsInGenerationPerStride.put(i, currBestSolution);
                    //TODO updateMessage ?
                    //fireStrideDetails(i, currBestSolution);
                }

                prevGeneration = currGeneration;
                System.out.println("in RunAlgoTask call(), end of loop " + i); //TODO delete later
            } //end of for loop
        } catch (Throwable e) {
//            fireEvent("error", new ErrorEvent("Failed running evolution algorithm", ErrorType.RunError, e));
        }

        updateMessage("Evolution algorithm completed running successfully");
        return bestSolutionEver;
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

//    private void fireStrideDetails(int i, TimeTableSolution currBestSolution) {
//        GenerationStrideScoreDTO strideScoreDTO = new GenerationStrideScoreDTO(i, currBestSolution.getTotalFitnessScore());
//        fireEvent("stride", new OnStrideEvent("generation ", i, strideScoreDTO));
//    }
}
