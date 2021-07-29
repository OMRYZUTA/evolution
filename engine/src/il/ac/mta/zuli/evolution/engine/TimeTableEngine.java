package il.ac.mta.zuli.evolution.engine;

import il.ac.mta.zuli.evolution.dto.SubjectDTO;
import il.ac.mta.zuli.evolution.dto.TimeTableDTO;
import il.ac.mta.zuli.evolution.engine.data.Descriptor;
import il.ac.mta.zuli.evolution.engine.data.Subject;
import il.ac.mta.zuli.evolution.engine.evolutionengine.EvolutionEngine;
import il.ac.mta.zuli.evolution.engine.xmlparser.XMLParser;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        return new TimeTableDTO( createSubjectDTOCollection());
    }


    private Map<Integer, SubjectDTO> createSubjectDTOCollection() {
        Map<Integer, SubjectDTO> subjectDTOS = new HashMap<>();
        Map<Integer, Subject> subjects = descriptor.getTimeTable().getSubjects();
        for (Map.Entry<Integer, Subject> subject : subjects.entrySet()){
            subjectDTOS.put(subject.getKey(), new SubjectDTO(subject.getKey(),subject.getValue().getName()));
        }
        return subjectDTOS;
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
