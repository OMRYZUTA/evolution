package il.ac.mta.zuli.evolution.engine;

import il.ac.mta.zuli.evolution.dto.DescriptorDTO;
import il.ac.mta.zuli.evolution.dto.GenerationProgressDTO;
import il.ac.mta.zuli.evolution.dto.TimeTableSolutionDTO;
import il.ac.mta.zuli.evolution.engine.evolutionengine.EngineSettings;
import il.ac.mta.zuli.evolution.engine.predicates.EndPredicate;
import il.ac.mta.zuli.evolution.ui.header.HeaderController;

import java.util.List;
import java.util.function.Consumer;

public interface Engine {
    void setController(HeaderController controller);

    void loadXML(String fileToLoad, Consumer<DescriptorDTO> onSuccess, Consumer<Throwable> onFailure);

    void setEngineSettings(EngineSettings settings);

    EngineSettings getEngineSettings();

    void startEvolutionAlgorithm(
            List<EndPredicate> endConditions,
            int generationsStride,
            Consumer<Boolean> onSuccess,
            Consumer<Throwable> onFailure,
            Consumer<TimeTableSolutionDTO> reportBestSolution);

    TimeTableSolutionDTO getBestSolution();

    List<GenerationProgressDTO> getEvolutionProgress();

    //used for the engine to "protect itself" even though the relevant buttons are disabled in the ui
    boolean isXMLLoaded();

    void stop();

    void pause();

    void resume(List<EndPredicate> endConditions,
                int generationsStride,
                Consumer<Boolean> onSuccess,
                Consumer<Throwable> onFailure,
                Consumer<TimeTableSolutionDTO> reportBestSolution);
}
