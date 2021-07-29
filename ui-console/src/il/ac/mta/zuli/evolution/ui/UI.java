package il.ac.mta.zuli.evolution.ui;

import il.ac.mta.zuli.evolution.dto.SubjectDTO;
import il.ac.mta.zuli.evolution.dto.TimeTableDTO;
import il.ac.mta.zuli.evolution.engine.Engine;
import il.ac.mta.zuli.evolution.engine.TimeTableEngine;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

public class UI implements ActionListener {
    Engine engine;
    public void operateMenu(){
        try {
            engine = new TimeTableEngine();
            engine.addHandler(this);
            engine.loadXML("engine/src/resources/EX1-small.xml");
            showSystemDetails();
        }catch (Exception e) {
            System.out.println(e.getMessage());
            //TODO handleException
        }
    }

    private void showSystemDetails() {
        TimeTableDTO timeTableDTO = engine.getSystemDetails();
        Map<Integer, SubjectDTO> subjects = timeTableDTO.getSubjects();
        for (Map.Entry<Integer, SubjectDTO> subjectDTO : subjects.entrySet()){
            System.out.println(subjectDTO);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println(e.getActionCommand());
    }

}
