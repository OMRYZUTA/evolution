package il.ac.mta.zuli.evolution.ui;

import il.ac.mta.zuli.evolution.dto.*;
import il.ac.mta.zuli.evolution.engine.Engine;
import il.ac.mta.zuli.evolution.engine.TimeTableEngine;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class UI implements ActionListener {
    Engine engine;

    public void operateMenu() {
        try {
            engine = new TimeTableEngine();
            engine.addHandler(this);
            engine.loadXML("engine/src/resources/EX1-small.xml");
//            showSystemDetails();
//            TODO get parameters for evolution algorithm (and validate in engine)
            engine.executeEvolutionAlgorithm(10,1 );
        } catch (Exception e) {
            System.out.println(e.getMessage());
            //TODO handleException
        }
    }

    private void showSystemDetails() {
        DescriptorDTO descriptorDTO = engine.getSystemDetails();
        TimeTableDTO timeTableDTO = descriptorDTO.getTimeTable();
        EngineSettingsDTO engineSettingsDTO = descriptorDTO.getEngineSettings();

        printTimeTable(timeTableDTO);
        printEngineSetting(engineSettingsDTO);
        System.out.println("*********************************");
    }

    private void printEngineSetting(EngineSettingsDTO engineSettingsDTO) {
        System.out.println(String.format("population size : %d",engineSettingsDTO.getInitialPopulationSize()));
        SelectionDTO selectionDTO = engineSettingsDTO.getSelection();
        CrossoverDTO crossoverDTO = engineSettingsDTO.getCrossover();
        List<MutationDTO> mutationDTOList = engineSettingsDTO.getMutations();
        System.out.println(selectionDTO);
        System.out.println(crossoverDTO);
        printList(mutationDTOList);
    }

    private void printList(List<?> values) {
        for (Object object: values) {
            System.out.println(object);
        }
    }

    private void printTimeTable(TimeTableDTO timeTableDTO) {
        Map<Integer, SubjectDTO> subjects = timeTableDTO.getSubjects();
        Map<Integer, TeacherDTO> teachers = timeTableDTO.getTeachers();
        Map<Integer, SchoolClassDTO> schoolClasses = timeTableDTO.getSchoolClasses();
        Set<RuleDTO> rules = timeTableDTO.getRules();
        System.out.println("in ui showsystemdetails:");
        printMap(subjects);
        printMap(teachers);
        printMap(schoolClasses);
        printSet(rules);
    }

    private void printSet(Set<RuleDTO> rules) {
        for (RuleDTO rule: rules) {
            System.out.println(rule);
        }
    }

    private <K, V> void printMap(Map<K, V> map) {
        for (Map.Entry<K, V> entry : map.entrySet()) {
            System.out.println(entry.getKey() + " " + entry.getValue());
        }
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println(e.getActionCommand());
    }

}
