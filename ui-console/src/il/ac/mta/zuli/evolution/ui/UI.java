package il.ac.mta.zuli.evolution.ui;

import il.ac.mta.zuli.evolution.engine.Engine;
import il.ac.mta.zuli.evolution.engine.TimeTableEngine;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UI implements ActionListener {

    public void operateMenu(){
        try {
            Engine engine = new TimeTableEngine();
            engine.addHandler(this);
            engine.loadXML("engine/src/resources/EX1-small.xml");
        }catch (Exception e) {
            System.out.println(e.getMessage());
            //TODO handleException
        }
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println(e.getActionCommand());
    }

}
