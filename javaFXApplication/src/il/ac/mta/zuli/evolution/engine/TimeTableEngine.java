package il.ac.mta.zuli.evolution.engine;

import il.ac.mta.zuli.evolution.dto.*;
import il.ac.mta.zuli.evolution.engine.events.ErrorEvent;
import il.ac.mta.zuli.evolution.engine.events.ErrorType;
import il.ac.mta.zuli.evolution.engine.events.EventsEmitter;
import il.ac.mta.zuli.evolution.engine.evolutionengine.EngineSettings;
import il.ac.mta.zuli.evolution.engine.evolutionengine.crossover.CrossoverInterface;
import il.ac.mta.zuli.evolution.engine.evolutionengine.mutation.Mutation;
import il.ac.mta.zuli.evolution.engine.evolutionengine.selection.Selection;
import il.ac.mta.zuli.evolution.engine.exceptions.InvalidOperationException;
import il.ac.mta.zuli.evolution.engine.predicates.EndPredicate;
import il.ac.mta.zuli.evolution.engine.rules.Rule;
import il.ac.mta.zuli.evolution.engine.tasks.LoadXMLTask;
import il.ac.mta.zuli.evolution.engine.tasks.RunAlgorithmTask;
import il.ac.mta.zuli.evolution.engine.timetable.Requirement;
import il.ac.mta.zuli.evolution.engine.timetable.SchoolClass;
import il.ac.mta.zuli.evolution.engine.timetable.Subject;
import il.ac.mta.zuli.evolution.engine.timetable.Teacher;
import il.ac.mta.zuli.evolution.ui.header.HeaderController;
import javafx.concurrent.Task;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Consumer;

public class TimeTableEngine extends EventsEmitter implements Engine {
    private Descriptor descriptor;
    private EvolutionState currEvolutionState;
    private HeaderController controller;
    private Task<?> currentRunningTask;

    public TimeTableEngine() {
    }

    public void setController(HeaderController controller) {
        this.controller = controller;
    }

    @Override
    public void loadXML(String fileToLoad,
                        Consumer<DescriptorDTO> onSuccess,
                        Consumer<Throwable> onFailure) {

        //TODO delete before submitting
        if (currentRunningTask != null) {
            System.out.println(" currentRunningTask != null, do we ever reach this line");
        }

        currentRunningTask = new LoadXMLTask(fileToLoad);

        currentRunningTask.setOnSucceeded(value -> {
            controller.onTaskFinished();
            this.descriptor = (Descriptor) currentRunningTask.getValue();
            DescriptorDTO descDTO = createDescriptorDTO();
            onSuccess.accept(descDTO); //sending the descriptorDTO to the controller
            currentRunningTask = null; // clearing task
        });

        currentRunningTask.setOnFailed(value -> {
            controller.onTaskFinished();
            //TODO figure out how to handle exceptions, with reaching the "root" error as we did in the console
            onFailure.accept(currentRunningTask.getException());
            currentRunningTask = null;
        });

        controller.bindTaskToUIComponents(currentRunningTask);

        new Thread(currentRunningTask).start();
    }

//    Because the loadXML returned a descriptorDTO to the controller,
//    no need for "DescriptorDTO getSystemDetails()" as an engine method

    @Override
    public void startEvolutionAlgorithm(List<EndPredicate> endConditions,
                                        int generationsStride,
                                        Consumer<Boolean> onSuccess,
                                        Consumer<Throwable> onFailure,
                                        Consumer<TimeTableSolutionDTO> reportBestSolution) {

        checkForErrorsBeforeExecutingAlgorithms();

        if (currentRunningTask != null) {
            System.out.println("currentRunningTask isn't null");
            return; // TODO: should not come here, we want one task at a time
        }

//        public RunAlgorithmTask(Descriptor descriptor, List < EndPredicate > endPredicates,
//        int generationsStride, EvolutionState initialEvolutionState, Consumer < EvolutionState > reportState,
//        Consumer < TimeTableSolution > reportBestSolution)
        currentRunningTask = new RunAlgorithmTask(
                this.descriptor,
                endConditions,
                generationsStride,
                null,
                (EvolutionState state) -> {
                    this.currEvolutionState = state;
                },
                (TimeTableSolution solution) -> {
                    reportBestSolution.accept(createTimeTableSolutionDTO(solution));
                });

        callingTask(onSuccess, onFailure);
    }

