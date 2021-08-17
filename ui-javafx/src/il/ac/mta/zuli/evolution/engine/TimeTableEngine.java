package il.ac.mta.zuli.evolution.engine;

import com.sun.istack.internal.NotNull;
import il.ac.mta.zuli.evolution.dto.*;
import il.ac.mta.zuli.evolution.engine.events.*;
import il.ac.mta.zuli.evolution.engine.evolutionengine.EvolutionEngine;
import il.ac.mta.zuli.evolution.engine.evolutionengine.crossover.Crossover;
import il.ac.mta.zuli.evolution.engine.evolutionengine.mutation.Mutation;
import il.ac.mta.zuli.evolution.engine.evolutionengine.selection.Selection;
import il.ac.mta.zuli.evolution.engine.exceptions.InvalidOperationException;
import il.ac.mta.zuli.evolution.engine.exceptions.ValidationException;
import il.ac.mta.zuli.evolution.engine.rules.Rule;
import il.ac.mta.zuli.evolution.engine.timetable.Requirement;
import il.ac.mta.zuli.evolution.engine.timetable.SchoolClass;
import il.ac.mta.zuli.evolution.engine.timetable.Subject;
import il.ac.mta.zuli.evolution.engine.timetable.Teacher;
import il.ac.mta.zuli.evolution.engine.xmlparser.XMLParser;

import java.util.*;
import java.util.stream.Collectors;

public class TimeTableEngine extends EventsEmitter implements Engine {
    private Descriptor descriptor;
    private EvolutionEngine<TimeTableSolution> evolutionEngine;
    private TimeTableSolution bestSolutionEver = null;
    private Map<Integer, TimeTableSolution> bestSolutionsInGenerationPerStride; // generation , solution

    //#region ui-parallel methods
    @Override
    public void loadXML(@NotNull String path) {
        try {
            XMLParser xmlParser = new XMLParser();
            descriptor = xmlParser.unmarshall(path);
            fireEvent("loaded", new LoadedEvent("File was loaded", path));
        } catch (
                Throwable e) {
            fireEvent("error", new ErrorEvent("Failed reading file", ErrorType.LoadError, e));
        }
    }

    @Override
    public DescriptorDTO getSystemDetails() {
        if (!isXMLLoaded()) {
            ErrorEvent e = new ErrorEvent("Failed getting the system settings", ErrorType.DetailsError, new InvalidOperationException("can't get system details, file is not loaded"));
            fireEvent("error", e);
            return null;
        }

        try {
            //DTO: list of subjects, list of teachers, list of SchoolClasses, list of rules
            TimeTableDTO timeTableDTO = createTimeTableDTO();
            EngineSettingsDTO engineSettingsDTO = createEngineSettingsDTO();
            return new DescriptorDTO(timeTableDTO, engineSettingsDTO);
        } catch (Throwable e) {
            fireEvent("error", new ErrorEvent("failed getting system details", ErrorType.DetailsError, e));
            return null;
        }
    }

    @Override
    public void executeEvolutionAlgorithm(int numOfGenerations, int generationsStride) {
        checkForErrorsBeforeExecutingAlgorithms(numOfGenerations, generationsStride);
        try {
            bestSolutionsInGenerationPerStride = new TreeMap<>();
            List<TimeTableSolution> initialPopulation = getInitialGeneration();

            evolutionEngine = new EvolutionEngine(this.descriptor.getEngineSettings(),
                    this.descriptor.getTimeTable().getRules());

            List<TimeTableSolution> prevGeneration = initialPopulation;
            List<TimeTableSolution> currGeneration;
            double bestSolutionFitnessScore = 0;

            for (int i = 1; i <= numOfGenerations; i++) {
                currGeneration = evolutionEngine.execute(prevGeneration);
                TimeTableSolution currBestSolution = currGeneration.stream().
                        sorted(Collections.reverseOrder()).limit(1).collect(Collectors.toList()).get(0);

                if (bestSolutionFitnessScore < currBestSolution.getTotalFitnessScore()) {
                    bestSolutionEver = currBestSolution;
                    bestSolutionFitnessScore = bestSolutionEver.getTotalFitnessScore();
                }

                //stride for purposes of info-display and to save a stride-generation history
                //with addition of first and last generation
                if (i == 1 || (i % generationsStride == 0) || (i == numOfGenerations)) {
                    bestSolutionsInGenerationPerStride.put(i, currBestSolution);
                    fireStrideDetails(i, currBestSolution);
                }

                prevGeneration = currGeneration;
            }

            fireEvent("completed", new Event("Evolution algorithm finished running."));
        } catch (Throwable e) {
            fireEvent("error", new ErrorEvent("Failed running evolution algorithm", ErrorType.RunError, e));
        }
    }

