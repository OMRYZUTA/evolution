package il.ac.mta.zuli.evolution.engine.tasks;

import il.ac.mta.zuli.evolution.engine.Descriptor;
import il.ac.mta.zuli.evolution.engine.TimeTableSolution;
import il.ac.mta.zuli.evolution.engine.evolutionengine.EvolutionEngine;
import il.ac.mta.zuli.evolution.engine.predicates.FinishPredicate;
import javafx.concurrent.Task;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

public class RunAlgorithmTask extends Task<TimeTableSolution> {
    private final Descriptor descriptor;
    List<FinishPredicate> finishPredicates;
    private final int generationsStride;
    private TimeTableSolution bestSolutionEver = null;
    private int currentGenerationNum;
    private long start;
    //TODO later change to updateProgress and send one by one upwards
    private Map<Integer, TimeTableSolution> bestSolutionsInGenerationPerStride;

    public RunAlgorithmTask(List<FinishPredicate> finishPredicates, int generationsStride, Descriptor descriptor) {
        this.descriptor = descriptor;
        this.finishPredicates = finishPredicates;
        this.generationsStride = generationsStride;
    }

    @Override
    protected TimeTableSolution call() throws Exception {
        start = System.currentTimeMillis();

        EvolutionEngine<TimeTableSolution> evolutionEngine;
        updateMessage("starting to run evolution algorithm");
        bestSolutionsInGenerationPerStride = new TreeMap<>();

        List<TimeTableSolution> initialPopulation = getInitialGeneration();
        evolutionEngine = new EvolutionEngine(descriptor.getEngineSettings(), descriptor.getRules());

        List<TimeTableSolution> prevGeneration = initialPopulation;
        List<TimeTableSolution> currGeneration;
        double bestSolutionFitnessScore = 0;
        TimeTableSolution currBestSolution = null;
        currentGenerationNum = 1;

        while (checkAllPredicates()) {
            currGeneration = evolutionEngine.execute(prevGeneration);
            currBestSolution = currGeneration.stream().
                    sorted(Collections.reverseOrder()).limit(1).collect(Collectors.toList()).get(0);
            if (bestSolutionFitnessScore < currBestSolution.getTotalFitnessScore()) {
                this.bestSolutionEver = currBestSolution;
                bestSolutionFitnessScore = this.bestSolutionEver.getTotalFitnessScore();
            }

            //stride for purposes of info-display and to save a stride-generation history
            //with addition of first and last generation
            if (currentGenerationNum == 1 || (currentGenerationNum % generationsStride == 0)) {
                bestSolutionsInGenerationPerStride.put(currentGenerationNum, currBestSolution);
                System.out.println("current generation: " + currentGenerationNum);
//                System.out.println("best score: " + currBestSolution.getTotalFitnessScore());
                //TODO updateMessage ?
                //fireStrideDetails(i, currBestSolution);
            }

            prevGeneration = currGeneration;
            currentGenerationNum++;
        } //end of for loop
        bestSolutionsInGenerationPerStride.put(currentGenerationNum - 1, currBestSolution);
//        } catch (Throwable e) {
//            updateMessage("Failed running algorithm: " + e.getMessage());
//            //TODO figure it out
////            fireEvent("error", new ErrorEvent("Failed running evolution algorithm", ErrorType.RunError, e));
//            return null;
//        }

        updateMessage("Evolution algorithm completed running successfully");
        return bestSolutionEver;
    }

    private boolean checkAllPredicates() {
        boolean result = true, predicateResult = true;

        //we know (validation in header controller, that there's at least one predicate (max of 3)
        for (FinishPredicate predicate : finishPredicates) {
            switch (predicate.getType()) {
                case FITNESS:
                    if (bestSolutionEver != null) {
                        predicateResult = predicate.test(bestSolutionEver.getTotalFitnessScore());
                    }
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

//    private void fireStrideDetails(int i, TimeTableSolution currBestSolution) {
//        GenerationStrideScoreDTO strideScoreDTO = new GenerationStrideScoreDTO(i, currBestSolution.getTotalFitnessScore());
//        fireEvent("stride", new OnStrideEvent("generation ", i, strideScoreDTO));
//    }
}