    @Override
    public void resume(List<EndPredicate> endConditions,
                       int generationsStride,
                       Consumer<Boolean> onSuccess,
                       Consumer<Throwable> onFailure,
                       Consumer<TimeTableSolutionDTO> reportBestSolution) {

        if (currentRunningTask != null) {
            System.out.println("currentRunningTask isn't null");
            return; // TODO: should not come here, we want one task at a time
        }

        currentRunningTask = new RunAlgorithmTask(
                this.descriptor,
                endConditions,
                generationsStride,
                this.currEvolutionState,
                (EvolutionState state) -> {
                    this.currEvolutionState = state;
                },
                (TimeTableSolution solution) -> {
                    reportBestSolution.accept(createTimeTableSolutionDTO(solution));
                });

        callingTask(onSuccess, onFailure);
    }

    private void callingTask(Consumer<Boolean> onSuccess, Consumer<Throwable> onFailure) {
        currentRunningTask.setOnCancelled(event -> {
            controller.onTaskFinished();
            onSuccess.accept(false);
            currentRunningTask = null; // clearing task
        });

        currentRunningTask.setOnSucceeded(event -> {
            this.currEvolutionState = null; // reset state
            controller.onTaskFinished();
            onSuccess.accept(true); // sending the best solutionDTO to the controller
            currentRunningTask = null; // clearing task
        });

        currentRunningTask.setOnFailed(value -> {
            controller.onTaskFinished();
            //TODO figure out how to handle exceptions, with reaching the "root" error as we did in the console
            onFailure.accept(currentRunningTask.getException());
            currentRunningTask = null;
        });

        controller.bindTaskToUIComponents(currentRunningTask);

        new Thread(currentRunningTask).start();
    }

    @Override
    public void pause() {
        currentRunningTask.cancel();
    }

    @Override
    public void stop() {
        currentRunningTask.cancel();
        this.currEvolutionState = null; //in case of STOP we don't want to save the previous state}
    }

    @Override
    public EngineSettings getEngineSettings() {
        return descriptor.getEngineSettings();
    }

    @Override
    public void setEngineSettings(EngineSettings validatedSettings) {
        this.descriptor.setValidatedEngineSettings(validatedSettings);
    }

    @Override
    public TimeTableSolutionDTO getBestSolution() {
        return null;
    }

    @Override
    public List<GenerationProgressDTO> getEvolutionProgress() {
//        if (!isXMLLoaded()) {
//            ErrorEvent e = new ErrorEvent("Failed getting progress history",
//                    ErrorType.ProgressHistoryError,
//                    new InvalidOperationException("Can't show evolution progress, file is not loaded"));
//            fireEvent("error", e);
//            return null;
//        }
//
//        if (bestSolutionsInGenerationPerStride.size() == 0) {
//            ErrorEvent e = new ErrorEvent("Failed getting evolution-progress history",
//                    ErrorType.ProgressHistoryError,
//                    new InvalidOperationException("Can't show evolution progress, evolution algorithm has not been executed"));
//            fireEvent("error", e);
//            return null;
//        }
//
//        try {
//            List<GenerationProgressDTO> progress = new ArrayList<>();
//            //we know generation # 1 is the first in the map because it's a treeMap
//            double previousScore = bestSolutionsInGenerationPerStride.get(1).getTotalFitnessScore();
//            double delta;
//            int generation;
//
//            for (Map.Entry<Integer, TimeTableSolution> entry : bestSolutionsInGenerationPerStride.entrySet()) {
//                TimeTableSolutionDTO solutionDTO = createTimeTableSolutionDTO(entry.getValue());
//                generation = entry.getKey();
//                double currScore = entry.getValue().getTotalFitnessScore();
//                delta = currScore - previousScore;
//                previousScore = currScore;
//                progress.add(new GenerationProgressDTO(generation, solutionDTO, delta));
//            }
//
//            return progress;
//        } catch (Throwable e) {
//            fireEvent("error", new ErrorEvent("Failed getting evolution-progress history",
//                    ErrorType.ProgressHistoryError, e));
//            return null;
//        }
        return null;
    }
    //#endregion

    //#region auxiliary methods
    private void checkForErrorsBeforeExecutingAlgorithms() {
        if (!isXMLLoaded()) {
            ErrorEvent e = new ErrorEvent("Failed running evolution algorithm",
                    ErrorType.RunError,
                    new InvalidOperationException("Can't execute Evolution algorithm, file is not loaded"));
            fireEvent("error", e);
        }
    }

    @Override
    public boolean isXMLLoaded() {
        return descriptor != null;
    }

    //#region DTO-related methods
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

        return new TimeTableSolutionDTO(quintets, solution.getSolutionSize(), solution.getTotalFitnessScore(),
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
