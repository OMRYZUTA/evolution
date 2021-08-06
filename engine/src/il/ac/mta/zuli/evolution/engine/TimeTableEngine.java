package il.ac.mta.zuli.evolution.engine;

import il.ac.mta.zuli.evolution.dto.*;
import il.ac.mta.zuli.evolution.engine.events.OnStrideEvent;
import il.ac.mta.zuli.evolution.engine.evolutionengine.EvolutionEngine;
import il.ac.mta.zuli.evolution.engine.evolutionengine.crossover.Crossover;
import il.ac.mta.zuli.evolution.engine.evolutionengine.mutation.Mutation;
import il.ac.mta.zuli.evolution.engine.evolutionengine.selection.Selection;
import il.ac.mta.zuli.evolution.engine.rules.Rule;
import il.ac.mta.zuli.evolution.engine.timetable.Requirement;
import il.ac.mta.zuli.evolution.engine.timetable.SchoolClass;
import il.ac.mta.zuli.evolution.engine.timetable.Subject;
import il.ac.mta.zuli.evolution.engine.timetable.Teacher;
import il.ac.mta.zuli.evolution.engine.xmlparser.XMLParser;
import org.jetbrains.annotations.NotNull;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.stream.Collectors;

public class TimeTableEngine implements Engine {
    private boolean isXMLLoaded = false;
    private Descriptor descriptor;
    private final XMLParser xmlParser = new XMLParser();
    private EvolutionEngine evolutionEngine;
    private Map<Integer, TimeTableSolution> bestSolutionsInGeneration; // generation , solution
    private List<ActionListener> handlers = new ArrayList<>();

    public TimeTableEngine() {
    }

