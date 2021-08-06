package il.ac.mta.zuli.evolution.engine;

import il.ac.mta.zuli.evolution.dto.*;
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
    private List<TimeTableSolution> bestSolutionsInHistory;
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

        List<TimeTableSolution> initialPopulation = getInitialGeneration();

        EvolutionEngine evolutionEngine = new EvolutionEngine(this.descriptor.getEngineSettings(),
                this.descriptor.getTimeTable().getRules());

        //numOfGenerations-1?
        System.out.println("num of generations " + numOfGenerations);

        List<TimeTableSolution> prevGeneration = initialPopulation;
        List<TimeTableSolution> currGeneration = new ArrayList<>();

        for (int i = 0; i < numOfGenerations; i++) {
            currGeneration = evolutionEngine.execute(prevGeneration);

            System.out.println("generation " + i);
            int k = 1;
            for (TimeTableSolution solution : currGeneration) {
                System.out.println(k + ". " + solution.getTotalFitnessScore());
                k++;
            }

            prevGeneration = currGeneration;
        }

//        for i of numof generations-1 :
//              evolution.execute()
//              if i% stride == 0{
//                    evolution.getBestSolution() TODO: decide whether to get the best ones before or after the execution.
//

    }

    @Override
    public void showBestSolution() {
        TimeTableSolution solution;
        //TODO implement properly
        // where will we get this solution from?

        //Option1: RAW with Quintets with this specific order: <D,H,C,T,S>


        // Option2: solution per teacher
        List<Integer> teacherIDs = (((descriptor.getTimeTable()).getTeachers()).keySet()).stream()
                .sorted().collect(Collectors.toList());

        for (Integer teacherID : teacherIDs) {
            //solution.getSubSolutionForTeacher(teacherID);
        }

        // Option2: solution per class
        List<Integer> classIDs = (((descriptor.getTimeTable()).getSchoolClasses()).keySet()).stream()
                .sorted().collect(Collectors.toList());

        for (Integer classID : classIDs) {
            //solution.getSubSolutionForTeacher(teacherID);
        }

    }

    @Override
    public void showEvolutionProcess() {

    }

    @Override
    public void leaveSystem() {
    }

    //#endregion

    //#region auxiliary methods
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

    private Map<Integer, TeacherDTO> createSortedTeacherDTOCollection() {
        Map<Integer, TeacherDTO> teacherDTOs = new TreeMap<>();
        Map<Integer, Teacher> teachers = descriptor.getTimeTable().getTeachers();

        for (Teacher teacher : teachers.values()) {
            Map<Integer, SubjectDTO> subjectsDTO = createSortedSubjectDTOCollection(teacher.getSubjects());
            teacherDTOs.put(teacher.getId(), new TeacherDTO(teacher.getId(), teacher.getName(), subjectsDTO));
        }

        return teacherDTOs; //in sorted order because of TreeMap
    }

    private Map<Integer, SchoolClassDTO> createSortedClassesDTOCollection() {
        Map<Integer, SchoolClassDTO> SchoolClassDTOs = new TreeMap<>();
        Map<Integer, SchoolClass> SchoolClass = descriptor.getTimeTable().getSchoolClasses();

        for (SchoolClass schoolClass : SchoolClass.values()) {
            List<RequirementDTO> requirementsDTO = createRequirementsDTOList(schoolClass.getRequirements());
            SchoolClassDTOs.put(schoolClass.getId(),
                    new SchoolClassDTO(schoolClass.getId(),
                            schoolClass.getName(), requirementsDTO));
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
        ActionEvent myEvent = new ActionEvent(this, 3, message);
        List<ActionListener> handlersToInvoke = new ArrayList<>(handlers);
        for (ActionListener handler : handlersToInvoke) {
            handler.actionPerformed(myEvent);
        }
    }
    //#endregion
}