    //bonus
    @Override
    public void executeEvolutionAlgorithmWithFitnessStop(int numOfGenerations, int generationsStride, double fitnessStop) {
        checkForErrorsBeforeExecutingAlgorithms(numOfGenerations, generationsStride);
        try {
            bestSolutionsInGenerationPerStride = new TreeMap<>();
            bestSolutionsInGenerationPerStride = new HashMap<>(numOfGenerations);
            List<TimeTableSolution> initialPopulation = getInitialGeneration();

            evolutionEngine = new EvolutionEngine(this.descriptor.getEngineSettings(),
                    this.descriptor.getTimeTable().getRules());

            List<TimeTableSolution> prevGeneration = initialPopulation;
            List<TimeTableSolution> currGeneration;
            double bestSolutionFitnessScore = 0;

            for (int i = 1; i <= numOfGenerations; i++) {
                currGeneration = evolutionEngine.execute(prevGeneration);
                TimeTableSolution currBestSolution = currGeneration.stream().
                        sorted(Collections.reverseOrder()).limit(1).collect(Collectors.toList()).get(0);

                if (bestSolutionFitnessScore < currBestSolution.getTotalFitnessScore()) {
                    bestSolutionEver = currBestSolution;
                    bestSolutionFitnessScore = bestSolutionEver.getTotalFitnessScore();
                }

                //stride for purposes of info-display and to save a stride-generation history
                if (i == 1 || (i % generationsStride == 0) || (i == numOfGenerations)) {
                    bestSolutionsInGenerationPerStride.put(i, currBestSolution);
                    fireStrideDetails(i, currBestSolution);
                }
                if (bestSolutionFitnessScore >= fitnessStop) {
                    break;
                }
                prevGeneration = currGeneration;
            }

            fireEvent("completed", new Event("Evolution algorithm finished running."));
        } catch (Throwable e) {
            fireEvent("error", new ErrorEvent("Failed running evolution algorithm", ErrorType.RunError, e));
        }
    }

    @Override
    public TimeTableSolutionDTO getBestSolution() {
        return createTimeTableSolutionDTO(bestSolutionEver);
    }

    @Override
    public List<GenerationProgressDTO> getEvolutionProgress() {
        if (!isXMLLoaded()) {
            ErrorEvent e = new ErrorEvent("Failed getting progress history",
                    ErrorType.ProgressHistoryError,
                    new InvalidOperationException("Can't show evolution progress, file is not loaded"));
            fireEvent("error", e);
            return null;
        }

        if (bestSolutionsInGenerationPerStride.size() == 0) {
            ErrorEvent e = new ErrorEvent("Failed getting evolution-progress history",
                    ErrorType.ProgressHistoryError,
                    new InvalidOperationException("Can't show evolution progress, evolution algorithm has not been executed"));
            fireEvent("error", e);
            return null;
        }

        try {
            List<GenerationProgressDTO> progress = new ArrayList<>();
            //we know generation # 1 is the first in the map because it's a treeMap
            double previousScore = bestSolutionsInGenerationPerStride.get(1).getTotalFitnessScore();
            double delta;
            int generation;

            for (Map.Entry<Integer, TimeTableSolution> entry : bestSolutionsInGenerationPerStride.entrySet()) {
                TimeTableSolutionDTO solutionDTO = createTimeTableSolutionDTO(entry.getValue());
                generation = entry.getKey();
                double currScore = entry.getValue().getTotalFitnessScore();
                delta = currScore - previousScore;
                previousScore = currScore;
                progress.add(new GenerationProgressDTO(generation, solutionDTO, delta));
            }

            return progress;
        } catch (Throwable e) {
            fireEvent("error", new ErrorEvent("Failed getting evolution-progress history",
                    ErrorType.ProgressHistoryError, e));
            return null;
        }
    }
    //#endregion