    //#region ui-parallel methods
    @Override
    public void loadXML(String path) {
        try {
            //TODO validatePath()
            descriptor = xmlParser.unmarshall(path);
            fireEvent("in loadXml in TTengine: file is loaded");
            if (descriptor != null) {
                isXMLLoaded = true;
            }
        } catch (Exception e) {
            //TODO handle exception
            System.out.println(e);
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public DescriptorDTO getSystemDetails() {
        //DTO: list of subjects, list of teachers, list of SchoolClasses, list of rules
        TimeTableDTO timeTableDTO = createTimeTableDTO();
        EngineSettingsDTO engineSettingsDTO = createEngineSettingsDTO();

        return new DescriptorDTO(timeTableDTO, engineSettingsDTO);
    }

    @Override
    public void executeEvolutionAlgorithm(int numOfGenerations, int generationsStride) {
        bestSolutionsInGeneration = new HashMap<>(numOfGenerations);
        List<TimeTableSolution> initialPopulation = getInitialGeneration();

        EvolutionEngine evolutionEngine = new EvolutionEngine(this.descriptor.getEngineSettings(),
                this.descriptor.getTimeTable().getRules());

        List<TimeTableSolution> prevGeneration = initialPopulation;
        List<TimeTableSolution> currGeneration;

        for (int i = 0; i < numOfGenerations; i++) {
            currGeneration = evolutionEngine.execute(prevGeneration);

            if (i % generationsStride == 0) {
                TimeTableSolution bestSolution = currGeneration.stream().
                        sorted(Collections.reverseOrder()).limit(1).collect(Collectors.toList()).get(0);
                bestSolutionsInGeneration.put(i, bestSolution);
                GenerationStrideScoreDTO strideScoreDTO = new GenerationStrideScoreDTO(i, bestSolution.getTotalFitnessScore());
                fireEvent2(strideScoreDTO);
            }

            prevGeneration = currGeneration;
        }

//        System.out.println("****best in generation****");
//        //private Map<Integer, TimeTableSolution> bestSolutionsInGeneration;
//        for (Map.Entry<Integer, TimeTableSolution> entry : bestSolutionsInGeneration.entrySet()) {
//            System.out.println(entry.getKey() + ". " + entry.getValue().getTotalFitnessScore());
//        }
    }

    @Override
    public void showBestSolution() {

        // Option2: solution per teacher


        // Option2: solution per class


    }

    @Override
    public TimeTableSolutionDTO getBestSolutionRaw() {
        TimeTableSolution bestSolution = getBestSolution();
        TimeTableSolution sortedSolution = bestSolution.sortQuintetsInSolution(Quintet.getRawComparator());

        return createTimeTableSolutionDTO(sortedSolution);
    }


    @Override
    public TimeTableSolutionDTO getBestSolutionTeacherOriented() {
        TimeTableSolution bestSolution = getBestSolution();
        TimeTableSolution sortedSolution = bestSolution.sortQuintetsInSolution(Quintet.getTeacherComparator());

        return createTimeTableSolutionDTO(sortedSolution);
    }

    @Override
    public TimeTableSolutionDTO getBestSolutionClassOriented() {
        TimeTableSolution bestSolution = getBestSolution();
        TimeTableSolution sortedSolution = bestSolution.sortQuintetsInSolution(Quintet.getSchoolClassComparator());

        return createTimeTableSolutionDTO(sortedSolution);
    }

    @Override
    public void showEvolutionProcess() {

    }

    @Override
    public void leaveSystem() {
    }

    //#endregion

    //#region auxiliary methods
    private TimeTableSolution getBestSolution() {
        return bestSolutionsInGeneration.values().stream()
                .sorted(Collections.reverseOrder())
                .limit(1).collect(Collectors.toList()).get(0);
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
        return isXMLLoaded;
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
        CrossoverDTO crossoverDTO = new CrossoverDTO(crossover.getClass().getSimpleName(), crossover.getNumOfCuttingPoints());

        return crossoverDTO;
    }

    @NotNull
    private SelectionDTO createSelectionDTO() {
        Selection<TimeTableSolution> selection = descriptor.getEngineSettings().getSelection();
        SelectionDTO selectionDTO = new SelectionDTO(selection.getClass().getSimpleName(), selection.getConfiguration());

        return selectionDTO;
    }

    @NotNull
    private TimeTableDTO createTimeTableDTO() {
        Map<Integer, SubjectDTO> subjectsDTO = createSortedSubjectDTOCollection(descriptor.getTimeTable().getSubjects());
        Map<Integer, TeacherDTO> teachersDTO = createSortedTeacherDTOCollection();
        Map<Integer, SchoolClassDTO> schoolClassesDTO = createSortedClassesDTOCollection();
        Set<RuleDTO> rulesDTO = createRulesDTOSet();
        TimeTableDTO timeTableDTO = new TimeTableDTO(subjectsDTO, teachersDTO, schoolClassesDTO, rulesDTO);

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
            List<RequirementDTO> requirementsDTO = createRequirementsDTOList(schoolClass.getRequirements());
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

    //#region event-related methods
    public void EventsGeneratorComponent() {
        handlers = new ArrayList<>();
    }

    public void addHandler(ActionListener handler) {
        if (handler != null && !handlers.contains(handler)) {
            handlers.add(handler);
        }
    }

    public void removeHandler(ActionListener handler) {
        handlers.remove(handler);
    }

    private void fireEvent(String message) {
        ActionEvent myEvent = new ActionEvent(this, 1, message);
        List<ActionListener> handlersToInvoke = new ArrayList<>(handlers);
        for (ActionListener handler : handlersToInvoke) {
            handler.actionPerformed(myEvent);
        }
    }

    private void fireEvent2(GenerationStrideScoreDTO generationStrideScoreDTO) {
        OnStrideEvent onStrideEvent = new OnStrideEvent(this, 3, "generationStride", generationStrideScoreDTO);
        List<ActionListener> handlersToInvoke = new ArrayList<>(handlers);
        for (ActionListener handler : handlersToInvoke) {
            handler.actionPerformed(onStrideEvent);
        }
    }
    //#endregion
}
