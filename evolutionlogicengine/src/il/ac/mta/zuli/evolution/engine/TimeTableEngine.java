package il.ac.mta.zuli.evolution.engine;

import il.ac.mta.zuli.evolution.Constants;
import il.ac.mta.zuli.evolution.dto.*;
import il.ac.mta.zuli.evolution.engine.evolutionengine.EngineSettings;
import il.ac.mta.zuli.evolution.engine.exceptions.InvalidOperationException;
import il.ac.mta.zuli.evolution.engine.exceptions.ValidationException;
import il.ac.mta.zuli.evolution.engine.predicates.EndPredicate;
import il.ac.mta.zuli.evolution.engine.predicates.EndPredicateType;
import il.ac.mta.zuli.evolution.engine.rules.Rule;
import il.ac.mta.zuli.evolution.engine.tasks.RunAlgorithmTask;
import il.ac.mta.zuli.evolution.engine.timetable.*;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class TimeTableEngine implements Engine {
    private final Descriptor descriptor;
    private EvolutionState currEvolutionState; // includes taskDone flag and exception-from-task fields
    private RunAlgorithmTask currentRunningTask; //in Ex2 this field was Task<?>
    //EX3 additions to class:
    private List<EndPredicate> endPredicates;
    private int generationsStride;
    private TimetableSolution bestSolution;
    private StrideData strideData; //so the UI can poll the GenerationNum and BestScore in that Generation

    //new CTOR for Ex 3
    public TimeTableEngine(TimeTable timetable,
                           Map<String, Object> engineSettingsMap,
                           Map<String, Object> endPredicatesMap,
                           Object stride) {

        EngineSettings<TimetableSolution> engineSettings = new EngineSettings<>(engineSettingsMap, timetable);
        this.descriptor = new Descriptor(timetable, engineSettings);
        setGenerationsStride(stride);
        this.endPredicates = generatePredicates(endPredicatesMap);
    }

    //#region algorithm-flow methods:
    @Override
    public void startEvolutionAlgorithm() {
        if (!isXMLLoaded()) {
            throw new InvalidOperationException("Can not execute Evolution Algorithm, a file is not loaded");
        }

        //if already running we do NOT want to start fresh TODO check isTaskDone()?
        runEvolutionAlgorithm(null); // sending null as currentState, to start a fresh run
    }

    @Override
    public void resumeEvolutionAlgorithm() {
        //will likely NOT throw exceptions, since we'll disable buttons accordingly in FrontEnd
        if (LogicalRunStatus.PAUSED == currEvolutionState.getStatus()) {
            runEvolutionAlgorithm(this.currEvolutionState);
        } else if (LogicalRunStatus.STOPPED == currEvolutionState.getStatus()) {
            throw new InvalidOperationException("Run was stopped, cannot resume");
        } else if (!currEvolutionState.isTaskDone()) {
            //do nothing (it's already running)
        } else {
            //if task completed (either successfully or with exception)
            throw new InvalidOperationException("Run cannot resume");
        }
    }

    private void runEvolutionAlgorithm(EvolutionState currentState) {
//        if (currentRunningTask != null) {
//            throw new RuntimeException("Failed running task because another task is currently running");
//        }

        RunAlgorithmTask currentRunningTask = new RunAlgorithmTask(
                this.descriptor,
                endPredicates,
                generationsStride,
                currentState,
                (EvolutionState state) -> {
                    if (state.isTaskDone() &&
                            state.getStatus() != LogicalRunStatus.STOPPED &&
                            state.getStatus() != LogicalRunStatus.PAUSED) {
                        state.setStatus(LogicalRunStatus.COMPLETED);
                    }
                    this.currEvolutionState = state;
                },
                (TimetableSolution solution) -> bestSolution = solution,
                (StrideData data) -> strideData = data);

//        currentRunningTask.setOnCancelled(event -> {
//            //onSuccess.accept(false);
//            currentRunningTask = null; // clearing task - no need to clear task in Ex3
//        });//
//        currentRunningTask.setOnSucceeded(event -> {
//            this.currEvolutionState = null; // reset state
////            onSuccess.accept(true); // sending the best solutionDTO to the controller
//            currentRunningTask = null; // clearing task
//        });//
//        currentRunningTask.setOnFailed(value -> {
//            System.out.println("********setOnFailed");
//            System.out.println(currentRunningTask.getException());
//            currentRunningTask = null;
//        });


        new Thread(currentRunningTask, "EvolutionAlgorithmThread").start();
    }

    @Override
    public void pauseEvolutionAlgorithm() {
        if (!currEvolutionState.isTaskDone()) {
            //if currently running
            currentRunningTask.cancel();
            currEvolutionState.setStatus(LogicalRunStatus.PAUSED);
        } else {
            throw new InvalidOperationException("Nothing to pause here");
        }
    }

    @Override
    public void stopEvolutionAlgorithm() {
        if (!currEvolutionState.isTaskDone()) {
            //if currently running
            currentRunningTask.cancel();
            currEvolutionState.setStatus(LogicalRunStatus.STOPPED);
        } else if (LogicalRunStatus.PAUSED == currEvolutionState.getStatus()) {
            currEvolutionState.setStatus(LogicalRunStatus.STOPPED);
        }
        // no need for this in Ex 3 because each timetableEngine only runs for a single timeTable
//        this.currEvolutionState = null; //in case of STOP we don't want to save the previous state
    }

//#endregion

    //#region getters:
    public Integer getTimetableID() {
        return descriptor.getTimetableID();
    }

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
        if(currEvolutionState!=null) {
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
    //#endregion

    public void setNewAlgorithmConfiguration(
            Map<String, Object> engineSettingsMap,
            Map<String, Object> endPredicatesMap,
            Object generationStride) {
        descriptor.setEngineSettings(new EngineSettings<>(engineSettingsMap, descriptor.getTimeTable()));
        setGenerationsStride(generationStride);
        endPredicates = generatePredicates(endPredicatesMap);
    }

    private void setGenerationsStride(Object generationsStride) {
        int stride;

        try {
            stride = Integer.parseInt((String) generationsStride);
        } catch (Throwable e) {
            throw new ValidationException("Stride must be a positive number");
        }

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
            double value = java.lang.Double.parseDouble((String) endPredicatesMap.get(constantName));
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

    //#region DTO-related methods
    @NotNull
    private TimeTableDTO createTimeTableDTO() {
        Map<Integer, SubjectDTO> subjectsDTO = createSortedSubjectDTOCollection(descriptor.getTimeTable().getSubjects());
        Map<Integer, TeacherDTO> teachersDTO = createSortedTeacherDTOCollection();
        Map<Integer, SchoolClassDTO> schoolClassesDTO = createSortedClassesDTOCollection();
        Set<RuleDTO> rulesDTO = createRulesDTOSet();

        return new TimeTableDTO(descriptor.getTimeTable().getDays(),
                descriptor.getTimeTable().getHours(),
                subjectsDTO, teachersDTO, schoolClassesDTO, rulesDTO);
    }

    private Set<RuleDTO> createRulesDTOSet() {
        Set<Rule> rules = descriptor.getTimeTable().getRules();
        Set<RuleDTO> rulesDTO = new HashSet<>();

        for (Rule rule : rules) {
            rulesDTO.add(
                    new RuleDTO(
                            rule.getClass().getSimpleName(),
                            rule.getRuleType().toString(),
                            rule.getParams()
                    ));
        }

        return rulesDTO;
    }

    private Map<Integer, SubjectDTO> createSortedSubjectDTOCollection(Map<Integer, Subject> subjects) {
        Map<Integer, SubjectDTO> subjectDTOS = new TreeMap<>();

        for (Subject subject : subjects.values()) {
            subjectDTOS.put(subject.getId(), new SubjectDTO(subject.getId(), subject.getName()));
        }

        return subjectDTOS; //in sorted order because of TreeMap
    }

    private QuintetDTO createQuintetDTO(Quintet quintet) {
        TeacherDTO teacherDTO = createTeacherDTO(quintet.getTeacher());
        SchoolClassDTO schoolClassDTO = createSchoolClassDTO(quintet.getSchoolClass());
        SubjectDTO subjectDTO = new SubjectDTO(quintet.getSubjectID(), quintet.getSubject().getName());

        return new QuintetDTO(quintet.getDay(), quintet.getHour(), teacherDTO, schoolClassDTO, subjectDTO);
    }

    private List<QuintetDTO> createQuintetDTOList(List<Quintet> quintets) {
        List<QuintetDTO> quintetDTOList = new ArrayList<>();

        for (Quintet quintet : quintets) {
            quintetDTOList.add(createQuintetDTO(quintet));
        }

        return quintetDTOList;
    }

    private TimeTableSolutionDTO createTimeTableSolutionDTO(@NotNull TimetableSolution solution) {
        List<QuintetDTO> quintets = createQuintetDTOList(solution.getSolutionQuintets());
        Map<RuleDTO, java.lang.Double> fitnessScorePerRuleDTO = new HashMap<>();

        for (Map.Entry<Rule, java.lang.Double> entry : solution.getFitnessScorePerRule().entrySet()) {
            Rule rule = entry.getKey();
            fitnessScorePerRuleDTO.put(
                    new RuleDTO(rule.getClass().getSimpleName(), rule.getRuleType().toString(), rule.getParams()),
                    entry.getValue()
            );
        }

        return new TimeTableSolutionDTO(quintets, solution.getSolutionSize(), solution.getFitnessScore(),
                fitnessScorePerRuleDTO, createTimeTableDTO());
    }

    private TeacherDTO createTeacherDTO(Teacher teacher) {
        Map<Integer, SubjectDTO> subjectsDTO = createSortedSubjectDTOCollection(teacher.getSubjects());

        return new TeacherDTO(teacher.getId(), teacher.getName(), subjectsDTO);
    }

    private Map<Integer, TeacherDTO> createSortedTeacherDTOCollection() {
        Map<Integer, TeacherDTO> teacherDTOs = new TreeMap<>();
        Map<Integer, Teacher> teachers = descriptor.getTimeTable().getTeachers();

        for (Teacher teacher : teachers.values()) {
            teacherDTOs.put(teacher.getId(), createTeacherDTO(teacher));
        }

        return teacherDTOs; //in sorted order because of TreeMap
    }

    private SchoolClassDTO createSchoolClassDTO(SchoolClass schoolClass) {
        List<RequirementDTO> requirementsDTO = createRequirementsDTOList(schoolClass.getRequirements());

        return new SchoolClassDTO(schoolClass.getId(), schoolClass.getName(), requirementsDTO);
    }

    private Map<Integer, SchoolClassDTO> createSortedClassesDTOCollection() {
        Map<Integer, SchoolClassDTO> SchoolClassDTOs = new TreeMap<>();
        Map<Integer, SchoolClass> SchoolClass = descriptor.getTimeTable().getSchoolClasses();

        for (SchoolClass schoolClass : SchoolClass.values()) {
            SchoolClassDTOs.put(schoolClass.getId(), createSchoolClassDTO(schoolClass));
        }

        return SchoolClassDTOs; //in sorted order because of TreeMap
    }

    private List<RequirementDTO> createRequirementsDTOList(List<Requirement> requirements) {
        List<RequirementDTO> requirementDTOs = new ArrayList<>();
        SubjectDTO subjectDTO;

        for (Requirement requirement : requirements) {
            subjectDTO = new SubjectDTO(requirement.getSubject().getId(), requirement.getSubject().getName());
            requirementDTOs.add(new RequirementDTO(requirement.getHours(), subjectDTO));
        }

        return requirementDTOs;
    }
    //#endregion
}
