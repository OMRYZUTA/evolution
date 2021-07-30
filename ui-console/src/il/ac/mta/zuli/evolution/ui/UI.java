package il.ac.mta.zuli.evolution.ui;

import il.ac.mta.zuli.evolution.dto.SchoolClassDTO;
import il.ac.mta.zuli.evolution.dto.SubjectDTO;
import il.ac.mta.zuli.evolution.dto.TeacherDTO;
import il.ac.mta.zuli.evolution.dto.TimeTableDTO;
import il.ac.mta.zuli.evolution.engine.Engine;
import il.ac.mta.zuli.evolution.engine.TimeTableEngine;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

public class UI implements ActionListener {
    Engine engine;

    public void operateMenu() {
        try {
            engine = new TimeTableEngine();
            engine.addHandler(this);
            engine.loadXML("engine/src/resources/EX1-small.xml");
            showSystemDetails();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            //TODO handleException
        }
    }

    private void showSystemDetails() {
        TimeTableDTO timeTableDTO = engine.getSystemDetails();
        Map<Integer, SubjectDTO> subjects = timeTableDTO.getSubjects();
        Map<Integer, TeacherDTO> teachers = timeTableDTO.getTeachers();
        Map<Integer, SchoolClassDTO> schoolClasses = timeTableDTO.getSchoolClasses();

        System.out.println("in ui showsystemdetails:");
        printMap(subjects);
        printMap(teachers);
        printMap(schoolClasses);

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
