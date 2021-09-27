package il.ac.mta.zuli.evolution.engine;

import il.ac.mta.zuli.evolution.dto.*;
import il.ac.mta.zuli.evolution.engine.evolutionengine.EngineSettings;
import il.ac.mta.zuli.evolution.engine.evolutionengine.crossover.CrossoverInterface;
import il.ac.mta.zuli.evolution.engine.evolutionengine.mutation.Mutation;
import il.ac.mta.zuli.evolution.engine.evolutionengine.selection.Selection;
import il.ac.mta.zuli.evolution.engine.exceptions.InvalidOperationException;
import il.ac.mta.zuli.evolution.engine.exceptions.ValidationException;
import il.ac.mta.zuli.evolution.engine.predicates.EndPredicate;
import il.ac.mta.zuli.evolution.engine.rules.Rule;
import il.ac.mta.zuli.evolution.engine.tasks.RunAlgorithmTask;
import il.ac.mta.zuli.evolution.engine.timetable.*;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.concurrent.Task;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class TimeTableEngine implements Engine {
    private final Descriptor descriptor;
    private EvolutionState currEvolutionState;
    private Task<?> currentRunningTask; //OR maybe: private Thread thread;
    //EX3 additions to class:
    private final List<EndPredicate> endPredicates;
    private int generationsStride;
    private TimeTableSolution bestSolution;

    //TODO get rid of properties and controller
    private SimpleIntegerProperty generationNumProperty;
    private SimpleDoubleProperty fitnessProperty;
    private SimpleLongProperty timeProperty;

    //new CTOR for Ex 3 - where was the descriptor set in the previous exercises?
    public TimeTableEngine(TimeTable timetable,
                           Map<String, Object> engineSettingsMap,
                           List<Map<String, Object>> endPredicatesMap,
                           Object stride) {
        EngineSettings<TimeTableSolution> engineSettings = new EngineSettings<>(engineSettingsMap, timetable);
        this.descriptor = new Descriptor(timetable, engineSettings);
        this.endPredicates = generatePredicates(endPredicatesMap);
        setGenerationsStride(stride);
    }

    public void setGenerationsStride(Object generationsStride) {
        int stride;

        try {
            stride = (int) generationsStride;
        } catch (Throwable e) {
            throw new ValidationException("Stride must be a positive number");
        }

        if (stride > 0) {
            this.generationsStride = stride; //we'll check stride < generationNum only if that predicate is applied
        } else {
            throw new ValidationException("Stride must be a positive number");
        }
    }

    private List<EndPredicate> generatePredicates(List<Map<String, Object>> endPredicatesMap) {
        List<EndPredicate> endPredicates = new ArrayList<>();

        if (endPredicatesMap.size() > 0) {
            for (Map<String, Object> predicateMap : endPredicatesMap) {
                endPredicates.add(new EndPredicate(predicateMap, generationsStride));
            }

            return endPredicates;
        } else {
            throw new ValidationException("Please select at least one End Condition");
        }
    }

    public boolean isXMLLoaded() {
        return descriptor != null;
    }

    //#region algorithm-flow methods:
    @Override
    public void startEvolutionAlgorithm() {
        if (!isXMLLoaded()) {
            throw new InvalidOperationException("Can not execute Evolution Algorithm, a file is not loaded");
        }

        runEvolutionAlgorithm(null); // sending null as currentState, to start a fresh run
    }

    @Override
    public void resumeEvolutionAlgorithm() {
        runEvolutionAlgorithm(this.currEvolutionState);
    }

    private void runEvolutionAlgorithm(EvolutionState currentState) {
        if (currentRunningTask != null) {
            throw new RuntimeException("Failed running task because another task is currently running");
        }

        currentRunningTask = new RunAlgorithmTask(
                this.descriptor,
                endPredicates,
                generationsStride,
                currentState,
                (EvolutionState state) -> {
                    this.currEvolutionState = state;
                },
                (TimeTableSolution solution) -> {
                    bestSolution = solution;
                });
        currentRunningTask.setOnCancelled(event -> {
            //onSuccess.accept(false);
            currentRunningTask = null; // clearing task
        });

        currentRunningTask.setOnSucceeded(event -> {
            this.currEvolutionState = null; // reset state
//            onSuccess.accept(true); // sending the best solutionDTO to the controller
            currentRunningTask = null; // clearing task
        });

        currentRunningTask.setOnFailed(value -> {
//            onFailure.accept(currentRunningTask.getException());
            currentRunningTask = null;
        });

        new Thread(currentRunningTask).start();
    }

    @Override
    public void pauseEvolutionAlgorithm() {
        currentRunningTask.cancel();
    }

    @Override
    public void stopEvolutionAlgorithm() {
        currentRunningTask.cancel();
        this.currEvolutionState = null; //in case of STOP we don't want to save the previous state}
    }
//#endregion

    //#region setters:
    @Override
    public void setValidatedEngineSettings(EngineSettings validatedSettings) {
        this.descriptor.setEngineSettings(validatedSettings);
    }

    public void setCurrEvolutionState(EvolutionState currEvolutionState) {
        this.currEvolutionState = currEvolutionState;
    }

    public void setCurrentRunningTask(Task<?> currentRunningTask) {
        this.currentRunningTask = currentRunningTask;
    }
    //#endregion

    //#region getters:
    @Override
    public EngineSettings getEngineSettings() {
        return descriptor.getEngineSettings();
    }

    @Override
    public TimeTable getTimeTable() {
        return descriptor.getTimeTable();
    }

    public Descriptor getDescriptor() {
        return descriptor;
    }

    public Integer getTimetableID() {
        return descriptor.getTimetableID();
    }

    public EvolutionState getCurrEvolutionState() {
        return currEvolutionState;
    }

    public List<EndPredicate> getEndPredicates() {
        return endPredicates;
    }

    public int getGenerationsStride() {
        return generationsStride;
    }

    public TimeTableSolution getBestSolution() {
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

    //#region DTO-related methods
    @Override
    public DescriptorDTO getDescriptorDTO() {
        //TODO change to return Descriptor or maybe delete
        return createDescriptorDTO();
    }

    private EngineSettingsDTO createEngineSettingsDTO() {
        int initialSize = descriptor.getEngineSettings().getInitialPopulationSize();
        SelectionDTO selectionDTO = createSelectionDTO();
        CrossoverDTO crossoverDTO = createCrossoverDTO();
        List<MutationDTO> mutationDTOS = createMutationDTOList();

        return new EngineSettingsDTO(initialSize, selectionDTO, crossoverDTO, mutationDTOS);
    }

    private List<MutationDTO> createMutationDTOList() {
        List<MutationDTO> mutationDTOS = new ArrayList<>();

        for (Object mutation : descriptor.getEngineSettings().getMutations()) {
            //it's safe to cast to Mutation since we're receiving data from within the engineSettings
            Mutation currMutation = (Mutation) mutation;
            String name = currMutation.getClass().getSimpleName();
            double probability = currMutation.getProbability();
            String configuration = currMutation.getConfiguration();
            mutationDTOS.add(new MutationDTO(name, probability, configuration));
        }

        return mutationDTOS;
    }

    @NotNull
    private CrossoverDTO createCrossoverDTO() {
        CrossoverInterface<TimeTableSolution> crossover = descriptor.getEngineSettings().getCrossover();

        return new CrossoverDTO(crossover.getClass().getSimpleName(), crossover.getNumOfCuttingPoints());
    }

    private DescriptorDTO createDescriptorDTO() {
        TimeTableDTO timeTableDTO = createTimeTableDTO();
        EngineSettingsDTO engineSettingsDTO = createEngineSettingsDTO();

        return new DescriptorDTO(timeTableDTO, engineSettingsDTO);
    }

    @NotNull
    private SelectionDTO createSelectionDTO() {
        Selection<TimeTableSolution> selection = descriptor.getEngineSettings().getSelection();

        return new SelectionDTO(selection);
    }

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

    private TimeTableSolutionDTO createTimeTableSolutionDTO(@NotNull TimeTableSolution solution) {
        List<QuintetDTO> quintets = createQuintetDTOList(solution.getSolutionQuintets());
        Map<RuleDTO, Double> fitnessScorePerRuleDTO = new HashMap<>();

        for (Map.Entry<Rule, Double> entry : solution.getFitnessScorePerRule().entrySet()) {
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
