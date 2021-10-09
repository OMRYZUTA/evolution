package il.ac.mta.zuli.evolution.engine;

import il.ac.mta.zuli.evolution.Constants;
import il.ac.mta.zuli.evolution.dto.GenerationProgressDTO;
import il.ac.mta.zuli.evolution.dto.StrideDataDTO;
import il.ac.mta.zuli.evolution.engine.evolutionengine.EngineSettings;
import il.ac.mta.zuli.evolution.engine.exceptions.InvalidOperationException;
import il.ac.mta.zuli.evolution.engine.exceptions.ValidationException;
import il.ac.mta.zuli.evolution.engine.predicates.EndPredicate;
import il.ac.mta.zuli.evolution.engine.predicates.EndPredicateType;
import il.ac.mta.zuli.evolution.engine.tasks.RunAlgorithmTask;
import il.ac.mta.zuli.evolution.engine.timetable.TimeTable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TimeTableEngine implements Engine {
    private final Descriptor descriptor;
    private EvolutionState currEvolutionState; // includes taskDone flag and exception-from-task fields
    private RunAlgorithmTask currentRunningTask; //in Ex2 this field was Task<?>
    //EX3 additions to class:
    private List<EndPredicate> endPredicates;
    private int generationsStride;
    private TimetableSolution bestSolution;
    private final List<StrideDataDTO> strideData; //generation num and best score in generation

    //new CTOR for Ex 3
    public TimeTableEngine(TimeTable timetable,
                           Map<String, Object> engineSettingsMap,
                           Map<String, Object> endPredicatesMap,
                           int stride) {

        EngineSettings<TimetableSolution> engineSettings = new EngineSettings<>(engineSettingsMap, timetable);
        this.descriptor = new Descriptor(timetable, engineSettings);
        setGenerationsStride(stride);
        this.endPredicates = generatePredicates(endPredicatesMap);
        this.strideData = new ArrayList<>();
    }

    //#region algorithm-flow methods:
    @Override
    public void startEvolutionAlgorithm() {
        if (!isXMLLoaded()) {
            throw new InvalidOperationException("Can not execute Evolution Algorithm, a file is not loaded");
        }

        if (null == currEvolutionState
                || (LogicalRunStatus.PAUSED != currEvolutionState.getStatus()
                && LogicalRunStatus.RUNNING != currEvolutionState.getStatus())) {
            //we only allow to start again if stopped or complete
            runEvolutionAlgorithm(null); // sending null as currentState, to start a fresh run
        } else {
            throw new InvalidOperationException("Run can not start");
        }
    }

    @Override
    public void resumeEvolutionAlgorithm() {
        if (null != currEvolutionState && LogicalRunStatus.PAUSED == currEvolutionState.getStatus()) {
            runEvolutionAlgorithm(this.currEvolutionState);
        } else {
            // task was not paused - we can not resume it
            throw new InvalidOperationException("Run can not resume");
        }
    }

    @Override
    public void pauseEvolutionAlgorithm() {
        if (null != currEvolutionState && LogicalRunStatus.RUNNING == currEvolutionState.getStatus()) {
            currentRunningTask.pause();
        } else {
            // task was not running - we can not pause it
            throw new InvalidOperationException("Run can not pause");
        }
    }

    @Override
    public void stopEvolutionAlgorithm() {
        if (null != currEvolutionState
                && (LogicalRunStatus.RUNNING == currEvolutionState.getStatus()
                || LogicalRunStatus.PAUSED == currEvolutionState.getStatus())) {
            currentRunningTask.stop();
        } else {
            // task was not running/paused - can not stop
            throw new InvalidOperationException("Run can not stop");
        }

//        no need for this in Ex 3 because each timetableEngine only runs for a single timeTable
//        this.currEvolutionState = null; //in case of STOP we don't want to save the previous state
    }

    private void runEvolutionAlgorithm(EvolutionState currentState) {
        currentRunningTask = new RunAlgorithmTask(
                this.descriptor,
                endPredicates,
                generationsStride,
                currentState,
                (EvolutionState state) -> {
                    System.out.println("state status " + state.getStatus() + ", isDone? " + state.isTaskDone());
                    this.currEvolutionState = state;
                },
                (TimetableSolution solution) -> bestSolution = solution,
                (StrideDataDTO data) -> strideData.add(data));

        new Thread(currentRunningTask, "EvolutionAlgorithmThread").start();
    }

    //#endregion

    //#region getters:
    @Override
    public TimeTable getTimeTable() {
        return descriptor.getTimeTable();
    }

    public Descriptor getDescriptor() {
        return descriptor;
    }

    @Override
    public EngineSettings getEngineSettings() {
        return descriptor.getEngineSettings();
    }

    public Integer getCurrGenerationNum() {
        if (currEvolutionState != null) {
            return currEvolutionState.getGenerationNum();
        }
        return null;
    }

    public List<EndPredicate> getEndPredicates() {
        return endPredicates;
    }

    public int getGenerationsStride() {
        return generationsStride;
    }

    public TimetableSolution getBestSolution() {
        return bestSolution;
    }

    public double getBestScore() {
        if (bestSolution != null) {
            return bestSolution.getFitnessScore();
        } else {
            return 0;
        }
    }

    public GenerationProgressDTO getProgressData() {
        return new GenerationProgressDTO(
                currEvolutionState.getGenerationNum(),
                currEvolutionState.getGenerationBestScore(),
                currEvolutionState.getBestScoreSoFar(),
                currEvolutionState.getStatus()
        );
    }

    public List<StrideDataDTO> getStrideData() {
        return strideData;
    }
    //#endregion

    public void setNewAlgorithmConfiguration(
            Map<String, Object> engineSettingsMap,
            Map<String, Object> endPredicatesMap,
            int generationStride) {
        descriptor.setEngineSettings(new EngineSettings<>(engineSettingsMap, descriptor.getTimeTable()));
        setGenerationsStride(generationStride);
        endPredicates = generatePredicates(endPredicatesMap);
    }

    private void setGenerationsStride(int stride) {
        if (stride > 0) {
            this.generationsStride = stride; //we'll check stride < generationNum only if that predicate is applied
        } else {
            throw new ValidationException("Stride must be a positive number");
        }
    }

    private List<EndPredicate> generatePredicates(Map<String, Object> endPredicatesMap) {
        // for example: endPredicatesMap = {numOfGenerations: 120, fitnessScore: 92.3, time: 2}
        List<EndPredicate> endPredicates = new ArrayList<>();

        if (endPredicatesMap != null) {
            if (endPredicatesMap.containsKey(Constants.NUM_OF_GENERATIONS)) {
                addPredicateToList(
                        endPredicatesMap,
                        endPredicates,
                        EndPredicateType.GENERATIONS,
                        Constants.NUM_OF_GENERATIONS);
            }

            if (endPredicatesMap.containsKey(Constants.SCORE)) {
                addPredicateToList(
                        endPredicatesMap,
                        endPredicates,
                        EndPredicateType.SCORE,
                        Constants.SCORE);
            }

            if (endPredicatesMap.containsKey(Constants.TIME)) {
                addPredicateToList(
                        endPredicatesMap,
                        endPredicates,
                        EndPredicateType.TIME,
                        Constants.TIME);
            }

            if (endPredicates.size() == 0) {
                throw new ValidationException("Please select at least one End Condition");
            }

            return endPredicates;
        } else {
            throw new ValidationException("Please select at least one End Condition");
        }
    }

    private void addPredicateToList(
            Map<String, Object> endPredicatesMap,
            List<EndPredicate> endPredicates,
            EndPredicateType type,
            String constantName) {

        try {
            double value = (double) endPredicatesMap.get(constantName);
            endPredicates.add(
                    new EndPredicate(type, value, generationsStride)
            );
        } catch (Throwable e) {
            throw new ValidationException("Invalid predicate parameter. " + e.getMessage());
        }
    }

    private boolean isXMLLoaded() {
        return descriptor != null;
    }
}
