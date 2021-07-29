package il.ac.mta.zuli.evolution.engine;

import java.awt.event.ActionListener;

public interface Engine {
    void loadXML(String path); //in impl of method - use other method to check if a valid file is already loaded
     void removeHandler(ActionListener handler);
     void addHandler (ActionListener handler);
    void showDetails();

    void executeEvolutionAlgo();

    void showBestSolution();

    void showEvolutionProcess();

    void leaveSystem();
}
