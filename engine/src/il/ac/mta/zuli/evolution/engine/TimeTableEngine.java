package il.ac.mta.zuli.evolution.engine;

import il.ac.mta.zuli.evolution.dto.*;
import il.ac.mta.zuli.evolution.engine.data.*;
import il.ac.mta.zuli.evolution.engine.evolutionengine.EvolutionEngine;
import il.ac.mta.zuli.evolution.engine.xmlparser.XMLParser;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class TimeTableEngine implements Engine {
    private Descriptor descriptor;
    private final XMLParser xmlParser = new XMLParser();
    private EvolutionEngine evolutionEngine;

    private List<ActionListener> handlers = new ArrayList<>();

    @Override
    public void loadXML(String path) {
        try {
            //TODO validatePath()
            descriptor = xmlParser.unmarshall(path);
            System.out.println(descriptor);
            fireEvent("file is loaded");
        } catch (Exception e) {
            //TODO handle exception
        }
    }

    @Override
    public TimeTableDTO getSystemDetails() {
        //DTO: list of subjects, list of teachers, list of SchoolClasses, list of rules
        Map<Integer, SubjectDTO> subjectsDTO = createSortedSubjectDTOCollection(descriptor.getTimeTable().getSubjects());
        Map<Integer, TeacherDTO> teachersDTO = createSortedTeacherDTOCollection();
        Map<Integer, SchoolClassDTO> schoolClassesDTO = createSortedClassesDTOCollection();
        TimeTableDTO timeTableDTO = new TimeTableDTO(subjectsDTO, teachersDTO, schoolClassesDTO);

        return timeTableDTO;
        //return new DescriptorDTO();
    }

    private Map<Integer, SubjectDTO> createSortedSubjectDTOCollection(Map<Integer, Subject> subjects) {
        Map<Integer, SubjectDTO> subjectDTOS = new TreeMap<>();

        for (Map.Entry<Integer, Subject> subject : subjects.entrySet()) {
            subjectDTOS.put(subject.getKey(), new SubjectDTO(subject.getKey(), subject.getValue().getName()));
        }

        return subjectDTOS; //in sorted order because of TreeMap
    }

    private Map<Integer, TeacherDTO> createSortedTeacherDTOCollection() {
        Map<Integer, TeacherDTO> teacherDTOs = new TreeMap<>();
        Map<Integer, Teacher> teachers = descriptor.getTimeTable().getTeachers();

        for (Map.Entry<Integer, Teacher> teacher : teachers.entrySet()) {
            Map<Integer, SubjectDTO> subjectsDTO = createSortedSubjectDTOCollection(teacher.getValue().getSubjects());
            teacherDTOs.put(teacher.getKey(), new TeacherDTO(teacher.getKey(), (teacher.getValue()).getName(), subjectsDTO));
        }

        return teacherDTOs; //in sorted order because of TreeMap
    }

    private Map<Integer, SchoolClassDTO> createSortedClassesDTOCollection() {
        Map<Integer, SchoolClassDTO> SchoolClassDTOs = new TreeMap<>();
        Map<Integer, SchoolClass> SchoolClasss = descriptor.getTimeTable().getSchoolClasses();

        for (Map.Entry<Integer, SchoolClass> schoolClass : SchoolClasss.entrySet()) {
            List<RequirementDTO> requirementsDTO = createRequirementsDTOList(schoolClass.getValue().getRequirements());
            SchoolClassDTOs.put(schoolClass.getKey(),
                    new SchoolClassDTO(schoolClass.getKey(),
                            (schoolClass.getValue()).getName(), requirementsDTO));
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
    public void executeEvolutionAlgo() {

        //randomly generate 1-total-required-hours quintets to create a single solution

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
