package il.ac.mta.zuli.evolution.engine;

import il.ac.mta.zuli.evolution.dto.DescriptorDTO;
import il.ac.mta.zuli.evolution.dto.GenerationProgressDTO;
import il.ac.mta.zuli.evolution.dto.TimeTableSolutionDTO;
import il.ac.mta.zuli.evolution.engine.events.EventListener;

import java.util.List;

public interface Engine {
    void loadXML(String path); //in impl of method - use other method to check if a valid file is already loaded

    void addListener(String name, EventListener listener);

    DescriptorDTO getSystemDetails();

    void executeEvolutionAlgorithm(int numOfGenerations, int generationsStride);
    void executeEvolutionAlgorithmWithFittnessStop(int numOfGenerations, int generationsStride, double fittnessStop);

    TimeTableSolutionDTO getBestSolution();

    //TimeTableSolutionDTO getBestSolutionRaw();

//    TimeTableSolutionDTO getBestSolutionTeacherOriented();
//
//    TimeTableSolutionDTO getBestSolutionClassOriented();

    List<GenerationProgressDTO> getEvolutionProgress();

    boolean isXMLLoaded();
}
