package il.ac.mta.zuli.evolution.engine;

import il.ac.mta.zuli.evolution.dto.DescriptorDTO;

import java.awt.event.ActionListener;

public interface Engine {
    void loadXML(String path); //in impl of method - use other method to check if a valid file is already loaded
     void removeHandler(ActionListener handler);
     void addHandler (ActionListener handler);
    DescriptorDTO getSystemDetails(); //TODO change return value to descriptorDTO

    void executeEvolutionAlgo();

    void showBestSolution();

    void showEvolutionProcess();

    void leaveSystem();
}