    //#region auxiliary methods
    private void fireStrideDetails(int i, TimeTableSolution currBestSolution) {
        GenerationStrideScoreDTO strideScoreDTO = new GenerationStrideScoreDTO(i, currBestSolution.getTotalFitnessScore());
        fireEvent("stride", new OnStrideEvent("generation ", i, strideScoreDTO));
    }

    private void checkForErrorsBeforeExecutingAlgorithms(int numOfGenerations, int generationsStride) {
        if (!isXMLLoaded()) {
            ErrorEvent e = new ErrorEvent("Failed running evolution algorithm",
                    ErrorType.RunError,
                    new InvalidOperationException("Can't execute Evolution algorithm, file is not loaded"));
            fireEvent("error", e);
        }

        if (numOfGenerations < 0) {
            ErrorEvent e = new ErrorEvent("Failed running evolution algorithm",
                    ErrorType.RunError,
                    new ValidationException(numOfGenerations + " is invalid number for generations, must be positive number"));
            fireEvent("error", e);
        }
        if (generationsStride < 0 || generationsStride > numOfGenerations) {
            ErrorEvent e = new ErrorEvent("Failed running evolution algorithm",
                    ErrorType.RunError,
                    new ValidationException(numOfGenerations + " is invalid number for generation strides, must be between 1 - " + numOfGenerations));
            fireEvent("error", e);
        }
    }

    public int getTimeTableHours() {
        return descriptor.getTimeTableHours();
    }

    public int getTimeTableDays() {
        return descriptor.getTimeTableDays();
    }

    @NotNull
    private List<TimeTableSolution> getInitialGeneration() {
        int initialPopulationSize = descriptor.getEngineSettings().getInitialPopulationSize();
        List<TimeTableSolution> initialPopulation = new ArrayList<>();

        for (int i = 0; i < initialPopulationSize; i++) {
            initialPopulation.add(new TimeTableSolution(descriptor.getTimeTable()));
        }

        return initialPopulation;
    }

    @Override
    public boolean isXMLLoaded() {
        return descriptor != null;
    }
//#endregion


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
        Crossover<TimeTableSolution> crossover = descriptor.getEngineSettings().getCrossover();

        return new CrossoverDTO(crossover.getClass().getSimpleName(), crossover.getNumOfCuttingPoints());
    }

    @NotNull
    private SelectionDTO createSelectionDTO() {
        Selection<TimeTableSolution> selection = descriptor.getEngineSettings().getSelection();

        return new SelectionDTO(selection.getClass().getSimpleName(), selection.getConfiguration());
    }

    @NotNull
    private TimeTableDTO createTimeTableDTO() {
        Map<Integer, SubjectDTO> subjectsDTO = createSortedSubjectDTOCollection(descriptor.getTimeTable().getSubjects());
        Map<Integer, TeacherDTO> teachersDTO = createSortedTeacherDTOCollection();
        Map<Integer, SchoolClassDTO> schoolClassesDTO = createSortedClassesDTOCollection();
        Set<RuleDTO> rulesDTO = createRulesDTOSet();
        TimeTableDTO timeTableDTO = new TimeTableDTO(descriptor.getTimeTable().getDays(),
                descriptor.getTimeTable().getHours(),
                subjectsDTO, teachersDTO, schoolClassesDTO, rulesDTO);

        return timeTableDTO;
    }

    private Set<RuleDTO> createRulesDTOSet() {
        Set<Rule> rules = descriptor.getTimeTable().getRules();
        Set<RuleDTO> rulesDTO = new HashSet<>();

        for (Rule rule : rules) {
            rulesDTO.add(new RuleDTO(rule.getClass().getSimpleName(), rule.getRuleType().toString()));
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

    private TimeTableSolutionDTO createTimeTableSolutionDTO(TimeTableSolution solution) {
        List<QuintetDTO> quintets = createQuintetDTOList(solution.getSolutionQuintets());
        Map<RuleDTO, Double> fitnessScorePerRuleDTO = new HashMap<>();

        for (Map.Entry<Rule, Double> entry : solution.getFitnessScorePerRule().entrySet()) {
            Rule rule = entry.getKey();
            fitnessScorePerRuleDTO.put(new RuleDTO(rule.getClass().getSimpleName(), rule.getRuleType().toString()),
                    entry.getValue());
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
