package il.ac.mta.zuli.evolution.engine;

import il.ac.mta.zuli.evolution.dto.DescriptorDTO;
import il.ac.mta.zuli.evolution.dto.GenerationProgressDTO;
import il.ac.mta.zuli.evolution.dto.TimeTableSolutionDTO;
import il.ac.mta.zuli.evolution.engine.events.EventListener;

import java.util.List;
import java.util.function.Consumer;

public interface Engine {
    void loadXML(String fileToLoad, Consumer<Boolean> isDescriptorReady, Consumer<String> selectedFileProperty, Consumer<String> messageProperty, Runnable onFinish);

    void addListener(String name, EventListener listener);

    DescriptorDTO getSystemDetails();

    void executeEvolutionAlgorithm(int numOfGenerations, int generationsStride);

    void executeEvolutionAlgorithmWithFitnessStop(int numOfGenerations, int generationsStride, double fittnessStop);

    TimeTableSolutionDTO getBestSolution();


    List<GenerationProgressDTO> getEvolutionProgress();

    boolean isXMLLoaded();
}
