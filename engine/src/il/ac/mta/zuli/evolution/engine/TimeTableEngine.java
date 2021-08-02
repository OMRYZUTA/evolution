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

public class TimeTableEngine implements Engine {
    private boolean isXMLLoaded = false;
    private Descriptor descriptor;
    private final XMLParser xmlParser = new XMLParser();
    private EvolutionEngine evolutionEngine;
    private List<TimeTableSolution> bestSolutionsInHistory;
    private List<ActionListener> handlers = new ArrayList<>();

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

    //SelectionDTO selection, CrossoverDTO crossover, List<MutationDTO> mutations
    private EngineSettingsDTO createEngineSettingsDTO() {
        int initialSize = descriptor.getEngineSettings().getInitialPopulationSize();
        SelectionDTO selectionDTO = createSelectionDTO();
        CrossoverDTO crossoverDTO = createCrossoverDTO();
        List<MutationDTO> mutationDTOS = createMutationDTOS();

        return new EngineSettingsDTO(initialSize, selectionDTO, crossoverDTO, mutationDTOS);
    }

    @NotNull
    private List<MutationDTO> createMutationDTOS() {
        List<Mutation> mutations = descriptor.getEngineSettings().getMutations();
        List<MutationDTO> mutationDTOS = createMutationsDTO();

        return mutationDTOS;
    }

    @NotNull
    private CrossoverDTO createCrossoverDTO() {
        Crossover<TimeTableSolution> crossover = descriptor.getEngineSettings().getCrossover();
        CrossoverDTO crossoverDTO = new CrossoverDTO(crossover.getClass().getSimpleName(), crossover.getCuttingPoints());

        return crossoverDTO;
    }

    @NotNull
    private SelectionDTO createSelectionDTO() {
        Selection<TimeTableSolution> selection = descriptor.getEngineSettings().getSelection();
        SelectionDTO selectionDTO = new SelectionDTO(selection.getClass().getSimpleName(), selection.getConfiguration());

        return selectionDTO;
    }

    private List<MutationDTO> createMutationsDTO() {
        List<MutationDTO> mutationDTOS = new ArrayList<>();

        for (Mutation mutation : descriptor.getEngineSettings().getMutations()) {
            String name = mutation.getClass().getSimpleName();
            double probability = mutation.getProbability();
            String configuration = mutation.getConfiguration();
            mutationDTOS.add(new MutationDTO(name, probability, configuration));
        }

        return mutationDTOS;
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


    @Override
    public void executeEvolutionAlgorithm(int numOfGenerations, int generationsStride) {
        //1. in loop:
        // 1a. generate as many solutions as required
        // create single solution including randomly generate numOfQuintets for a single solution
        List<TimeTableSolution> initialPopulation = new ArrayList<>();
        for (int i = 0; i <descriptor.getEngineSettings().getInitialPopulationSize() ; i++) {
            initialPopulation.add(new TimeTableSolution(descriptor.getTimeTable()));
        }
        for (TimeTableSolution solution: initialPopulation) {
            System.out.println(solution);
        }
//        evolutionEngine(engineSettings, initialPopulation)
//
//        for i of numof generations-1 :
//              evolution.execute()
//              if i% stride == 0{
//                    evolution.getBestSolution() TODO: decide whether to get the best ones before or after the execution.
//

    }


    @Override
    public void showBestSolution() {

    }

    @Override
    public void showEvolutionProcess() {

    }

    @Override
    public void leaveSystem() {

    }

    @Override
    public boolean isXMLLoaded() {
        return isXMLLoaded;
    }

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

}
